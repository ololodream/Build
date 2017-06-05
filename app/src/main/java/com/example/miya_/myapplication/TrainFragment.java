package com.example.miya_.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.example.miya_.myapplication.providers.Complete_Provider.Complete_Data;
import com.example.miya_.myapplication.providers.Program_Provider;

import static android.app.Activity.RESULT_OK;

/**
 * TrainFragment
 * Description:
 *          1.One of the three main section: Train.
 *          2.There are two parts of this section.
 *          The first part shows summary of workout history.
 *          Section two is the quick access of ‘my program’ list.
 *          By clicking on a item of 'my program' list, users can see the detail and start training
 *          of this program.
 *
 * Main method:
 *          1.displayListViewMyProgram:
 *              initialize program list from database
 *          2.onViewCreatedset:
 *              action listener for button ‘create new program’. If user click this button, then
 *              turn to ‘CreateProgram page’
 */

public class TrainFragment extends Fragment {
    private static final String ARG_TEXT = "arg_text";
    private static final String ARG_COLOR = "arg_color";

    private String mText;
    private int mColor;

    private View mContent;

    //all text views are here

    TextView minutes;

    TextView times;

    TextView buildingDays;

    TextView weightLifted;

    public static Fragment newInstance() {
        Fragment frag = new TrainFragment();
        Bundle args = new Bundle();


        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_train, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // retrieve text and color from bundle or savedInstanceState
        if (savedInstanceState == null) {
            Bundle args = getArguments();

        } else {

        }

        // initialize views
        mContent = view.findViewById(R.id.fragment_train);

        minutes = (TextView) view.findViewById(R.id.textTrain2);

        times = (TextView) view.findViewById(R.id.textTrain8);

        buildingDays = (TextView) view.findViewById(R.id.textTrain5);

        weightLifted = (TextView) view.findViewById(R.id.textTrain11);

        displayCounts();


        // initialize views

        //ini button
        Button create = (Button) view.findViewById(R.id.program_button1);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //ManageReplay
                Intent i = new Intent(getActivity().getApplicationContext(), CreateProgram.class);

                startActivityForResult(i, 1);

                //expecting a result
            }
        });
        displayListViewMyProgram();

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode == RESULT_OK && data !=null) {
            String result = data.getStringExtra("result");
            if (result.equals("added"))
            {
                displayListViewMyProgram();
            }
        }

    }
    @Override
    public void onResume() {
        Log.e("DEBUG", "onResume of LoginFragment");
        super.onResume();

        displayCounts();
        displayListViewMyProgram();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }

    public void displayCounts(){

        //Routine_Data.NAME+"='"+routineName+"'"
        int dayCount=0;

        {
            Cursor cursorRoutine = getActivity().getContentResolver().query(Complete_Data.CONTENT_URI,
                    null,
                    null,
                    null,  Complete_Data._ID+" DESC");


            if (cursorRoutine != null && cursorRoutine.moveToFirst()) {
                dayCount = 1;
                int dayLast=cursorRoutine.getInt(cursorRoutine.getColumnIndex(Complete_Data.DAY));
                int monthLast=cursorRoutine.getInt(cursorRoutine.getColumnIndex(Complete_Data.MONTH));
                int yearLast=cursorRoutine.getInt(cursorRoutine.getColumnIndex(Complete_Data.YEAR));
                while (cursorRoutine.moveToNext()) {
                    int dayNow = cursorRoutine.getInt(cursorRoutine.getColumnIndex(Complete_Data.DAY));
                    int monthNow=cursorRoutine.getInt(cursorRoutine.getColumnIndex(Complete_Data.MONTH));
                    int yearNow=cursorRoutine.getInt(cursorRoutine.getColumnIndex(Complete_Data.YEAR));
                    if(dayNow==dayLast&&monthNow==monthLast&&yearNow==yearLast)
                    {
                        Log.d("CJJAWL","not empty");
                    }
                    else
                    {
                        dayCount=dayCount+1;
                    }
                }
            }
            if (cursorRoutine != null && !cursorRoutine.isClosed())
                cursorRoutine.close();
        }

        buildingDays.setText(""+dayCount);

        int timeCount=0;

        {
            Cursor cursorRoutine = getActivity().getContentResolver().query(Complete_Data.CONTENT_URI,
                    null, null,
                    null, Complete_Data._ID);
            if (cursorRoutine != null && cursorRoutine.moveToFirst()) {
                timeCount = cursorRoutine.getCount();
            }
            if (cursorRoutine != null && !cursorRoutine.isClosed())
                cursorRoutine.close();
        }
        times.setText(""+timeCount);

        int mintuesCount=0;
        {
            Cursor cursorRoutine = getActivity().getContentResolver().query(Complete_Data.CONTENT_URI,
                    null, null,
                    null, Complete_Data._ID);
            if (cursorRoutine != null) {
                while (cursorRoutine.moveToNext()) {
                    mintuesCount = mintuesCount +  cursorRoutine.getInt(cursorRoutine.getColumnIndex(Complete_Data.SECOND));
                }
            }
            if (cursorRoutine != null && !cursorRoutine.isClosed())
                cursorRoutine.close();
        }
        mintuesCount=mintuesCount/60;

        minutes.setText(""+mintuesCount);

        int weightCount=0;
        {
            Cursor cursorRoutine = getActivity().getContentResolver().query(Complete_Data.CONTENT_URI,
                    null, null,
                    null, Complete_Data._ID);
            if (cursorRoutine != null) {
                while (cursorRoutine.moveToNext()) {

                    weightCount = weightCount +  cursorRoutine.getInt(cursorRoutine.getColumnIndex(Complete_Data.WEIGHT));
                }
            }
            if (cursorRoutine != null && !cursorRoutine.isClosed())
                cursorRoutine.close();
        }


        weightLifted.setText(""+weightCount);


        /*
       ContentValues rowData = new ContentValues();

                                rowData.put(Complete_Data.ROUTINE_ID, routineID);
                                rowData.put(Complete_Data.HOUR, 0);
                                rowData.put(Complete_Data.MINUTE, 0);
                                rowData.put(Complete_Data.SECOND, secondTimer);
                                rowData.put(Complete_Data.YEAR, c.get(Calendar.YEAR));
                                rowData.put(Complete_Data.MONTH, c.get(Calendar.MONTH));
                                rowData.put(Complete_Data.DAY, c.get(Calendar.DAY_OF_MONTH));
                                //get this from UI
                                rowData.put(Complete_Data.WEIGHT, weightNumber);//weight kg
                                getApplicationContext().getContentResolver().insert(Complete_Data.CONTENT_URI, rowData);

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

         */
    }

    SimpleCursorAdapter dataAdapterData;
    public void displayListViewMyProgram() {
        Cursor cursor = getActivity().getContentResolver().query(Program_Provider.Program_Data.CONTENT_URI, null,
                null, null, Program_Provider.Program_Data._ID + " ASC");
        if (cursor != null) {
            cursor.moveToFirst();
        }

        String[] columns = new String[]{
                Program_Provider.Program_Data.NAME

        };

        //UI binding
        int[] to = new int[]{
                R.id.textView_itemMyProgram2
        };

        dataAdapterData = new SimpleCursorAdapter(getActivity(), R.layout.item_myprogram, cursor, columns, to, 0);

        final ListView listView = (ListView) getActivity().findViewById(R.id.program_container1);


        listView.setAdapter(dataAdapterData);

        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                String programName = cursor.getString(cursor.getColumnIndexOrThrow(Program_Provider.Program_Data.NAME));

                //Toast.makeText(getActivity(),"You selected : " + programName,Toast.LENGTH_SHORT).show();

                //past programName to a new activity
                Intent i = new Intent(getActivity().getApplicationContext(), ViewProgram.class);
                i.putExtra("programName", programName);
                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);

            }
        });
    }
}