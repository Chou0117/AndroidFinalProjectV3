package com.example.antho.android_final;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    //Activity name for Log.i
    protected static final String ACTIVITY_NAME = "MainActivity";
    //ImageButton for accessing each activity
    ImageButton activityActivityButton;
    ImageButton foodActivityButton; // By Johnny
    ImageButton thermostatActivityButton;
    ImageButton automobileActivityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Show Log.i on create
        Log.i(ACTIVITY_NAME, "In onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set ActivityActivityButton OnClick Intent
        activityActivityButton = findViewById(R.id.activityActivityButton);
        activityActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ActivityActivity.class);
                startActivity(intent);
            }
        });

        //Set FoodActivityButton OnClick Intent
        foodActivityButton = findViewById(R.id.foodActivityButton);
        foodActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FoodActivity.class);
                startActivity(intent);
            }
        });

        //Set ThermostatActivityButton OnClick Intent
        thermostatActivityButton = findViewById(R.id.thermostatActivityButton);
        thermostatActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ThermostatActivity.class);
                startActivity(intent);
            }
        });

        //Set AutomobileActivityButton OnClick Intent
        automobileActivityButton = findViewById(R.id.automobileActivityButton);
        automobileActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AutomobileActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume(){
        Log.i(ACTIVITY_NAME, "In onResume()");
        super.onResume();
    }
    @Override
    public void onStart(){
        Log.i(ACTIVITY_NAME, "In onStart()");
        super.onStart();
    }
    @Override
    public void onPause(){
        Log.i(ACTIVITY_NAME, "In onPause()");
        super.onPause();
    }
    @Override
    public void onStop(){
        Log.i(ACTIVITY_NAME, "In onStop()");
        super.onStop();
    }
    @Override
    public void onDestroy(){
        Log.i(ACTIVITY_NAME, "In onDestroy()");
        super.onDestroy();
    }
}
