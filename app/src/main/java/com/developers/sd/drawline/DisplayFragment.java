package com.developers.sd.drawline;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ListPopupWindow;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DisplayFragment extends Fragment {

//    ImageView drawingImageView;
    PhotoView drawingImageView;
    Paint paint2 = new Paint();
    int color = Color.BLACK;
    double xx,yy;

    private final String TAG = "DisplayFragment";

    private MqttAndroidClient client;
    private String clientId;
    private String topic_sub = "bus14/#";
    private FloatingActionButton fab;
    private List<Integer> ars = new ArrayList<>();
    private List<Integer> colors = new ArrayList<>();

    private boolean check = true;

    @Override
    public void onSaveInstanceState(Bundle onOrientChange) {
        super.onSaveInstanceState(onOrientChange);
        onOrientChange.putIntegerArrayList("colors", (ArrayList<Integer>) colors);
        onOrientChange.putBoolean("check",check);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!= null){
            colors = savedInstanceState.getIntegerArrayList("colors");
            check = savedInstanceState.getBoolean("check");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_display, container, false);
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        drawingImageView = (PhotoView) view.findViewById(R.id.DrawingImageView);
//        drawingImageView = (ImageView) view.findViewById(R.id.DrawingImageView);

        if(check){
            List<Integer> status = new ArrayList<>();
            status = initArrayList();
            colors = i2c(status);
            Log.e("colors", String.valueOf(colors));
            check = false;
        }

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
                    Log.e(TAG, "onSuccess");

                    try {
                        IMqttToken subToken = client.subscribe(topic_sub, 1);
                        subToken.setActionCallback(new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                Log.e("D-mqtt", "subscription success");
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken,
                                                  Throwable exception) {
                                // The subscription could not be performed, maybe the user was not
                                // authorized to subscribe on the specified topic e.g. using wildcards
                                Log.e("D-mqtt", "subscription failed");

                            }
                        });

                    } catch (MqttException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.e(TAG, "D-onFailure");

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        loadCanvas();
        return view;
    }

    private void loadCanvas(){

        Bitmap bitmap = Bitmap.createBitmap((int) 1600, (int) 2000, Bitmap.Config.ARGB_8888);
        drawingImageView.setImageBitmap(bitmap);

        //creating canvas
        Canvas canvas = new Canvas(bitmap);

        Paint paint0 = new Paint();
        paint0.setColor(Color.BLACK);
        paint0.setTextSize(40);

        Paint paint1 = new Paint();
        paint1.setColor(Color.BLACK);
        //G circle 1
        paint1.setStrokeWidth(6);
        paint1.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(80, 900, 40, paint1);
        Paint paint2 = new Paint();
        paint2.setColor(Color.BLACK);
        //G text 1
        paint2.setTextSize(60);
        canvas.drawText("G", 58, 920, paint2);
        Paint paint3 = new Paint();
        paint3.setColor(Color.BLACK);
        //v line G1
        paint3.setStrokeCap(Paint.Cap.ROUND);
        paint3.setStrokeWidth(6);
        canvas.drawLine(80, 950, 80, 980, paint3);

        Paint paint4 = new Paint();
        paint4.setColor(Color.BLACK);
        //Bus 1
        paint4.setStrokeCap(Paint.Cap.ROUND);
        paint4.setStrokeWidth(9);
        canvas.drawLine(00, 980, 160, 980, paint4);

        Paint paint5 = new Paint();
//        paint5.setColor(Color.GREEN);
        paint5.setColor(colors.get(0));
        //line 1
        paint5.setStrokeCap(Paint.Cap.ROUND);
        paint5.setStrokeWidth(6);
        canvas.drawLine(50, 990, 50, 1050, paint5);
        //line 1 text
        canvas.drawText("1",80,1300,paint0);
        //Slant line 1
        canvas.drawLine(50, 1050, 200, 1400, paint5);
        //Lower vertical line 1
        canvas.drawLine(200, 1400, 200, 1450, paint5);


        Paint paint7 = new Paint();
//        paint7.setColor(Color.GREEN);
        paint7.setColor(colors.get(1));
        //line 2
        //line 2 text
        canvas.drawText("2",300,1070,paint0);
        paint7.setStrokeCap(Paint.Cap.ROUND);
        paint7.setStrokeWidth(6);
        canvas.drawLine(100, 990, 100, 1030, paint7);
        //SLL2
        canvas.drawLine(100, 1030, 600, 1200, paint7);
        //vertical to bus 5
        canvas.drawLine(600, 1200, 600, 1150, paint7);


        //Bus 2
        canvas.drawLine(180, 1450, 350, 1450, paint4);
        //vertical line connecting the above two
        canvas.drawLine(230, 1455, 230, 1490, paint3);
        //Generator 2
        canvas.drawCircle(230, 1530, 40, paint1);
        //Generator 2 text
        canvas.drawText("G", 210, 1550, paint2);

        Paint paint9 = new Paint();
//        paint9.setColor(Color.GREEN);
        paint9.setColor(colors.get(4));
        //line 5
        paint9.setStrokeCap(Paint.Cap.ROUND);
        //line 5 text
        canvas.drawText("5",400,1300,paint0);
        paint9.setStrokeWidth(6);
        //vertical part of 5
        canvas.drawLine(230, 1445, 230, 1380, paint9);
        //Slanting line associated with 5
        canvas.drawLine(230, 1380, 630, 1215, paint9);
        //vertical connecting 5
        canvas.drawLine(630,1215,630,1150,paint9);

        //Bus 5
        canvas.drawLine(580, 1150, 680, 1150, paint4);

        //line 10
        Paint paint10 = new Paint();
//        paint10.setColor(Color.GREEN);
        paint10.setColor(colors.get(9));
        paint10.setStrokeCap(Paint.Cap.ROUND);
        paint10.setStrokeWidth(6);
        canvas.drawLine(630, 1144, 630, 982, paint10);
        //line 10 text
        canvas.drawText("10",650,1100,paint0);

        //Bus 6
        canvas.drawLine(550, 982, 740, 982, paint4);
        //vertical line connecting 6
        canvas.drawLine(590, 982, 590, 1025, paint3);
        //horizontal line connecting 6
        canvas.drawLine(590, 1025, 510, 1025, paint3);
        //Generator 6
        canvas.drawCircle(470, 1025, 40, paint1);
        //Generator 6 text
        canvas.drawText("G", 450, 1040, paint2);

        //Line 12
        Paint paint11 = new Paint();
//        paint11.setColor(Color.GREEN);
        paint11.setColor(colors.get(10));
        paint11.setStrokeCap(Paint.Cap.ROUND);
        paint11.setStrokeWidth(6);
        //vertical part of 12
        canvas.drawLine(580, 975, 580, 920, paint11);
        //Slanting line associated with 12
        canvas.drawLine(580, 920, 350, 630, paint11);
        //vertical connecting 12
        canvas.drawLine(350,630,350,590,paint11);
        //line 12 text
        canvas.drawText("12",380,800,paint0);

        //Bus 12
        canvas.drawLine(340,580,420,580,paint4);

        //Line 19
        Paint paint12 = new Paint();
//        paint12.setColor(Color.GREEN);
        paint12.setColor(colors.get(18));
        paint12.setStrokeCap(Paint.Cap.ROUND);
        paint12.setStrokeWidth(6);
        //vertical part of 19
        canvas.drawLine(410, 590, 410, 610, paint12);
        //horizontal part of 19
        canvas.drawLine(410, 610, 480, 610, paint12);
        //Slanting line associated with 19
        canvas.drawLine(480, 610, 550, 300, paint12);
        //vertical connecting 19
        canvas.drawLine(550,300,550,250,paint12);
        //line 19 text
        canvas.drawText("19",440,450,paint0);

        //Bus 13
        canvas.drawLine(530,240,700,240,paint4);

        //Line 13
        Paint paint13 = new Paint();
//        paint13.setColor(Color.GREEN);
        paint13.setColor(colors.get(12));
        paint13.setStrokeCap(Paint.Cap.ROUND);
        paint13.setStrokeWidth(6);
        canvas.drawLine(630, 972, 630, 250, paint13);
        //line 13 text
        canvas.drawText("13",580,700,paint0);

        //Line 20
        Paint paint14 = new Paint();
//        paint14.setColor(Color.GREEN);
        paint14.setColor(colors.get(19));
        paint14.setStrokeCap(Paint.Cap.ROUND);
        paint14.setStrokeWidth(6);
        //vertical part of 20
        canvas.drawLine(675, 250, 675, 300, paint14);
        //Slanting line associated with 20
        canvas.drawLine(675, 300, 920, 500, paint14);
        //horizontal part of 20
        canvas.drawLine(920, 500, 980, 500, paint14);
        //vertical connecting 20
        canvas.drawLine(980,500,980,440,paint14);
        //line 20 text
        canvas.drawText("20",780,380,paint0);

        //Bus 14
        canvas.drawLine(960,430,1050,430,paint4);


        //Line 11
        Paint paint15 = new Paint();
//        paint15.setColor(Color.GREEN);
        paint15.setColor(colors.get(10));
        paint15.setStrokeCap(Paint.Cap.ROUND);
        paint15.setStrokeWidth(6);
        //vertical part of 11
        canvas.drawLine(690, 972, 690, 922, paint15);
        //Slanting line associated with 11
        canvas.drawLine(690, 922, 790, 822, paint15);
        //vertical connecting 11
        canvas.drawLine(790,822,790,760,paint15);
        //line 11 text
        canvas.drawText("11",710,860,paint0);


        //Bus 11
        canvas.drawLine(770,750,870,750,paint4);

        //Line 18
        Paint paint16 = new Paint();
//        paint16.setColor(Color.GREEN);
        paint16.setColor(colors.get(17));
        paint16.setStrokeCap(Paint.Cap.ROUND);
        paint16.setStrokeWidth(6);
        //vertical part of 18
        canvas.drawLine(850, 760, 850, 822, paint16);
        //Slanting line associated with 18
        canvas.drawLine(850, 822, 950, 922, paint16);
        //horizontal part of 18
        canvas.drawLine(950, 922, 980, 922, paint16);
        //vertical connecting 18
        canvas.drawLine(980,922,980,840,paint16);
        //line 18 text
        canvas.drawText("18",890,860,paint0);

        //Bus 10
        canvas.drawLine(970,830,1050,830,paint4);

        //Line 16
        Paint paint17 = new Paint();
//        paint17.setColor(Color.GREEN);
        paint17.setColor(colors.get(15));
        paint17.setStrokeCap(Paint.Cap.ROUND);
        paint17.setStrokeWidth(6);
        //vertical part of 16
        canvas.drawLine(1025, 840, 1025, 870, paint17);
        //Slanting line associated with 16
        canvas.drawLine(1025, 870, 1100, 930, paint17);
        //vertical connecting 16
        canvas.drawLine(1100,930,1100,960,paint17);
        //line 16 text
        canvas.drawText("16",1020,940,paint0);

        //Bus 9
        canvas.drawLine(1050,970,1200,970,paint4);

        //Line 17
        Paint paint18 = new Paint();
//        paint18.setColor(Color.GREEN);
        paint18.setColor(colors.get(16));
        paint18.setStrokeCap(Paint.Cap.ROUND);
        paint18.setStrokeWidth(6);
        //vertical part of 17
        canvas.drawLine(1040, 440, 1040, 470, paint18);
        //Slanting line associated with 17
        canvas.drawLine(1040, 470, 1150, 920, paint18);
        //vertical connecting 17
        canvas.drawLine(1150,920,1150,960,paint18);
        //line 17 text
        canvas.drawText("17",1100,700,paint0);

        //Line 15
        Paint paint19 = new Paint();
//        paint19.setColor(Color.GREEN);
        paint19.setColor(colors.get(14));
        paint19.setStrokeCap(Paint.Cap.ROUND);
        paint19.setStrokeWidth(6);
        //vertical part of 15
        canvas.drawLine(1150, 980, 1150, 1000, paint19);
        //Slanting line associated with 15
        canvas.drawLine(1150, 1000, 1250, 1050, paint19);
        //vertical connecting 15
        canvas.drawLine(1250,1050,1250,1090,paint19);
        //line 15 text
        canvas.drawText("15",1200,1025,paint0);

        //Bus 7
        canvas.drawLine(1235,1100,1325,1100,paint4);

        //Line 8
        Paint paint20 = new Paint();
        paint20.setColor(Color.GREEN);
        paint20.setColor(colors.get(7));
        paint20.setStrokeCap(Paint.Cap.ROUND);
        paint20.setStrokeWidth(6);
        //vertical part of 8
        canvas.drawLine(1250, 1110, 1250, 1160, paint20);
        //Slanting line associated with 15
        canvas.drawLine(1250, 1160, 1150, 1210, paint20);
        //vertical connecting 8
        canvas.drawLine(1150,1210,1150,1230,paint20);
        //line 8 text
        canvas.drawText("8",1200,1220,paint0);

        //Bus 4
        canvas.drawLine(1025,1240,1200,1240,paint4);

        //Line 9
        Paint paint21 = new Paint();
//        paint21.setColor(Color.GREEN);
        paint21.setColor(colors.get(8));
        paint21.setStrokeCap(Paint.Cap.ROUND);
        paint21.setStrokeWidth(6);
        //vertical connecting 9
        canvas.drawLine(1100,980,1100,1230,paint17);
        //line 9 text
        canvas.drawText("9",1050,1100,paint0);

        //Line 14
        Paint paint22 = new Paint();
//        paint22.setColor(Color.GREEN);
        paint22.setColor(colors.get(13));
        paint22.setStrokeCap(Paint.Cap.ROUND);
        paint22.setStrokeWidth(6);
        //vertical connecting 14
        canvas.drawLine(1300 ,1090,1300,1020,paint22);
        //horizontal connecting 14
        canvas.drawLine(1300 ,1020,1450,1020,paint22);
        //line 14 text
        canvas.drawText("14",1350,1000,paint0);

        //Bus 8
        canvas.drawLine(1455,980,1455,1050,paint4);
        //horizontal line connecting the above two
        canvas.drawLine(1455, 1020, 1500, 1020, paint3);
        //Generator 8
        canvas.drawCircle(1540, 1020, 40, paint1);
        //Generator 8 text
        canvas.drawText("G", 1525, 1035, paint2);


        //Line 7
        Paint paint23 = new Paint();
//        paint23.setColor(Color.GREEN);
        paint23.setColor(colors.get(6));
        paint23.setStrokeCap(Paint.Cap.ROUND);
        paint23.setStrokeWidth(6);
        //vertical part of 7
        canvas.drawLine(660, 1160, 660, 1280, paint23);
        //horizontal line associated with 7
        canvas.drawLine(660, 1280, 1050, 1280, paint23);
        //vertical connecting 7
        canvas.drawLine(1050,1280,1050,1250,paint23);
        //line 7 text
        canvas.drawText("7",800,1260,paint0);

        //Line 4
        Paint paint24 = new Paint();
//        paint24.setColor(Color.GREEN);
        paint24.setColor(colors.get(3));
        paint24.setStrokeCap(Paint.Cap.ROUND);
        paint24.setStrokeWidth(6);
        //vertical part of 4
        canvas.drawLine(280, 1445, 280, 1410, paint24);
        //horizontal line associated with 7
        canvas.drawLine(280, 1410, 1090, 1320, paint24);
        //vertical connecting 4
        canvas.drawLine(1090,1320,1090,1250,paint24);
        //line 4 text
        canvas.drawText("4",600,1360,paint0);

        //Line 6
        Paint paint25 = new Paint();
//        paint25.setColor(Color.GREEN);
        paint25.setColor(colors.get(5));
        paint25.setStrokeCap(Paint.Cap.ROUND);
        paint25.setStrokeWidth(6);
        //vertical part of 6
        canvas.drawLine(1120, 1250, 1120, 1520, paint25);
        //line 6 text
        canvas.drawText("6",1090,1400,paint0);

        //Bus 3
        canvas.drawLine(1020,1520,1220,1520,paint4);
        //vertical line connecting the above two
        canvas.drawLine(1120, 1520, 1120, 1570, paint3);
        //Generator 3
        canvas.drawCircle(1120, 1610, 40, paint1);
        //Generator 3 text
        canvas.drawText("G", 1100, 1630, paint2);

        //Line 3
        Paint paint26 = new Paint();
//        paint26.setColor(Color.GREEN);
        paint26.setColor(colors.get(2));
        paint26.setStrokeCap(Paint.Cap.ROUND);
        paint26.setStrokeWidth(6);
        //vertical part of 3
        canvas.drawLine(280, 1460, 280, 1480, paint26);
        //horizontal line associated with 3
        canvas.drawLine(280, 1480, 1060, 1480, paint26);
        //vertical connecting 3
        canvas.drawLine(1060,1480,1060,1510,paint26);
        //line 3 text
        canvas.drawText("3",800,1460,paint0);

    }

    private class BackgroundFunctions extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground (Void...params) {
            loadCanvas();
            return null;
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
            Log.e("mqtt-d", mqttMessage.toString());
            ars = s2a(mqttMessage.toString());
            Log.e("s2a-d", String.valueOf(ars));
            colors = i2c(ars);
            Log.e("i2c-d", String.valueOf(ars));
            loadCanvas();
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

    public List<Integer> i2c(List<Integer> array){
        List<Integer> status = new ArrayList<>();
        if(array.size() > 20){
            Toast.makeText(getContext(),"Wrong data, try again",Toast.LENGTH_SHORT).show();}
        for(int i = 0; i< array.size(); i++){
            if(array.get(i)== 0){status.add(Color.BLACK);}
            if(array.get(i)== 1){status.add(Color.GREEN);}
            if(array.get(i)== 2){status.add(Color.RED);}
        }
        return status;
    }

    public ArrayList<Integer> initArrayList()
    {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < 20;i++)
        {
            list.add(0);
        }
        return list;
    }
}
