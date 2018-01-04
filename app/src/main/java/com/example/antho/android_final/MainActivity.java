package com.example.antho.android_final;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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


    public boolean onCreateOptionsMenu(Menu m) {
        getMenuInflater().inflate(R.menu.main_toolbar_menu, m);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem mi) {

        AlertDialog.Builder instruction;
        Intent intent;
        switch (mi.getItemId()) {

            case R.id.ActivityAct:
                intent = new Intent(MainActivity.this, ActivityActivity.class);
                startActivity(intent);
                break;
            case R.id.FoodAct:
                intent = new Intent(MainActivity.this, FoodActivity.class);
                startActivity(intent);
                break;
            case R.id.ThermoAct:
                intent = new Intent(MainActivity.this, ThermostatActivity.class);
                startActivity(intent);
                break;
            case R.id.AutoAct:
                intent = new Intent(MainActivity.this, AutomobileActivity.class);
                startActivity(intent);
                break;

        }
        return true;
    }
}
