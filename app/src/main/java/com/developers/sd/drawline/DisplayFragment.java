package com.developers.sd.drawline;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
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

        // final String[] colorchoice = {"Red","Blue","Green"};
        final String[] colorchoice = {"ON","OFF"};
        final ListPopupWindow lpw = new ListPopupWindow(this.getActivity());
        lpw.setWidth(800);
        lpw.setHeight(400);
        lpw.setHorizontalOffset(200);
        lpw.setVerticalOffset(600);
        lpw.setAdapter(new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_list_item_1,colorchoice));
        lpw.setAnchorView(view.findViewById(R.id.DrawingImageView));
        lpw.setModal(true);

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


        return view;
    }

    private void loadCanvas(){

        Bitmap bitmap = Bitmap.createBitmap((int) getActivity().getWindowManager()
                .getDefaultDisplay().getWidth(), (int) getActivity().getWindowManager()
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

        Bitmap bitmap = Bitmap.createBitmap((int) getActivity().getWindowManager()
                .getDefaultDisplay().getWidth(), (int) getActivity().getWindowManager()
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

}
