package com.developers.sd.drawline;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.ListPopupWindow;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MqttAndroidClient client;
    String clientId;
    String topic = "topic";
    String Message;

    ImageView drawingImageView;
    int color = Color.BLACK;
    double xx,yy;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private final String TAG = "TAG";
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new BackgroundFunctions().execute();


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        clientId = MqttClient.generateClientId();
        Log.e("client-id",clientId);
        client = new MqttAndroidClient(this.getApplicationContext(), "tcp://broker.hivemq.com:1883",
                        clientId);
        Log.e("client", String.valueOf(client));


        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
//            options.setUserName("USERNAME");
//            options.setPassword("PASSWORD".toCharArray());
            String topic = "users/last/will";
            byte[] payload = "some payload".getBytes();
            options.setWill(topic, payload ,1,false);
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    subscribetoTopic();
                    Log.d(TAG, "onSuccess");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d(TAG, "onFailure");

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }


//        final String[] colorchoice = {"ON","OFF"};
//        final ListPopupWindow lpw = new ListPopupWindow(this);
//        lpw.setWidth(800);
//        lpw.setHeight(400);
//        lpw.setHorizontalOffset(200);
//        lpw.setVerticalOffset(600);
//        lpw.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,colorchoice));
//        lpw.setAnchorView(this.findViewById(R.id.DrawingImageView));
//        lpw.setModal(true);
//
//        lpw.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String item = colorchoice[position];
//                if(item.equals("ON"))
//                {
//                    color = Color.GREEN;
//                }
//                if(item.equals("OFF"))
//                {
//                    color = Color.RED;
//                }
//                lpw.dismiss();
//                loadCanvas1();
//            }
//        });
//
//
//        drawingImageView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event){
//                double x,y;
//                x = event.getX();
//                y = event.getY();
//                xx = x;
//                yy = y;
//                if(x < 100 && y < 560)
//                {
//                    lpw.show();
//                }
//                Log.e("x", String.valueOf(x));
//                Log.e("y", String.valueOf(y));
//                return false;
//            }
//        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mqttpublish();
            }
        });
    }

    private void loadCanvas(){

//        Bitmap bitmap = Bitmap.createBitmap((int) getWindowManager()
//                .getDefaultDisplay().getWidth(), (int) getWindowManager()
//                .getDefaultDisplay().getHeight(), Bitmap.Config.ARGB_8888);
        Bitmap bitmap = Bitmap.createBitmap((int) 2000, (int) 2000, Bitmap.Config.ARGB_8888);
        drawingImageView.setImageBitmap(bitmap);

        //creating canvas
        Canvas canvas = new Canvas(bitmap);

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
        paint5.setColor(Color.GREEN);
        //line 1
        paint5.setStrokeCap(Paint.Cap.ROUND);
        paint5.setStrokeWidth(6);
        canvas.drawLine(50, 990, 50, 1050, paint5);
        //Slant line 1
        canvas.drawLine(50, 1050, 200, 1400, paint5);
        //Lower vertical line 1
        canvas.drawLine(200, 1400, 200, 1450, paint5);


        Paint paint7 = new Paint();
        paint7.setColor(Color.GREEN);
        //line 2
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
        paint9.setColor(Color.GREEN);
        //line 5
        paint9.setStrokeCap(Paint.Cap.ROUND);
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
        paint10.setColor(Color.GREEN);
        paint10.setStrokeCap(Paint.Cap.ROUND);
        paint10.setStrokeWidth(6);
        canvas.drawLine(630, 1144, 630, 982, paint10);

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
        paint11.setColor(Color.GREEN);
        paint11.setStrokeCap(Paint.Cap.ROUND);
        paint11.setStrokeWidth(6);
        //vertical part of 12
        canvas.drawLine(580, 975, 580, 920, paint11);
        //Slanting line associated with 12
        canvas.drawLine(580, 920, 350, 630, paint11);
        //vertical connecting 12
        canvas.drawLine(350,630,350,590,paint11);

        //Bus 12
        canvas.drawLine(340,580,420,580,paint4);

        //Line 19
        Paint paint12 = new Paint();
        paint12.setColor(Color.GREEN);
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

        //Bus 13
        canvas.drawLine(530,240,700,240,paint4);

        //Line 13
        Paint paint13 = new Paint();
        paint13.setColor(Color.GREEN);
        paint13.setStrokeCap(Paint.Cap.ROUND);
        paint13.setStrokeWidth(6);
        canvas.drawLine(630, 972, 630, 250, paint13);

        //Line 20
        Paint paint14 = new Paint();
        paint14.setColor(Color.GREEN);
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

        //Bus 14
        canvas.drawLine(960,430,1050,430,paint4);


        //Line 11
        Paint paint15 = new Paint();
        paint15.setColor(Color.GREEN);
        paint15.setStrokeCap(Paint.Cap.ROUND);
        paint15.setStrokeWidth(6);
        //vertical part of 11
        canvas.drawLine(690, 972, 690, 922, paint15);
        //Slanting line associated with 11
        canvas.drawLine(690, 922, 790, 822, paint15);
        //vertical connecting 11
        canvas.drawLine(790,822,790,760,paint15);

        //Bus 11
        canvas.drawLine(770,750,870,750,paint4);

        //Line 18
        Paint paint16 = new Paint();
        paint16.setColor(Color.GREEN);
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

        //Bus 10
        canvas.drawLine(970,830,1050,830,paint4);

        //Line 16
        Paint paint17 = new Paint();
        paint17.setColor(Color.GREEN);
        paint17.setStrokeCap(Paint.Cap.ROUND);
        paint17.setStrokeWidth(6);
        //vertical part of 16
        canvas.drawLine(1025, 840, 1025, 870, paint17);
        //Slanting line associated with 16
        canvas.drawLine(1025, 870, 1100, 930, paint17);
        //vertical connecting 16
        canvas.drawLine(1100,930,1100,960,paint17);

        //Bus 9
        canvas.drawLine(1050,970,1200,970,paint4);

        //Line 17
        Paint paint18 = new Paint();
        paint18.setColor(Color.GREEN);
        paint18.setStrokeCap(Paint.Cap.ROUND);
        paint18.setStrokeWidth(6);
        //vertical part of 17
        canvas.drawLine(1040, 440, 1040, 470, paint18);
        //Slanting line associated with 17
        canvas.drawLine(1040, 470, 1150, 920, paint18);
        //vertical connecting 17
        canvas.drawLine(1150,920,1150,960,paint18);

        //Line 15
        Paint paint19 = new Paint();
        paint19.setColor(Color.GREEN);
        paint19.setStrokeCap(Paint.Cap.ROUND);
        paint19.setStrokeWidth(6);
        //vertical part of 15
        canvas.drawLine(1150, 980, 1150, 1000, paint19);
        //Slanting line associated with 15
        canvas.drawLine(1150, 1000, 1250, 1050, paint19);
        //vertical connecting 15
        canvas.drawLine(1250,1050,1250,1090,paint19);

        //Bus 7
        canvas.drawLine(1235,1100,1325,1100,paint4);

        //Line 8
        Paint paint20 = new Paint();
        paint20.setColor(Color.GREEN);
        paint20.setStrokeCap(Paint.Cap.ROUND);
        paint20.setStrokeWidth(6);
        //vertical part of 15
        canvas.drawLine(1250, 1110, 1250, 1160, paint20);
        //Slanting line associated with 15
        canvas.drawLine(1250, 1160, 1150, 1210, paint20);
        //vertical connecting 15
        canvas.drawLine(1150,1210,1150,1230,paint20);

        //Bus 4
        canvas.drawLine(1025,1240,1200,1240,paint4);

        //Line 9
        Paint paint21 = new Paint();
        paint21.setColor(Color.GREEN);
        paint21.setStrokeCap(Paint.Cap.ROUND);
        paint21.setStrokeWidth(6);
        //vertical connecting 9
        canvas.drawLine(1100,980,1100,1230,paint17);

        //Line 14
        Paint paint22 = new Paint();
        paint22.setColor(Color.GREEN);
        paint22.setStrokeCap(Paint.Cap.ROUND);
        paint22.setStrokeWidth(6);
        //vertical connecting 14
        canvas.drawLine(1300 ,1090,1300,1020,paint22);
        //horizontal connecting 14
        canvas.drawLine(1300 ,1020,1450,1020,paint22);

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
        paint23.setColor(Color.GREEN);
        paint23.setStrokeCap(Paint.Cap.ROUND);
        paint23.setStrokeWidth(6);
        //vertical part of 7
        canvas.drawLine(660, 1160, 660, 1280, paint23);
        //horizontal line associated with 7
        canvas.drawLine(660, 1280, 1050, 1280, paint23);
        //vertical connecting 7
        canvas.drawLine(1050,1280,1050,1250,paint23);

        //Line 4
        Paint paint24 = new Paint();
        paint24.setColor(Color.GREEN);
        paint24.setStrokeCap(Paint.Cap.ROUND);
        paint24.setStrokeWidth(6);
        //vertical part of 4
        canvas.drawLine(280, 1445, 280, 1410, paint24);
        //horizontal line associated with 7
        canvas.drawLine(280, 1410, 1090, 1320, paint24);
        //vertical connecting 4
        canvas.drawLine(1090,1320,1090,1250,paint24);

        //Line 6
        Paint paint25 = new Paint();
        paint25.setColor(Color.GREEN);
        paint25.setStrokeCap(Paint.Cap.ROUND);
        paint25.setStrokeWidth(6);
        //vertical part of 6
        canvas.drawLine(1120, 1250, 1120, 1520, paint25);

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
        paint26.setColor(Color.GREEN);
        paint26.setStrokeCap(Paint.Cap.ROUND);
        paint26.setStrokeWidth(6);
        //vertical part of 3
        canvas.drawLine(280, 1460, 280, 1480, paint26);
        //horizontal line associated with 3
        canvas.drawLine(280, 1480, 1060, 1480, paint26);
        //vertical connecting 3
        canvas.drawLine(1060,1480,1060,1510,paint26);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DisplayFragment(), "Display");
        adapter.addFragment(new StatusFragment(), "Status");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void mqttpublish(){
//        String topic = "foo/bar";
        String payload = "the payload";
        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            message.setRetained(true);
            client.publish(topic, message);
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }

    private void subscribetoTopic(){
        try {
            client.subscribe(topic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    //Client subscribed
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // The subscription could not be performed, maybe the user was not
                    // authorized to subscribe on the specified topic e.g. using wildcards
                }
            });

            client.subscribe(topic, 0, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    // message Arrived!
                    System.out.println("Message: " + topic + " : " + new String(message.getPayload()));
                    Message = new String(message.getPayload());
                }
            });

        } catch (MqttException e){
            System.err.println("Exception whilst subscribing");
            e.printStackTrace();
        }
    }

    private void mqttunsubscribe(){
        try {
            IMqttToken unsubToken = client.unsubscribe(topic);
            unsubToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // The subscription could successfully be removed from the client
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    // some error occurred, this is very unlikely as even if the client
                    // did not had a subscription to the topic the unsubscribe action
                    // will be successfully
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void mqttdisconnet(){
        try {
            IMqttToken disconToken = client.disconnect();
            disconToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // we are now successfully disconnected
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    // something went wrong, but probably we are disconnected anyway
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private  boolean checkAndRequestPermissions() {
        int lock = ContextCompat.checkSelfPermission(this, Manifest.permission.WAKE_LOCK);
        int internet = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        int net_state = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);
        int r_phone_st = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (lock != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WAKE_LOCK);
        }
        if (internet != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.INTERNET);
        }
        if (net_state != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_NETWORK_STATE);
        }
        if (r_phone_st != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    private class BackgroundFunctions extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground (Void...params) {
            checkAndRequestPermissions();
            return null;
        }
    }

}
