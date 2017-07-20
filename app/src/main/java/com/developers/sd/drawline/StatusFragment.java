package com.developers.sd.drawline;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
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
import java.util.List;

public class StatusFragment extends Fragment {

    private RecyclerView recyclerView;
    private LineAdapter lineAdapter;
    private List<Line> lineList = new ArrayList<>();

    private final String TAG = "StatusFragment";

    private MqttAndroidClient client;
    private String clientId;
    private String topic = "topic/fog";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getContext(), "tcp://broker.hivemq.com:1883", clientId);
        client.setCallback(new MqttCallbackHandler(client));

        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
            //options.setUserName("fog");
            //options.setPassword("1234".toCharArray());
            IMqttToken token = client.connect(options);

            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.e(TAG, "onSuccess");
                    MqttMessage message = new MqttMessage("Hello, I am an Android Mqtt Client.".getBytes());
                    message.setQos(2);
                    message.setRetained(false);

                    try {
                        client.publish(topic, message);
                        Log.e("mqtt", "Message published");
                        MqttMessage status = new MqttMessage(lineAdapter.getStatusList().toString().getBytes());
                        Log.e(TAG, status.toString());
                        client.publish(topic, status);
                    }
                    catch (MqttPersistenceException e) {
                        e.printStackTrace();
                    }
                    catch (MqttException e) {
                        e.printStackTrace();
                    }

                    try {
                        IMqttToken subToken = client.subscribe(topic, 1);
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


    public class LineAdapter extends RecyclerView.Adapter<LineAdapter.MyViewHolder> {

        private List<Line> lineList;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView title;
            public ToggleButton status;

            public MyViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.title);
                status = (ToggleButton) view.findViewById(R.id.status);
            }
        }


        public LineAdapter(List<Line> lineList) {
            this.lineList = lineList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final Line line = lineList.get(position);
            holder.title.setText(line.getTitle());
            if (line.getStatus() == 0) {
                holder.status.setChecked(true);
            } else {
                holder.status.setChecked(false);
            }
            holder.status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        line.setStatus(0);
                    } else {
                        line.setStatus(1);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return lineList.size();
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
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

        }
    }

}
