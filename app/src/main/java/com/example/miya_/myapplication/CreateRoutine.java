package com.example.miya_.myapplication;

/**
 * Created by chuluo on 2/5/17.
 */

import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.v4.app.DialogFragment;

import java.util.ArrayList;
import java.util.Objects;
import android.app.Fragment;
import android.app.Dialog;
import java.util.Calendar;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.CheckBox;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.miya_.myapplication.providers.Routine_Provider.Routine_Data;
import com.example.miya_.myapplication.providers.Routine_Exercise_Provider.Routine_Exercise_Data;
import com.example.miya_.myapplication.providers.Exercise_Provider.Exercise_Data;
import com.example.miya_.myapplication.providers.Program_Provider.Program_Data;
import com.example.miya_.myapplication.providers.Program_Routine_Provider.Program_Routine_Data;
import com.example.miya_.myapplication.providers.Exercise_Provider.Exercise_Data;
//Program_Provider

public class CreateRoutine extends AppCompatActivity{


    //listViewNewRoutine1
    public TextView timeTextView ;
    static int hourSelected=0;
    static int mintueSelected=0;

    //buttonNewRoutine1   2and 3

    static String programName;

    ArrayList<ExerciseItem> products = new ArrayList<ExerciseItem>();
    ListAdapterExercises boxAdapter;

