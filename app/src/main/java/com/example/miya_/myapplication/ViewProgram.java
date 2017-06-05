package com.example.miya_.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.database.Cursor;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by chuluo on 1/5/17.
 */
import com.example.miya_.myapplication.providers.Program_Provider.Program_Data;
import com.example.miya_.myapplication.providers.Program_Routine_Provider.Program_Routine_Data;
import com.example.miya_.myapplication.providers.Routine_Provider.Routine_Data;
/**
 * ViewProgram
 * Description:
 *         Display view program page
 *         list routines of a program
 *
 */
public class ViewProgram extends AppCompatActivity {

    String programName;
    TextView textView1;
    //routine list
    SimpleCursorAdapter dataAdapterData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewprogram);

        programName= getIntent().getStringExtra("programName");
        textView1= (TextView) findViewById(R.id.textViewProgram1);
        textView1.setText(""+ programName);

        Button schedule = (Button) findViewById(R.id.buttonViewProgram1);

        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //create routine
                Intent i = new Intent(getApplicationContext(), CreateRoutine.class);

                i.putExtra("programName",programName);
                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(i,1);



                //run routine
                //pass routine, program name to count activity
            }
        });
        displayListViewDataset();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode == RESULT_OK && data !=null) {
            String result = data.getStringExtra("result");
            if (result.equals("added"))
            {
                displayListViewDataset();
            }
        }

    }

    //add new

    //get routine list

    //onclick to routines, then it can add exercises or start routine

    //
    //String s= getIntent().getStringExtra("programName");


    public void displayListViewDataset() {

        int program_ID=0;
        //get program ID first
        Cursor cursorProgram = getApplicationContext().getContentResolver().query(Program_Data.CONTENT_URI, null, Program_Data.NAME+"='"+programName+"'",
                null, Program_Data._ID + " DESC LIMIT 1");
        if (cursorProgram != null && cursorProgram.moveToFirst()) {
            program_ID = cursorProgram.getInt(cursorProgram.getColumnIndex(Program_Data._ID));
            Log.d("CJJAWL","program_ID = "+program_ID);
        }
        if (cursorProgram != null && !cursorProgram.isClosed())
            cursorProgram.close();

        //search all routines ID related to a program using program ID

        Cursor cursorRountine = getApplicationContext().getContentResolver().query(Program_Routine_Data.CONTENT_URI, null, Program_Routine_Data.PROGRAM_ID+"="+program_ID,
                null, Program_Routine_Data._ID + " ASC");

        //display only routine ID
        if (cursorRountine != null) {
            cursorRountine.moveToFirst();
        }

        String[] columns = new String[] {
                Program_Routine_Data.ROUTINE_NAME
        };
        int[] to = new int[] {
                R.id.textView_itemProgramRoutine2
        };


        dataAdapterData = new SimpleCursorAdapter(this, R.layout.item_programroutinue, cursorRountine, columns, to, 0);

        ListView listView = (ListView) findViewById(R.id.viewprogram_container1);
        listView.setAdapter(dataAdapterData);

        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor =  (Cursor) parent.getItemAtPosition(position);
                String routineName = cursor.getString(cursor.getColumnIndexOrThrow(Program_Routine_Data.ROUTINE_NAME));

                //Toast.makeText(getActivity(),"You selected : " + programName,Toast.LENGTH_SHORT).show();

                //past programName to a new activity
                Intent i = new Intent( getApplicationContext(), ViewRoutine.class);
                i.putExtra("routineName",routineName);
                i.putExtra("programName",programName);
                startActivity(i);

            }
        });
    }

}
