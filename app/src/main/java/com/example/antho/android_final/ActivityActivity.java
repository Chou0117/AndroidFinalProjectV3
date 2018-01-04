package com.example.antho.android_final;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity);

        //Actions for the three initial buttons on the homepage.
        final Button newActivity = (Button) findViewById(R.id.activity_Button1);
        newActivity.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(ActivityActivity.this, Activity_NewRecord.class);
                startActivityForResult(intent, 10);
            }
        });
        final Button pastActivity = (Button) findViewById(R.id.activity_Button2);
        pastActivity.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(ActivityActivity.this, Activity_PastRecord.class);
                startActivityForResult(intent, 10);
            }
        });

        final Button personalStatistics = (Button) findViewById(R.id.activity_Button3);
//        personalStatistics.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v){
//                Intent intent = new Intent(ActivityActivity.this, Activity_PersonalStatistics.class);
//                startActivityForResult(intent, 10);
//            }
//        });

    }
}
