package com.example.miya_.myapplication;

/**
 * ProgramFragment
 * Description:
 *          One of the three main section: Program.
 *          In this section, user can choose a program they want to take
 *          and create a new program
 * Main method:
 *          1.displayListViewMyProgram:
 *              initialize program list from database
 *          2.onViewCreatedset:
 *              action listener for button ‘create new program’. If user click this button, then
 *              turn to ‘CreateProgram page’
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.database.Cursor;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.example.miya_.myapplication.providers.Program_Provider.Program_Data;

import static android.app.Activity.RESULT_OK;


public class ProgramFragment extends Fragment {

    private View mContent;


    public static Fragment newInstance( ) {
        Fragment frag = new ProgramFragment();
        Bundle args = new Bundle();

        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_program, container, false);
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
        mContent = view.findViewById(R.id.fragment_program);
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

        //my program list
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
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }

    SimpleCursorAdapter dataAdapterData;
    public void displayListViewMyProgram() {
        Cursor cursor = getActivity().getContentResolver().query(Program_Data.CONTENT_URI, null,
                null, null, Program_Data._ID + " ASC");
        if (cursor != null) {
            cursor.moveToFirst();
        }

        String[] columns = new String[] {
                Program_Data.NAME

        };

        //UI binding
        int[] to = new int[] {
                R.id.textView_itemMyProgram2
        };

        dataAdapterData = new SimpleCursorAdapter(getActivity(), R.layout.item_myprogram, cursor, columns, to, 0);

        final ListView listView = (ListView) getActivity().findViewById(R.id.program_container1);



        listView.setAdapter(dataAdapterData);

        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor =  (Cursor) parent.getItemAtPosition(position);
                String programName = cursor.getString(cursor.getColumnIndexOrThrow(Program_Data.NAME));

                //Toast.makeText(getActivity(),"You selected : " + programName,Toast.LENGTH_SHORT).show();

                //past programName to a new activity
                Intent i = new Intent( getActivity().getApplicationContext(), ViewProgram.class);
                i.putExtra("programName",programName);
                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);

            }
        });

        //family is dataset

        //event is replay
    }



}