    public EditText EditTextRoutineName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newroutine);

        fillData();
        boxAdapter = new ListAdapterExercises(this, products);

        ListView lvMain = (ListView) findViewById(R.id.listViewNewRoutine1);
        lvMain.setItemsCanFocus(true);
        lvMain.setAdapter(boxAdapter);

        //boxAdapter   notifyDataSetChanged();

        programName = getIntent().getStringExtra("programName");

        timeTextView = (TextView) findViewById(R.id.textNewRoutine3);

        EditTextRoutineName = (EditText) findViewById(R.id.editTextNewRoutine1);

        EditTextRoutineName.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(EditTextRoutineName.getText().toString().equals("Routine Name"))
                        {

                            EditTextRoutineName.setText("");
                        }

                    }
                }
        );
        timeTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                new TimePickerDialog(CreateRoutine.this, MyTimePickerDialog,
                        dateAndTime.get(Calendar.HOUR_OF_DAY),
                        dateAndTime.get(Calendar.MINUTE),
                        true
                ).show();
            }
        });
        Button timeButton = (Button) findViewById(R.id.buttonNewRoutine1);

        timeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                new TimePickerDialog(CreateRoutine.this, MyTimePickerDialog,
                        dateAndTime.get(Calendar.HOUR_OF_DAY),
                        dateAndTime.get(Calendar.MINUTE),
                        true
                        ).show();
            }
        });

        Button submit = (Button) findViewById(R.id.buttonNewRoutine3);

        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                String routineName = EditTextRoutineName.getText().toString();

                if(routineName.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Input A Name!", Toast.LENGTH_LONG).show();
                    return;
                }

                int countChecked=0;

                //get checked exercises, set , repts
                for (ExerciseItem p : boxAdapter.getBox()) {
                    if (p.box){
                        countChecked++;


                        if(p.getSet()<1)
                        {
                            Toast.makeText(getApplicationContext(), "Set Number Cannot Be 0!", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if(p.getRept()<1)
                        {
                            Toast.makeText(getApplicationContext(), "Rept Number Cannot Be 0!", Toast.LENGTH_LONG).show();
                            return;
                        }

                    }
                }

                if(!timePicked)
                {
                    Toast.makeText(getApplicationContext(), "Pick A Time!", Toast.LENGTH_LONG).show();
                    return;
                }

                if(countChecked==0)
                {
                    //show alert
                    Toast.makeText(getApplicationContext(), "Select At Least 1 Exercise!", Toast.LENGTH_LONG).show();
                    return;
                }
                //put in routine db
                {
                    ContentValues rowData = new ContentValues();
                    rowData.put(Routine_Data.NAME, routineName);
                    rowData.put(Routine_Data.HOUR, hourSelected);
                    rowData.put(Routine_Data.MINUTE, mintueSelected);
                    rowData.put(Routine_Data.SECOND, 0);
                    getApplicationContext().getContentResolver().insert(Routine_Data.CONTENT_URI, rowData);
                }
                //get programID
                int program_ID=0;
                Cursor cursorProgram = getApplicationContext().getContentResolver().query(Program_Data.CONTENT_URI, null, Program_Data.NAME+"='"+programName+"'",
                        null, Program_Data._ID + " DESC LIMIT 1");
                if (cursorProgram != null && cursorProgram.moveToFirst()) {
                    program_ID = cursorProgram.getInt(cursorProgram.getColumnIndex(Program_Data._ID));
                }
                if (cursorProgram != null && !cursorProgram.isClosed())
                    cursorProgram.close();

                //get routine ID
                int routine_ID=0;
                Cursor cursorRoutine = getApplicationContext().getContentResolver().query(Routine_Data.CONTENT_URI, null, Routine_Data.NAME+"='"+routineName+"'",
                        null, Routine_Data._ID + " DESC LIMIT 1");
                if (cursorRoutine != null && cursorRoutine.moveToFirst()) {
                    routine_ID = cursorRoutine.getInt(cursorRoutine.getColumnIndex(Routine_Data._ID));
                }
                if (cursorRoutine != null && !cursorRoutine.isClosed())
                    cursorRoutine.close();
                //using programID , put in program routine db
                {
                    ContentValues rowData = new ContentValues();
                    rowData.put(Program_Routine_Data.PROGRAM_ID, program_ID);
                    rowData.put(Program_Routine_Data.ROUTINE_ID, routine_ID);
                    rowData.put(Program_Routine_Data.ROUTINE_NAME, routineName);
                    getApplicationContext().getContentResolver().insert(Program_Routine_Data.CONTENT_URI, rowData);
                }
                //add routine-exercises
                for (ExerciseItem p : boxAdapter.getBox()) {
                    if (p.box) {

                        //Exercise one by one
                        {
                            ContentValues rowData = new ContentValues();
                            rowData.put(Routine_Exercise_Data.ROUTINE_ID, routine_ID);
                            rowData.put(Routine_Exercise_Data.EXERCISE_ID, p.getID());
                            rowData.put(Routine_Exercise_Data.EXERCISE_NAME, p.getName());


                            Log.d("CJJAWL","p.getID() = "+p.getID());

                            Log.d("CJJAWL","p.getName() = "+p.getName());


                            rowData.put(Routine_Exercise_Data.SET, p.getSet());
                            rowData.put(Routine_Exercise_Data.REPT, p.getRept());
                            getApplicationContext().getContentResolver().insert(Routine_Exercise_Data.CONTENT_URI, rowData);
                        }
                    }
                }

                //return a value to refresh view program
                Intent output = new Intent();
                output.putExtra("result","added");
                setResult(RESULT_OK,output);
                finish();
            }
        });




    }

    Calendar dateAndTime = Calendar.getInstance();

    public void updateLabel()
    {
        timeTextView.setText("" + hourSelected+":"+mintueSelected);
    }

    boolean timePicked=false;

    TimePickerDialog.OnTimeSetListener MyTimePickerDialog = new TimePickerDialog.OnTimeSetListener(){
        public void onTimeSet(TimePicker view, int hourOfDay, int minute){

            //hour 0-23
            // //minute  0-59
            //Display the user changed time on TextView

            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            hourSelected = hourOfDay;
            mintueSelected = minute;
            timePicked=true;
            updateLabel();
        }
    };

    //show all exercises

    void fillData() {
        Cursor cursor = getContentResolver().query(Exercise_Data.CONTENT_URI, null,
                null, null, Exercise_Data._ID + " ASC");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                ExerciseItem replay_item = new ExerciseItem();
                replay_item.setID(cursor.getInt(cursor.getColumnIndex(Exercise_Data._ID)));

                Log.d("CJJAWL","setID = "+cursor.getInt(cursor.getColumnIndex(Exercise_Data._ID)));

                replay_item.setName(cursor.getString(cursor.getColumnIndex(Exercise_Data.NAME)));

                Log.d("CJJAWL","setName = "+cursor.getString(cursor.getColumnIndex(Exercise_Data.NAME)));

                replay_item.setSet(1);
                replay_item.setRept(10);
                replay_item.setBox(false);
                products.add(replay_item);
            }
        }
        if (cursor != null && !cursor.isClosed())
        {
            cursor.close();
        }
    }
}
