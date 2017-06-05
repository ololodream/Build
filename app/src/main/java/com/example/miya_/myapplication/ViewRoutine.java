package com.example.miya_.myapplication;

/**
 * ViewRoutine
 * Description:
 *         Display view routine page(e.g. programName, routineName, scheduled exercises)
 *         list exercises of a routine
 *         and listen to actions happens in this page
 *         When click on a specific exercise item, turn to view exercise page
 */
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.database.Cursor;

import android.widget.TextView;

import com.example.miya_.myapplication.providers.Program_Routine_Provider;
import com.example.miya_.myapplication.providers.Routine_Provider.Routine_Data;
import com.example.miya_.myapplication.providers.Routine_Exercise_Provider.Routine_Exercise_Data;

public class ViewRoutine extends AppCompatActivity {

    String programName;

    String routineName;

    TextView textView1;

    TextView textView2;

    //exercise list
    SimpleCursorAdapter dataAdapterData;


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("programName", this.programName);
        savedInstanceState.putString("routineName", this.routineName);

        super.onSaveInstanceState(savedInstanceState);

        Log.d("CJJAWL","save");

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewroutine);


        textView1= (TextView) findViewById(R.id.textViewRoutine1);
        textView2= (TextView) findViewById(R.id.textViewRoutine2);

        if (savedInstanceState != null) {
            Log.d("CJJAWL","old");
            programName = savedInstanceState.getString("programName");
            routineName = savedInstanceState.getString("routineName");
        }else {
            Log.d("CJJAWL","new");
            programName = getIntent().getStringExtra("programName");
            routineName = getIntent().getStringExtra("routineName");
        }

        textView1.setText(""+ programName);

        textView2.setText(""+ routineName);

        Log.d("CJJAWL","ON Create = ");

        //buttonViewRoutine1

        Button submit = (Button) findViewById(R.id.buttonViewRoutine1);

        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){


                //pass the programName, routineName
                //get no result
                Intent i = new Intent(getApplicationContext(), Count.class);

                i.putExtra("programName",programName);
                i.putExtra("routineName", routineName);


                startActivity(i);



            }
        });



        //display all exercises using routine name
        displayListViewDataset();


    }


    public void displayListViewDataset() {
        // get routine ID
        int routineID = 0;
        Cursor cursorProgram = getApplicationContext().getContentResolver().query(Routine_Data.CONTENT_URI, null, Routine_Data.NAME+"='"+ routineName +"'",
                null, Routine_Data._ID + " DESC LIMIT 1");
        if (cursorProgram != null && cursorProgram.moveToFirst()) {
            routineID = cursorProgram.getInt(cursorProgram.getColumnIndex(Routine_Data._ID));

        }
        if (cursorProgram != null && !cursorProgram.isClosed())
            cursorProgram.close();

        //search all exercises   related to a routine using routine ID
        Cursor cursorRountine = getApplicationContext().getContentResolver().query(Routine_Exercise_Data.CONTENT_URI, null, Routine_Exercise_Data.ROUTINE_ID+"="+routineID,
                null, Routine_Exercise_Data._ID + " ASC");

        //display  exercise name, set, rept
        if (cursorRountine != null) {
            cursorRountine.moveToFirst();
        }

        String[] columns = new String[] {
                Routine_Exercise_Data.EXERCISE_NAME,
                Routine_Exercise_Data.SET,
                Routine_Exercise_Data.REPT
        };
        int[] to = new int[] {
                R.id.textView_itemRoutineExercise2,
                R.id.textView_itemRoutineExercise4,
                R.id.textView_itemRoutineExercise6
        };


        dataAdapterData = new SimpleCursorAdapter(this, R.layout.item_viewroutineexercise, cursorRountine, columns, to, 0);

        ListView listView = (ListView) findViewById(R.id.viewroutine_container1);
        listView.setAdapter(dataAdapterData);

        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor =  (Cursor) parent.getItemAtPosition(position);
                String exerciseName = cursor.getString(cursor.getColumnIndexOrThrow(Routine_Exercise_Data.EXERCISE_NAME));

                Intent i = new Intent( getApplicationContext(), ViewExercise.class);
                i.putExtra("exerciseName",exerciseName);

                startActivity(i);

            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();
        // put your code here...
        Log.d("CJJAWL", "onResume "+programName);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("CJJAWL","onDestroy");
    }


    @Override
    public void onRestart() {
        super.onRestart();
        Log.d("CJJAWL","onRestart");

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("CJJAWL","onPause");



    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("CJJAWL","onStop");
        
    }


    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);

        setIntent(intent);//must store the new intent unless getIntent() will return the old one

        Log.d("CJJAWL","intent");
    }

}
