package com.example.miya_.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.database.Cursor;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by chuluo on 1/5/17.
 */
import com.example.miya_.myapplication.providers.Exercise_Provider.Exercise_Data;

/**
 * ViewExercise
 * Description:
 *      Show view exercise page. Get data from database.
 *      Show exerciseName, preparation, execution and comments of an exercise
 */
public class ViewExercise extends AppCompatActivity {

    String exerciseName;

    String preparation;

    String execution;

    String comment;

    TextView textViewexerciseName;

    TextView textViewPrepare;

    TextView textViewExecution;

    TextView textViewComments;
    ImageView imageExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        textViewexerciseName= (TextView) findViewById(R.id.textviewActivityExercise1);

        textViewPrepare = (TextView) findViewById(R.id.textviewActivityExercise3);

        textViewExecution = (TextView) findViewById(R.id.textviewActivityExercise5);

        textViewComments = (TextView) findViewById(R.id.textviewActivityExercise7);
        imageExercise = (ImageView) findViewById(R.id.imageViewExercise) ;

        exerciseName = getIntent().getStringExtra("exerciseName");

        textViewexerciseName.setText(exerciseName);

        Cursor cursorRoutine = getApplicationContext().getContentResolver().query(Exercise_Data.CONTENT_URI, null, Exercise_Data.NAME+"='"+exerciseName+"'",
                null, Exercise_Data._ID + " DESC LIMIT 1");
        if (cursorRoutine != null && cursorRoutine.moveToFirst()) {
            preparation = cursorRoutine.getString(cursorRoutine.getColumnIndex(Exercise_Data.PREPARATION));
            execution = cursorRoutine.getString(cursorRoutine.getColumnIndex(Exercise_Data.EXECUTION));
            comment = cursorRoutine.getString(cursorRoutine.getColumnIndex(Exercise_Data.COMMENTS));
        }
        if (cursorRoutine != null && !cursorRoutine.isClosed())
            cursorRoutine.close();

        textViewPrepare.setText(preparation);

        textViewExecution.setText(execution);

        textViewComments.setText(comment);

        //put image


        if(exerciseName.equals("Squat"))
        {

            //squat
            imageExercise.setImageResource(R.mipmap.squat);

            //squat.png

        }
        if(exerciseName.equals("Dumbbell Kickback"))
        {

            //squat
            imageExercise.setImageResource(R.mipmap.babell);

            //squat.png

        }

        //

    }


}
