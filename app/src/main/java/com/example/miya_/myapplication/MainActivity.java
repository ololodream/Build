package com.example.miya_.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * MainActivity
 * Description:
 *       The entrance of application.
 * Main Method:
 *       onCreate：initialize the nevigation bar and the set the welcome page.
 */
public class MainActivity extends AppCompatActivity {

    //private TextView mTextMessage;
    int fragNum;
    boolean fragChanged;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment frag = null;

            Log.d("NAIVE","click ="+item.getItemId());

            switch (item.getItemId()) {
                //Train
                case R.id.navigation_home:
                    if(fragNum!=1) {
                        frag = TrainFragment.newInstance();
                        fragNum=1;
                        fragChanged=true;
                        navigation.getMenu().getItem(0).setChecked(true);
                        navigation.getMenu().getItem(1).setChecked(false);
                        navigation.getMenu().getItem(2).setChecked(false);
                    }
                    break;
                case R.id.navigation_dashboard:
                    //Program
                    if(fragNum!=2) {
                        frag = ProgramFragment.newInstance();
                        fragNum=2;
                        fragChanged=true;
                        navigation.getMenu().getItem(0).setChecked(false);
                        navigation.getMenu().getItem(1).setChecked(true);
                        navigation.getMenu().getItem(2).setChecked(false);


                        //test

                        //Intent context_unlock = new Intent();
                        //context_unlock.setAction("ACTION_BUILD_START_COUNT");

                        //sendBroadcast(context_unlock);
                    }

                    break;
                case R.id.navigation_notifications:
                    //Me
                    if(fragNum!=3) {
                        frag = MeFragment.newInstance();
                        fragNum=3;
                        fragChanged=true;
                        navigation.getMenu().getItem(0).setChecked(false);
                        navigation.getMenu().getItem(1).setChecked(false);
                        navigation.getMenu().getItem(2).setChecked(true);

                        //test

                        //Intent context_unlock = new Intent();
                        //context_unlock.setAction("ACTION_BUILD_STOP_COUNT");

                        //sendBroadcast(context_unlock);
                    }
                    break;
            }

            if(fragChanged) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content, frag);
                transaction.addToBackStack(null);

                transaction.commit();

                fragChanged=false;
                return true;
            }
            return false;
        }

    };

    BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(MainActivity.this, CountService.class));

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Fragment frag = null;
        frag = MeFragment.newInstance( );
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content, frag).commit();
        fragNum = 3;
        fragChanged=false;

        navigation.getMenu().getItem(0).setChecked(false);
        navigation.getMenu().getItem(1).setChecked(false);
        navigation.getMenu().getItem(2).setChecked(true);
    }

}
