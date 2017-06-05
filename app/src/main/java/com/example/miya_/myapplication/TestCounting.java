package com.example.miya_.myapplication;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TestCounting extends AppCompatActivity{

    TextView Exercise;
    TextView Count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testcounting);

        Exercise = (TextView) findViewById(R.id.textView);
        Count = (TextView) findViewById(R.id.textView2);


        IntentFilter command_filter = new IntentFilter();
        command_filter.addAction("ACTION_BUILD_COUNT_RESULT");


        registerReceiver(commandListener, command_filter);

        Button schedule = (Button)  findViewById(R.id.button2);

        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent context_unlock = new Intent();
                context_unlock.setAction("ACTION_BUILD_START_COUNT");

                sendBroadcast(context_unlock);
            }
        });

        Button schedule2 = (Button)  findViewById(R.id.button3);

        schedule2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent context_unlock = new Intent();
                context_unlock.setAction("ACTION_BUILD_STOP_COUNT");

                sendBroadcast(context_unlock);

            }
        });
    }

    private CommandListener2 commandListener = new CommandListener2();

    public class CommandListener2 extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("ACTION_BUILD_COUNT_RESULT"))  {
                Log.d("BUILD", "Start count!");

                //Exercise

                //Count

            }



        }
    }


}
