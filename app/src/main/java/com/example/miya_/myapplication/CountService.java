package com.example.miya_.myapplication;

import android.app.Service;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

import computing.AddSome;

import com.example.miya_.myapplication.providers.Exercise_Provider.Exercise_Data;

/**
* Name: CountService
* Description:
*   count service start when application is active and run at the back end
* Main Method:
*   onCreate:
*           initialize sensor manager
*   onSensorChanged:
*           get and maintain accelerometer data from sensor and push the latest 10s data to countservice
 *  onDestroy:
 *          unregisterListener sensor
* */
public class CountService extends Service implements SensorEventListener {

    public static boolean started; //sensing started?

    private static SensorManager mSensorManager;
    private static Sensor mAccelerometer;
    private static Sensor mGyroscope;

    private static HandlerThread mHandlerThread;

    @Override
    public void onCreate() {

        Log.d("BUILD", "AddSome.addOne() = "+AddSome.addOne(1));

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        //sensor type
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        mHandlerThread = new HandlerThread("sensorThread");

        mHandlerThread.start();

        IntentFilter command_filter = new IntentFilter();
        command_filter.addAction("ACTION_BUILD_START_COUNT");
        command_filter.addAction("ACTION_BUILD_STOP_COUNT");

        registerReceiver(commandListener, command_filter);


        //add demo data

        addDemoData(true);
        //addDemoData(false);


        //test code
        //put exercise data in DB
        /*
        {
            ContentValues rowData = new ContentValues();
            rowData.put(Exercise_Data.NAME, "Squat");
            rowData.put(Exercise_Data.PREPARATION, "preparation text");
            rowData.put(Exercise_Data.EXECUTION, "execution text");
            rowData.put(Exercise_Data.COMMENTS, "comments text");
            getApplicationContext().getContentResolver().insert(Exercise_Data.CONTENT_URI, rowData);
        }
        {
            ContentValues rowData = new ContentValues();
            rowData.put(Routine_Data.HOUR, 23);
            rowData.put(Routine_Data.MINUTE, 59);
            rowData.put(Routine_Data.SECOND, 59);
            getApplicationContext().getContentResolver().insert(Routine_Data.CONTENT_URI, rowData);
        }
        {
            ContentValues rowData = new ContentValues();
            rowData.put(Routine_Exercise_Data.ROUTINE_ID, 1);
            rowData.put(Routine_Exercise_Data.EXERCISE_ID, 1);
            rowData.put(Routine_Exercise_Data.SET, 3);
            rowData.put(Routine_Exercise_Data.REPT, 10);
            getApplicationContext().getContentResolver().insert(Routine_Exercise_Data.CONTENT_URI, rowData);
        }

        {
            ContentValues rowData = new ContentValues();
            rowData.put(Program_Data.NAME, "Squat Program");
            rowData.put(Program_Data.BEGIN_YEAR, 2016);
            rowData.put(Program_Data.BEGIN_MONTH, 12);
            rowData.put(Program_Data.BEGIN_DAY, 31);
            rowData.put(Program_Data.END_YEAR, 2017);
            rowData.put(Program_Data.END_MONTH, 12);
            rowData.put(Program_Data.END_DAY, 1);
            rowData.put(Program_Data.PROGRAM_DAY_MONDAY, 1);
            rowData.put(Program_Data.PROGRAM_DAY_TUESDAY, 1);
            rowData.put(Program_Data.PROGRAM_DAY_WEDNESDAY, 0);
            rowData.put(Program_Data.PROGRAM_DAY_THURSDAY, 0);
            rowData.put(Program_Data.PROGRAM_DAY_FRIDAY, 1);
            rowData.put(Program_Data.PROGRAM_DAY_SATURDAY, 1);
            rowData.put(Program_Data.PROGRAM_DAY_SUNDAY, 1);
            getApplicationContext().getContentResolver().insert(Program_Data.CONTENT_URI, rowData);
        }

        {
            ContentValues rowData = new ContentValues();
            rowData.put(Program_Routine_Data.PROGRAM_ID, 1);
            rowData.put(Program_Routine_Data.ROUTINE_ID, 1);
            getApplicationContext().getContentResolver().insert(Program_Routine_Data.CONTENT_URI, rowData);
        }

        {
            ContentValues rowData = new ContentValues();
            rowData.put(Complete_Data.ROUTINE_ID, 1);
            rowData.put(Complete_Data.HOUR, 666);
            rowData.put(Complete_Data.MINUTE, 19);
            rowData.put(Complete_Data.SECOND, 19);
            rowData.put(Complete_Data.WEIGHT, 100000); //100000g
            getApplicationContext().getContentResolver().insert(Complete_Data.CONTENT_URI, rowData);
        }
*/

    }


    boolean initThread=false;

    private CommandListener commandListener = new CommandListener();

    public class CommandListener extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ((intent.getAction().equals("ACTION_BUILD_START_COUNT")) && (!started)) {
                Log.d("BUILD", "Start count!");

