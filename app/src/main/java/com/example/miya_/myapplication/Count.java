package com.example.miya_.myapplication;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miya_.myapplication.providers.Routine_Provider.Routine_Data;
import com.example.miya_.myapplication.providers.Routine_Exercise_Provider.Routine_Exercise_Data;
import com.example.miya_.myapplication.providers.Complete_Provider.Complete_Data;

import java.util.ArrayList;
import java.util.Calendar;
import android.os.Vibrator;

/**
 * Name: Count
 * Description：When user click start a routine, start counting.
 * Main method:
 *      onCreate:
 *          initilize training variables, namely reps, sets, weights
 *          check completes
 *          count reps -> update UI
 *
 *      BroadcastReceiver:
 *          check exercise validation
 *          update db
 *      onDestroy:
 *          when destory, stop counting
 *          when user presses back, or the app is killed by OS when it is in the background AND other apps want memory
 *          so it is OK for users to use other apps without huge memory (e.g., SNS)  when counting
 * */

public class Count extends AppCompatActivity implements NumberPicker.OnValueChangeListener{

    String programName;
    String routineName;

    int weightNumber = 0;

    ArrayList<ExerciseItem> exerciseItems = new ArrayList<>();
    int routineID = 0;

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

    }

    TextView textViewRept;
    TextView textViewSet;
    TextView textViewTimer;
    TextView textViewExercise;

    boolean startedCounting=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counting);

        //count text
        textViewRept= (TextView) findViewById(R.id.textCount5);
        textViewSet = (TextView) findViewById(R.id.textCount7);
        textViewTimer = (TextView) findViewById(R.id.textCount9);
        textViewExercise = (TextView) findViewById(R.id.textCount3);

        //weight setting
        //weight text
        final TextView textView3= (TextView) findViewById(R.id.textCount10);

        Button weight = (Button) findViewById(R.id.buttonCount2);

        weight.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                final Dialog d = new Dialog(Count.this);
                d.setTitle("Weight");
                d.setContentView(R.layout.dialog_wegiht);
                Button b1 = (Button) d.findViewById(R.id.buttonDialogWeight1);
                Button b2 = (Button) d.findViewById(R.id.buttonDialogWeight2);
                final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
                np.setMaxValue(300); // max value 300
                np.setMinValue(0);   // min value 0
                np.setWrapSelectorWheel(false);
                np.setOnValueChangedListener(Count.this);
                b1.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        weightNumber= np.getValue();
                        textView3.setText(String.valueOf(weightNumber)+"kg"); //set the value to textview
                        d.dismiss();

                    }
                });
                b2.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        d.dismiss(); // dismiss the dialog
                    }
                });
                d.show();
            }
        });

        //receive counting results
        final IntentFilter command_filter = new IntentFilter();
        command_filter.addAction("ACTION_BUILD_COUNT_RESULT");
        command_filter.addAction("ACTION_BUILD_COUNT_TIMER");


        //start count
        final Button submit = (Button) findViewById(R.id.buttonCount3);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //in a button
                if(!startedCounting) {
                    startedCounting=true;
                    //start counting here
                    Intent context_unlock = new Intent();
                    context_unlock.setAction("ACTION_BUILD_START_COUNT");
                    sendBroadcast(context_unlock);
                    submit.setText("PAUSE");
                    registerReceiver(commandListener3, command_filter);
                }
                else
                {
                    unregisterReceiver( commandListener3);
                    startedCounting=false;
                    Intent context_unlock = new Intent();
                    context_unlock.setAction("ACTION_BUILD_STOP_COUNT");
                    //sendBroadcast(context_unlock);
                    submit.setText("START");
                }

            }
        });

        //get what is being done
        programName = getIntent().getStringExtra("programName");
        routineName = getIntent().getStringExtra("routineName");

        //display program and routine name

        TextView textView1= (TextView) findViewById(R.id.textCount1);
        TextView textView2= (TextView) findViewById(R.id.textCount2);
        textView1.setText(programName);
        textView2.setText(routineName);

        //get routineID

        Cursor cursorProgram = getApplicationContext().getContentResolver().query(Routine_Data.CONTENT_URI, null, Routine_Data.NAME+"='"+ routineName +"'",
                null, Routine_Data._ID + " DESC LIMIT 1");
        if (cursorProgram != null && cursorProgram.moveToFirst()) {
            routineID = cursorProgram.getInt(cursorProgram.getColumnIndex(Routine_Data._ID));

        }
        if (cursorProgram != null && !cursorProgram.isClosed())
            cursorProgram.close();

        Log.d("CJJAWL","routineID count ="+routineID);

        //read data base check completes
        //get today
        Calendar c = Calendar.getInstance();

        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH);
        int currentDay = c.get(Calendar.DAY_OF_MONTH);

        Cursor cursorComplete = getApplicationContext().getContentResolver().query(Complete_Data.CONTENT_URI, null, Complete_Data.ROUTINE_ID+"="+routineID
                +" AND "+ Complete_Data.YEAR+"="+currentYear +" AND "+ Complete_Data.MONTH +"="+currentMonth +" AND "+ Complete_Data.DAY +"="+currentDay,
                null, Complete_Data._ID + " ASC");
        if (cursorComplete != null && !cursorComplete.isClosed()) {
            if (cursorComplete.getCount() > 0)
            {
                Toast.makeText(getApplicationContext(), "Already done today!", Toast.LENGTH_LONG).show();
                finish();
            }
            cursorComplete.close();
        }

        //get the exercise need to be done at this moment
        //search all exercises   related to a routine using routine ID
        Cursor cursorRountine = getApplicationContext().getContentResolver().query(Routine_Exercise_Data.CONTENT_URI, null, Routine_Exercise_Data.ROUTINE_ID+"="+routineID,
                null, Routine_Exercise_Data._ID + " ASC");
        if (cursorRountine != null && cursorRountine.moveToFirst()) {
            while (!cursorRountine.isAfterLast()) {
                ExerciseItem currentItem = new ExerciseItem();
                currentItem.setName(cursorRountine.getString(cursorRountine.getColumnIndex(Routine_Exercise_Data.EXERCISE_NAME)));
                currentItem.setSet(cursorRountine.getInt(cursorRountine.getColumnIndex(Routine_Exercise_Data.SET)));
                currentItem.setRept(cursorRountine.getInt(cursorRountine.getColumnIndex(Routine_Exercise_Data.REPT)));
                exerciseItems.add(currentItem);

                //your code to implement
                cursorRountine.moveToNext();
            }
        }
        if (cursorRountine != null && !cursorRountine.isClosed())
            cursorRountine.close();


    }

    String currentExerciseDetected="";
    int currentSetDone=0;
    int currentReptDone=0;
    long timestamp=0;
    long secondTimer=0;

    private CommandListener3 commandListener3 = new CommandListener3();

    public class CommandListener3 extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {


            if (intent.getAction().equals("ACTION_BUILD_COUNT_TIMER"))
            {
                timestamp=intent.getLongExtra("timestamp",0L);
                if(secondTimer<timestamp/1000)
                {
                    secondTimer=timestamp/1000;

                    textViewTimer.setText( secondTimer+"s");
                }
            }

            if (intent.getAction().equals("ACTION_BUILD_COUNT_RESULT"))  {
                //Log.d("BUILD", "Start count!");
                Log.d("exerciseItems.size()",""+exerciseItems.size());

                if(exerciseItems.size()>0)
                {
                    ExerciseItem currentItem = exerciseItems.get(0);
                    //get Exercise
                    currentExerciseDetected = intent.getStringExtra("exercise");

                    Log.d("CJJAWL","aaaa="+currentExerciseDetected);
                    textViewExercise.setText(currentItem.getName());

                    //get Count
                    //if correct for the current one
                    //set count as valid

                    if(currentExerciseDetected.equals(currentItem.getName()))
                    {
                        if(currentReptDone==currentItem.getRept())
                        {
                            currentSetDone=currentSetDone+1;
                            currentReptDone=0;
                        }
                        else
                        {
                            currentReptDone=currentReptDone+1;
                        }

                        textViewRept.setText(""+currentReptDone);
                        textViewSet.setText(""+currentSetDone);

                        if(currentSetDone==currentItem.getSet())
                        {
                            Vibrator v = (Vibrator)  context.getSystemService(Context.VIBRATOR_SERVICE);
                            // Vibrate for  1 second
                            v.vibrate(100);
                            currentReptDone=0;
                            currentSetDone = 0;
                            Log.d("current_rep",""+currentReptDone);
                            Log.d("currentItem.getName()",currentItem.getName().toString());
                            Log.d("currentSetDone",""+currentSetDone);


                            //if reach the set number and rept
                            exerciseItems.remove(0);
                            //update UI and DB
                            if(exerciseItems.size()==0)
                            {
                                Toast.makeText(getApplicationContext(), "Already done today!", Toast.LENGTH_LONG).show();
                                Calendar c = Calendar.getInstance();


                                ContentValues rowData = new ContentValues();

                                rowData.put(Complete_Data.ROUTINE_ID, routineID);
                                rowData.put(Complete_Data.HOUR, c.get(Calendar.HOUR_OF_DAY));
                                rowData.put(Complete_Data.MINUTE, c.get(Calendar.MINUTE));
                                rowData.put(Complete_Data.SECOND, c.get(Calendar.SECOND));
                                rowData.put(Complete_Data.YEAR, c.get(Calendar.YEAR));
                                rowData.put(Complete_Data.MONTH, c.get(Calendar.MONTH));
                                rowData.put(Complete_Data.DAY, c.get(Calendar.DAY_OF_MONTH));
                                //get this from UI
                                rowData.put(Complete_Data.WEIGHT, weightNumber);//weight kg
                                getApplicationContext().getContentResolver().insert(Complete_Data.CONTENT_URI, rowData);
                            }
                        }
                    }
                }
                else
                {
                    Intent context_unlock = new Intent();
                    context_unlock.setAction("ACTION_BUILD_STOP_COUNT");
                    sendBroadcast(context_unlock);
                    finish();
                }
            }
        }
    }
/*
* when destory, stop counting
* when user presses back, or the app is killed by OS when it is in the background AND other apps want memory
* so it is OK for users to use other apps without huge memory (e.g., SNS)  when counting
* */

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Log.d("CJJAWL","STOP COUNT");



        Intent context_unlock = new Intent();
        context_unlock.setAction("ACTION_BUILD_STOP_COUNT");
        sendBroadcast(context_unlock);

        if(startedCounting) {
            unregisterReceiver(commandListener3);
        }
    }

}
