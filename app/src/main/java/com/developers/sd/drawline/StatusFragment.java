package com.developers.sd.drawline;

import android.content.Context;
import android.graphics.*;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatusFragment extends Fragment {

    private RecyclerView recyclerView;
    private LineAdapter lineAdapter;
    private List<Line> lineList = new ArrayList<>();
    private List<Integer> ars = new ArrayList<>();

    private final String TAG = "StatusFragment";

    private MqttAndroidClient client;
    private String clientId;
    private String topic_pub = "bus14/pub";
    private String topic_sub = "bus14/#";
    private FloatingActionButton fab;

    @Override
    public void onSaveInstanceState(Bundle onOrientChange) {
        super.onSaveInstanceState(onOrientChange);
        onOrientChange.putParcelableArrayList("linelist", (ArrayList<? extends Parcelable>) lineList);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!= null){
            lineList = savedInstanceState.getParcelableArrayList("linelist");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        lineAdapter = new LineAdapter(lineList);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(lineAdapter);

        for (int i = 1; i<=20; i++) {
            Line line = new Line("Line "+i, 0);
            lineList.add(line);
        }
        lineAdapter.notifyDataSetChanged();

        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
//        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MqttMessage status = new MqttMessage(Arrays.toString(lineAdapter.getStatusList()).getBytes());
                Log.e(TAG, status.toString());
                try {
                    client.publish(topic_pub, status);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });


        clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getContext(), "tcp://broker.hivemq.com:1883", clientId);
        client.setCallback(new MqttCallbackHandler(client));

        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
//            options.setUserName("fog");
//            options.setPassword("1234".toCharArray());
            IMqttToken token = client.connect(options);

            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
//                    Log.e(TAG, "onSuccess");
//                    MqttMessage message = new MqttMessage("Hello, I am an Android Mqtt Client.".getBytes());
//                    message.setQos(2);
//                    message.setRetained(false);

                    try {
//                        client.publish(topic, message);
                        Log.e("mqtt", "Message published");
                        MqttMessage status = new MqttMessage(Arrays.toString(lineAdapter.getStatusList()).getBytes());
                        Log.e(TAG, status.toString());
                        client.publish(topic_pub, status);
                    }
                    catch (MqttPersistenceException e) {
                        e.printStackTrace();
                    }
                    catch (MqttException e) {
                        e.printStackTrace();
                    }

                    try {
                        IMqttToken subToken = client.subscribe(topic_sub, 1);
                        subToken.setActionCallback(new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                Log.e("mqtt", "subscription success");
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken,
                                                  Throwable exception) {
                                // The subscription could not be performed, maybe the user was not
                                // authorized to subscribe on the specified topic e.g. using wildcards
                                Log.e("mqtt", "subscription failed");

                            }
                        });

                    } catch (MqttException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.e(TAG, "onFailure");

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        return view;
    }

    public class LineHolder extends RecyclerView.ViewHolder {

        public Line mline;
        public TextView title;
        public ToggleButton status;

        public LineHolder(final View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            status = (ToggleButton) itemView.findViewById(R.id.status);
        }

        public void bindLine(Line line){
            mline = line;
            title.setText(mline.getTitle());
            if(mline.getStatus() == 1 || mline.getStatus() == 2) {
                status.setChecked(true);
            }
            else {
                status.setChecked(false);
            }
            status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        mline.setStatus(1);
                        Log.e("set checked", String.valueOf(mline.getTitle()) +" = " +mline.getStatus());
                    }
                    if(!isChecked){
                        mline.setStatus(0);
                        Log.e("set checked", String.valueOf(mline.getTitle()) +" = " +mline.getStatus());
                    }
                }
            });
        }
    }

    public class LineAdapter extends RecyclerView.Adapter<LineHolder>{

        public LineAdapter(List<Line> lines) {
            lineList = lines;
        }

        @Override
        public LineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater
                    .inflate(R.layout.item_list, parent, false);
            return new LineHolder(view);
        }

        @Override
        public void onBindViewHolder(LineHolder holder, int position) {
            final Line line = lineList.get(position);
            holder.bindLine(line);
        }

        @Override
        public int getItemCount() {
            return lineList.size();
        }

        public void setNotes(List<Line> lines) {
            lineList = lines;
        }

        public int[] getStatusList() {
            int[] status_list = new int[20];
            for (int i=0; i<=19; i++) {
                status_list[i] = lineList.get(i).getStatus();
            }
            return status_list;
        }
    }

    class MqttCallbackHandler implements MqttCallbackExtended {

        private final MqttAndroidClient client;

        public MqttCallbackHandler (MqttAndroidClient client)
        {
            this.client=client;
        }

        @Override
        public void connectComplete(boolean b, String s) {
            Log.w("mqtt", s);
        }

        @Override
        public void connectionLost(Throwable throwable) {

        }

        @Override
        public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
            Log.e("mqtt", mqttMessage.toString());
            ars = s2a(mqttMessage.toString());
            Log.e("s2a", String.valueOf(ars));
            setStatusList();
            lineAdapter.notifyDataSetChanged();
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

        }
    }

    public List<Integer> s2a(String string){
        List<Integer> status = new ArrayList<>();
        for(int i = 1; i< string.length(); i =i+3){
            int decimal = (int) string.charAt(i) - 48;
            status.add(decimal);
        }
        return status;
    }

    public void setStatusList() {
        for (int i=0; i<=19; i++) {
            lineList.get(i).setStatus(ars.get(i));
        }
    }

}
