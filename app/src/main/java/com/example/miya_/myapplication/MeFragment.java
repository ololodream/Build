package com.example.miya_.myapplication;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.RelativeLayout;

import static android.app.Activity.RESULT_OK;
/**
 * MeFragment
 * Description:
 *          Set default sensor placement by dragging a sensor on the body
 */

public class MeFragment extends Fragment {

    private View mContent;

    private int _xDelta;
    private int _yDelta;


    public static Fragment newInstance( ) {
        Fragment frag = new MeFragment();
        Bundle args = new Bundle();

        frag.setArguments(args);
        return frag;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_me, container, false);

        return view;
    }


    @Override

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        // retrieve text and color from bundle or savedInstanceState
        if (savedInstanceState == null) {
            Bundle args = getArguments();

        } else {

        }


        // initialize views
        mContent = view.findViewById(R.id.fragment_me);


        ImageView sensor1 = (ImageView) view.findViewById(R.id.sensor1);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(200, 200);
        layoutParams.leftMargin = 625;
        layoutParams.topMargin = 1900;
        layoutParams.rightMargin = -250;
        layoutParams.bottomMargin = -250;

        Log.d("CMJ  ","init_left:"+layoutParams.leftMargin );
        Log.d("CMJ  ","init_top:"+layoutParams.topMargin );
        sensor1.setLayoutParams(layoutParams);

        sensor1.setOnTouchListener(new OnTouchListener() {


            public boolean onTouch(View v, MotionEvent event) {
                final int X = (int)event.getRawX();
                final int Y = (int)event.getRawY();
                Log.d("CMJ  ","X:"+X);
                Log.d("CMJ  ","Y:"+Y);
                switch(event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_DOWN:
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                        _xDelta = X - lParams.leftMargin;
                        _yDelta = Y - lParams.topMargin;
                        break;
                    case MotionEvent.ACTION_UP:
                        break;

                    case MotionEvent.ACTION_POINTER_DOWN:
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v
                                .getLayoutParams();
                        layoutParams.leftMargin = X - _xDelta;
                        layoutParams.topMargin = Y - _yDelta;
                        layoutParams.rightMargin = -250;
                        layoutParams.bottomMargin = -250;
                        v.setLayoutParams(layoutParams);
                        Log.d("CMJ  ","left:"+layoutParams.leftMargin );
                        Log.d("CMJ  ","top:"+layoutParams.topMargin );

                        break;
                    case
                    MotionEvent.ACTION_HOVER_EXIT:
                        Log.d("cmj", "Sensor ACTION_DRAG_EXITED");
                        break;

                }
                v.invalidate();
                return true;
            }
        });


    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }




}