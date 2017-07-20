package com.developers.sd.drawline;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class DisplayFragment extends Fragment {

    ImageView drawingImageView;
    Paint paint2 = new Paint();
    int color = Color.BLACK;
    double xx,yy;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_display, container, false);
        drawingImageView = (ImageView) view.findViewById(R.id.DrawingImageView);
        loadCanvas();
        return view;
    }

    private void loadCanvas(){

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

    private class BackgroundFunctions extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground (Void...params) {
            loadCanvas();
            return null;
        }
    }

}