                started = true;



                if(!initThread)
                {

                    Handler handler = new Handler(mHandlerThread.getLooper());

                    mSensorManager.registerListener(CountService.this, mAccelerometer,
                            SensorManager.SENSOR_DELAY_FASTEST, handler);

                    mSensorManager.registerListener(CountService.this, mGyroscope,
                            SensorManager.SENSOR_DELAY_FASTEST, handler);

                    initThread=true;

                    count_thread.start();
                }

            }

            if ((intent.getAction().equals("ACTION_BUILD_STOP_COUNT"))&&(started)) {
                Log.d("BUILD", "Stop count!");

                //count_thread.interrupt();
                started = false;
            }

        }
    }

    float gyr_x = 0;
    float gyr_y = 0;
    float gyr_z = 0;

    //put an array of sensorValue here
    ArrayList<AccSensorValue> accSensorValues = new ArrayList<>();
    ArrayList<GyrSensorValue> gyrSensorValues = new ArrayList<>();


    //put an array of sensorValue here

    //Last 10Sec copy of values
    ArrayList<AccSensorValue> accSensorValues10Sec = new ArrayList<>();
    ArrayList<GyrSensorValue> gyrSensorValues10Sec = new ArrayList<>();

    //please do in detection algorithm to refresh 10sec copies
    //
    //accSensorValues10Sec = new ArrayList<>(accSensorValues);
    //gyrSensorValues10Sec = new ArrayList<>(gyrSensorValues);

    //decide to take out!!!!

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            //data collection in values

            //acc_x = event.values[0];
            //acc_y = event.values[1];
            //acc_z = event.values[2];

            //data collection in array

            AccSensorValue ValueItem = new AccSensorValue();
            ValueItem.setAcc_x(event.values[0]);
            ValueItem.setAcc_y(event.values[1]);
            ValueItem.setAcc_z(event.values[2]);

            ValueItem.setTimeInMs((event.timestamp+500000)/1000000);

            accSensorValues.add(ValueItem);

            //remove?

            while(accSensorValues.size()>0) {
                if(accSensorValues.get(0).getTimeInMs()+10000<System.currentTimeMillis())
                {
                    accSensorValues.remove(0);
                }
                else
                {
                    break;
                }
            }



            //long nanoTime = event.timestamp;

            //each time your have an acc value, you have a gyro one at the same time

            //Log.d("BUILD", "acc_x = "+acc_x);
            //Log.d("BUILD", "nanoTime = "+nanoTime);
            //Log.d("BUILD", "acc_x = "+acc_x);
            //Log.d("BUILD", "acc_y = "+acc_y);
            //Log.d("BUILD", "acc_z = "+acc_z);

            //put acceleration data in DB
