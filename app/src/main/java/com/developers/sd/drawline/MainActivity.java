package com.developers.sd.drawline;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

    ImageView drawingImageView;
    Paint paint2 = new Paint();
    int color = Color.BLACK;
    double xx,yy;

    MqttAndroidClient client;
    String clientId;
    String topic = "topic";
    String Message;

    private final String TAG = "TAG";
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawingImageView = (ImageView) findViewById(R.id.DrawingImageView);
        loadCanvas();
        new BackgroundFunctions().execute();

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


//        final String[] colorchoice = {"Red","Blue","Green"};
        final String[] colorchoice = {"ON","OFF"};
        final ListPopupWindow lpw = new ListPopupWindow(this);
        lpw.setWidth(800);
        lpw.setHeight(400);
        lpw.setHorizontalOffset(200);
        lpw.setVerticalOffset(600);
        lpw.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,colorchoice));
        lpw.setAnchorView(this.findViewById(R.id.DrawingImageView));
        lpw.setModal(true);

        lpw.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = colorchoice[position];
                if(item.equals("ON"))
                {
                    color = Color.GREEN;
                }
                if(item.equals("OFF"))
                {
                    color = Color.RED;
                }
                lpw.dismiss();
                loadCanvas1();
            }
        });


        drawingImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event){
                double x,y;
                x = event.getX();
                y = event.getY();
                xx = x;
                yy = y;
                if(x < 100 && y < 560)
                {
                    lpw.show();
                }
                Log.e("x", String.valueOf(x));
                Log.e("y", String.valueOf(y));
                return false;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mqttpublish();
            }
        });
    }

    private void loadCanvas(){

        Bitmap bitmap = Bitmap.createBitmap((int) getWindowManager()
                .getDefaultDisplay().getWidth(), (int) getWindowManager()
                .getDefaultDisplay().getHeight(), Bitmap.Config.ARGB_8888);
        drawingImageView.setImageBitmap(bitmap);

        //creating canvas
        Canvas canvas = new Canvas(bitmap);

        Paint paint1 = new Paint();
        paint1.setColor(Color.GREEN);
        //G circle
        paint1.setStrokeWidth(6);
        paint1.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(80, 600, 70, paint1);

        paint2.setColor(Color.GREEN);
        //G text
        paint2.setTextSize(60);
        canvas.drawText("G", 58, 620, paint2);

        Paint paint3 = new Paint();
        paint3.setColor(Color.GREEN);
        //Vertical Line
        paint3.setStrokeCap(Paint.Cap.ROUND);
        paint3.setStrokeWidth(6);
        canvas.drawLine(80, 675, 80, 750, paint3);

        Paint paint4 = new Paint();
        paint4.setColor(Color.BLACK);
        //Horizontal Line
        paint4.setStrokeCap(Paint.Cap.ROUND);
        paint4.setStrokeWidth(15);
        canvas.drawLine(00, 750, 160, 750, paint4);

        Paint paint5 = new Paint();
        paint5.setColor(Color.GREEN);
        //Vertical line leftmost 1
        paint5.setStrokeCap(Paint.Cap.ROUND);
        paint5.setStrokeWidth(6);
        canvas.drawLine(20, 760, 20, 1050, paint5);

        Paint paint6 = new Paint();
        paint6.setColor(Color.GREEN);
        //VLL2
        paint6.setStrokeCap(Paint.Cap.ROUND);
        paint6.setStrokeWidth(6);
        canvas.drawLine(80, 760, 80, 1000, paint6);

        Paint paint7 = new Paint();
        paint7.setColor(Color.GREEN);
        //VLL3
        paint7.setStrokeCap(Paint.Cap.ROUND);
        paint7.setStrokeWidth(6);
        canvas.drawLine(140, 760, 140, 900, paint7);


        //Slant leftmost line 1
        canvas.drawLine(20, 1050, 300, 1600, paint5);

        //SLL2
        canvas.drawLine(80, 1000, 380, 1600, paint6);

        //SLL3
        canvas.drawLine(140, 900, 800, 1200, paint7);

        //Lower vertical leftmost 1
        canvas.drawLine(300, 1600, 300, 1680, paint5);
    }

    private void loadCanvas1(){

        Bitmap bitmap = Bitmap.createBitmap((int) getWindowManager()
                .getDefaultDisplay().getWidth(), (int) getWindowManager()
                .getDefaultDisplay().getHeight(), Bitmap.Config.ARGB_8888);
        drawingImageView.setImageBitmap(bitmap);

        //creating canvas
        Canvas canvas = new Canvas(bitmap);

        Paint paint1 = new Paint();
        paint1.setColor(color);
        //G circle
        paint1.setStrokeWidth(6);
        paint1.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(80, 600, 70, paint1);

        paint2.setColor(Color.GREEN);
        //G text
        paint2.setTextSize(60);
        canvas.drawText("G", 58, 620, paint2);

        Paint paint3 = new Paint();
        paint3.setColor(Color.GREEN);
        //Vertical Line
        paint3.setStrokeCap(Paint.Cap.ROUND);
        paint3.setStrokeWidth(6);
        canvas.drawLine(80, 675, 80, 750, paint3);

        Paint paint4 = new Paint();
        paint4.setColor(Color.BLACK);
        //Horizontal Line
        paint4.setStrokeCap(Paint.Cap.ROUND);
        paint4.setStrokeWidth(15);
        canvas.drawLine(00, 750, 160, 750, paint4);

        Paint paint5 = new Paint();
        paint5.setColor(Color.GREEN);
        //Vertical line leftmost 1
        paint5.setStrokeCap(Paint.Cap.ROUND);
        paint5.setStrokeWidth(6);
        canvas.drawLine(20, 760, 20, 1050, paint5);

        Paint paint6 = new Paint();
        paint6.setColor(Color.GREEN);
        //VLL2
        paint6.setStrokeCap(Paint.Cap.ROUND);
        paint6.setStrokeWidth(6);
        canvas.drawLine(80, 760, 80, 1000, paint6);

        Paint paint7 = new Paint();
        paint7.setColor(Color.GREEN);
        //VLL3
        paint7.setStrokeCap(Paint.Cap.ROUND);
        paint7.setStrokeWidth(6);
        canvas.drawLine(140, 760, 140, 900, paint7);


        //Slant leftmost line 1
        canvas.drawLine(20, 1050, 300, 1600, paint5);

        //SLL2
        canvas.drawLine(80, 1000, 380, 1600, paint6);

        //SLL3
        canvas.drawLine(140, 900, 800, 1200, paint7);

        //Lower vertical leftmost 1
        canvas.drawLine(300, 1600, 300, 1680, paint5);
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
