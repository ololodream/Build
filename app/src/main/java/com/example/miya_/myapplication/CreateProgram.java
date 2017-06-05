package com.example.miya_.myapplication;


import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.v4.app.DialogFragment;
import java.util.Objects;
import android.app.DatePickerDialog;
import android.app.Dialog;
import java.util.Calendar;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.CheckBox;
import com.example.miya_.myapplication.providers.Program_Provider.Program_Data;

/**
 * Name: CreateProgram
 * Description:
 *      Maintain create program page
 * Main Method:
 *  onCreate:
 *      Initialize page of 'create program'
 *      Set listener for users' input
 *          e.g. input program name/start date/end date/ days in week
 *      Check validation
 *      If input infomations is valid, then update those data to database
 */

public class CreateProgram extends AppCompatActivity{

    //EditText
    EditText programName;


    TextView SelectedDateView1;

    TextView SelectedDateView2;

    static int startYear; //2017
    static int startMonth; //0-11
    static int startDay; //1-31

    static int endYear; //2017
    static int endMonth; //0-11
    static int endDay; //1-31

    //checkbox
    CheckBox Mon;
    CheckBox Tue;
    CheckBox Wed;
    CheckBox Thu;
    CheckBox Fri;
    CheckBox Sat;
    CheckBox Sun;

    //dialog start

    Calendar MyCalendar1 = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener MyOnDateSetListener1 = new DatePickerDialog.OnDateSetListener(){
        public void onDateSet(DatePicker view, int year, int month, int day) {
            startYear = year;
            startMonth = month;
            startDay = day;

            SelectedDateView1.setText("" + day  + "/" + (month + 1) + "/" + year);
        }
    };

    //dialog end
    Calendar MyCalendar2 = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener MyOnDateSetListener2 = new DatePickerDialog.OnDateSetListener(){
        public void onDateSet(DatePicker view, int year, int month, int day) {
            endYear = year;
            endMonth = month;
            endDay = day;

            SelectedDateView2.setText("" + day  + "/" + (month + 1) + "/" + year);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newprogram);

        //init everthing
        programName = (EditText) findViewById(R.id.editTextNewProgram1);

        SelectedDateView1 = (TextView) findViewById(R.id.textNewProgram4);

        SelectedDateView2 = (TextView) findViewById(R.id.textNewProgram6);

        Mon = (CheckBox) findViewById(R.id.checkBoxNewProgram1);
        Tue = (CheckBox) findViewById(R.id.checkBoxNewProgram2);
        Wed = (CheckBox) findViewById(R.id.checkBoxNewProgram3);
        Thu = (CheckBox) findViewById(R.id.checkBoxNewProgram4);
        Fri = (CheckBox) findViewById(R.id.checkBoxNewProgram5);
        Sat = (CheckBox) findViewById(R.id.checkBoxNewProgram6);
        Sun = (CheckBox) findViewById(R.id.checkBoxNewProgram7);

        programName.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(programName.getText().toString().equals("Program Name"))
                        {
                            programName.setText("");
                        }
                    }
                }

        );

        //click textview
        SelectedDateView1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new DatePickerDialog(CreateProgram.this, MyOnDateSetListener1,
                                MyCalendar1.get(Calendar.YEAR),
                                MyCalendar1.get(Calendar.MONTH),
                                MyCalendar1.get(Calendar.DAY_OF_MONTH)
                        ).show();

                    }
                }

        );

        SelectedDateView2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new DatePickerDialog(CreateProgram.this, MyOnDateSetListener2,
                                MyCalendar1.get(Calendar.YEAR),
                                MyCalendar1.get(Calendar.MONTH),
                                MyCalendar1.get(Calendar.DAY_OF_MONTH)
                        ).show();

                    }
                }

        );
        //two buttons for picking dates
        Button startDateButton = (Button) findViewById(R.id.buttonNewProgram1);

        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(CreateProgram.this, MyOnDateSetListener1,
                        MyCalendar1.get(Calendar.YEAR),
                        MyCalendar1.get(Calendar.MONTH),
                        MyCalendar1.get(Calendar.DAY_OF_MONTH)
                        ).show();

            }
        });

        Button endDateButton = (Button) findViewById(R.id.buttonNewProgram2);

        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(CreateProgram.this, MyOnDateSetListener2,
                        MyCalendar2.get(Calendar.YEAR),
                        MyCalendar2.get(Calendar.MONTH),
                        MyCalendar2.get(Calendar.DAY_OF_MONTH)
                ).show();

            }
        });

        Button nextButton = (Button) findViewById(R.id.buttonNewProgram3);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i = new Intent(getApplicationContext(), ObtainData.class);

                String programNameString = programName.getText().toString();

//                Log.d("CJJAWL",""+programNameString);
//                Log.d("CJJAWL",""+startYear);
//                Log.d("CJJAWL",""+startMonth);
//                Log.d("CJJAWL",""+startDay);
//                Log.d("CJJAWL",""+endYear);
//                Log.d("CJJAWL",""+endMonth);
//                Log.d("CJJAWL",""+endDay);

                //Log.d("CJJAWL",""+Mon.isChecked());
                //Log.d("CJJAWL",""+Tue.isChecked());
                //checkbox
                int MonChecked=0;
                if(Mon.isChecked())
                {
                    MonChecked=1;
                }
                int TueChecked=0;
                if(Tue.isChecked())
                {
                    TueChecked=1;
                }
                int WedChecked=0;
                if(Wed.isChecked())
                {
                    WedChecked=1;
                }
                int ThuChecked=0;
                if(Thu.isChecked())
                {
                    ThuChecked=1;
                }
                int FriChecked=0;
                if(Fri.isChecked())
                {
                    FriChecked=1;
                }
                int SatChecked=0;
                if(Sat.isChecked())
                {
                    SatChecked=1;
                }
                int SunChecked=0;
                if(Sun.isChecked())
                {
                    SunChecked=1;
                }

                //put data in DB
                ContentValues rowData = new ContentValues();
                rowData.put(Program_Data.NAME, programNameString);
                rowData.put(Program_Data.BEGIN_YEAR, startYear);
                rowData.put(Program_Data.BEGIN_MONTH, startMonth); //month is from 0-11
                rowData.put(Program_Data.BEGIN_DAY, startDay);
                rowData.put(Program_Data.END_YEAR, endYear);
                rowData.put(Program_Data.END_MONTH, endMonth);
                rowData.put(Program_Data.END_DAY, endDay);
                rowData.put(Program_Data.PROGRAM_DAY_MONDAY, MonChecked);
                rowData.put(Program_Data.PROGRAM_DAY_TUESDAY, TueChecked);
                rowData.put(Program_Data.PROGRAM_DAY_WEDNESDAY, WedChecked);
                rowData.put(Program_Data.PROGRAM_DAY_THURSDAY, ThuChecked);
                rowData.put(Program_Data.PROGRAM_DAY_FRIDAY, FriChecked);
                rowData.put(Program_Data.PROGRAM_DAY_SATURDAY, SatChecked);
                rowData.put(Program_Data.PROGRAM_DAY_SUNDAY, SunChecked);
                getApplicationContext().getContentResolver().insert(Program_Data.CONTENT_URI, rowData);

                Intent output = new Intent();
                output.putExtra("result","added");
                setResult(RESULT_OK,output);
                finish();


            }
        });
    }


}