//
//            ContentValues rowData = new ContentValues();
//            rowData.put(Accelerometer_Data.TIMESTAMP, System.currentTimeMillis());
//            rowData.put(Accelerometer_Data.VALUES_0, x_acc);
//            rowData.put(Accelerometer_Data.VALUES_1, y_acc);
//            rowData.put(Accelerometer_Data.VALUES_2, z_acc);
//            getApplicationContext().getContentResolver().insert(Accelerometer_Data.CONTENT_URI, rowData);
        }

        if (sensor.getType() == Sensor.TYPE_GYROSCOPE) //with drift compensation
        {

            //data collection in values

            //gyr_x = event.values[0];
            //gyr_y = event.values[1];
            //gyr_z = event.values[2];

            //data collection in array

            GyrSensorValue ValueItem = new GyrSensorValue();
            ValueItem.setGyr_x(event.values[0]);
            ValueItem.setGyr_y(event.values[1]);
            ValueItem.setGyr_z(event.values[2]);

            ValueItem.setTimeInMs((event.timestamp+500000)/1000000);

            gyrSensorValues.add(ValueItem);

            //remove?
            while(gyrSensorValues.size()>0) {
                if(gyrSensorValues.get(0).getTimeInMs()+10000<System.currentTimeMillis())
                {
                    gyrSensorValues.remove(0);
                }
                else
                {
                    break;
                }
            }

            //long nanoTime = event.timestamp;

            //Log.d("BUILD", "gyr_x = "+gyr_x);
            //Log.d("BUILD", "nanoTime = "+nanoTime);

//            Log.d("BUILD", "gyr_x = "+gyr_x);
//            Log.d("BUILD", "gyr_y = "+gyr_y);
//            Log.d("BUILD", "gyr_z = "+gyr_z);
        }
    }

    long timestamp=0;
    long delay=1000;

    public Thread count_thread = new Thread(){
        public void run (){
            while(true) { //started

                if(started) {
//
                /*
                float acc_x=0;
                float acc_y = 0;
                float acc_z =0 ;
                float gyr_x = 0;
                float gyr_y = 0;
                float gyr_z = 0;
                 */
                    //TODO: Detect the exercise regions!

                    String exercise = "Lunge";
                    if (timestamp/1000>10)
                    {
                        exercise = "Dumbbell Kickback";
                    }
                    Intent context_unlock = new Intent();
                    context_unlock.setAction("ACTION_BUILD_COUNT_RESULT");
                    context_unlock.putExtra("exercise", exercise);
                    sendBroadcast(context_unlock);

                    Intent context_clock = new Intent();
                    context_clock.setAction("ACTION_BUILD_COUNT_TIMER");
                    context_clock.putExtra("timestamp", timestamp);
                    sendBroadcast(context_clock);

//                    String exercise2 = "Squat";
//
//                    Intent context_unlock2 = new Intent();
//                    context_unlock2.setAction("ACTION_BUILD_COUNT_RESULT");
//                    context_unlock2.putExtra("exercise", exercise2);
//                    sendBroadcast(context_unlock2);
//
//                    Intent context_clock2 = new Intent();
//                    context_clock2.setAction("ACTION_BUILD_COUNT_TIMER");
//                    context_clock2.putExtra("timestamp", timestamp);
//                    sendBroadcast(context_clock2);

//                    String exercise3 = "Dumbbell Kickback";
//
//                    Intent context_unlock3 = new Intent();
//                    context_unlock3.setAction("ACTION_BUILD_COUNT_RESULT");
//                    context_unlock3.putExtra("exercise", exercise3);
//                    sendBroadcast(context_unlock3);
//
//                    Intent context_clock3 = new Intent();
//                    context_clock3.setAction("ACTION_BUILD_COUNT_TIMER");
//                    context_clock3.putExtra("timestamp", timestamp);
//                    sendBroadcast(context_clock3);
                    timestamp = timestamp + delay;
                }

                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


    };


    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d("CJJAWL","Service DDDDDDDDDDDDDDDDDDDDDD");
        mHandlerThread.quitSafely();
        mSensorManager.unregisterListener(CountService.this, mAccelerometer);
        mSensorManager.unregisterListener(CountService.this, mGyroscope);
        unregisterReceiver(commandListener);
    }


    public void addDemoData(boolean demo)
    {
        if(!demo)
        {
            return;
        }

        //add exercise
        //Squat
        {
            Cursor cursor_app = getContentResolver().query(Exercise_Data.CONTENT_URI, null, null, null, Exercise_Data._ID + " DESC LIMIT 1");
            int number = cursor_app.getCount();
            if (cursor_app != null && cursor_app.moveToFirst()) {

            }
            if (cursor_app != null && !cursor_app.isClosed()) {

                cursor_app.close();
            }
            if(number==0) {
                ContentValues rowData = new ContentValues();
//                rowData.put(Exercise_Data.NAME, "Squat");
//                rowData.put(Exercise_Data.PREPARATION, "preparation");
//                rowData.put(Exercise_Data.EXECUTION, "execution");
//                rowData.put(Exercise_Data.COMMENTS, "comments");
//                getApplicationContext().getContentResolver().insert(Exercise_Data.CONTENT_URI, rowData);

//                rowData = new ContentValues();
//                rowData.put(Exercise_Data.NAME, "Deadlift");
//                rowData.put(Exercise_Data.PREPARATION, "preparation");
//                rowData.put(Exercise_Data.EXECUTION, "execution");
//                rowData.put(Exercise_Data.COMMENTS, "comments");
//                getApplicationContext().getContentResolver().insert(Exercise_Data.CONTENT_URI, rowData);


                //new added
                rowData = new ContentValues();
                rowData.put(Exercise_Data.NAME, "Lunge");
                rowData.put(Exercise_Data.PREPARATION, "1.Stand with hands on hips or clasped behind neck\n");
                rowData.put(Exercise_Data.EXECUTION, "1. Lunge forward with first leg. Land on heel, then forefoot\n" +
                        "2. Lower body by flexing knee and hip of front leg until kne\n" +
                        "    -e of rear leg is almost in contact with floor \n" +
                        "3. Return to original standing position by forcibly extending \n" +
                        "    hip and knee of forward leg\n" +
                        "4. Repeat by alternating lunge with opposite leg\n");
                rowData.put(Exercise_Data.COMMENTS, "1. Keep torso upright during lunge\n");
                getApplicationContext().getContentResolver().insert(Exercise_Data.CONTENT_URI, rowData);

                rowData = new ContentValues();
                rowData.put(Exercise_Data.NAME, "Dumbbell Kickback");
                rowData.put(Exercise_Data.PREPARATION, "1.Kneel over bench with arm supporting body. Grasp dumbbell. Position upper arm parallel to floor.\n");
                rowData.put(Exercise_Data.EXECUTION, "1.Extend arm until it is straight. \n" +
                        "2.Return and repeat. Continue with opposite arm.\n" +
                        "\n");
                rowData.put(Exercise_Data.COMMENTS, "1.For greater range of motion, upper arm can be positioned with elbow slightly higher than shoulder.\n");
                getApplicationContext().getContentResolver().insert(Exercise_Data.CONTENT_URI, rowData);
            }
        }
    }

}
