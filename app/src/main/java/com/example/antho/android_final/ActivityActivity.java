package com.example.antho.android_final;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ActivityActivity extends AppCompatActivity {

    TextView stat1;
    TextView stat2;
    TextView stat3;
    TextView stat4;
    TextView stat5;
    TextView stat6;
    SQLiteDatabase db;
    ActivityDatabaseHelper adh;
    Cursor cursor;
    private Toolbar mTopToolbar;


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

        adh = new ActivityDatabaseHelper(this);
        db = adh.getReadableDatabase();


        String[] allColumns = {ActivityDatabaseHelper.ACTIVITY_TYPE, ActivityDatabaseHelper.ACTIVITY_TIME, ActivityDatabaseHelper.ACTIVITY_COMMENTS, ActivityDatabaseHelper.ACTIVITY_TIMESTAMP};

        String query1="SELECT COUNT("+adh.ACTIVITY_TYPE+ ") FROM "+adh.TABLE_NAME+" WHERE " + adh.ACTIVITY_TYPE + " = 'Running';";
        String query2="SELECT COUNT("+adh.ACTIVITY_TYPE+ ") FROM "+adh.TABLE_NAME+" WHERE " + adh.ACTIVITY_TYPE + " = 'Walking';";
        String query3="SELECT COUNT("+adh.ACTIVITY_TYPE+ ") FROM "+adh.TABLE_NAME+" WHERE " + adh.ACTIVITY_TYPE + " = 'Biking';";
        String query4="SELECT COUNT("+adh.ACTIVITY_TYPE+ ") FROM "+adh.TABLE_NAME+" WHERE " + adh.ACTIVITY_TYPE + " = 'Skating';";
        String query5="SELECT COUNT("+adh.ACTIVITY_TYPE+ ") FROM "+adh.TABLE_NAME+" WHERE " + adh.ACTIVITY_TYPE + " = 'Swimming';";
        String query6="SELECT COUNT("+adh.ACTIVITY_TYPE+ ") FROM "+adh.TABLE_NAME+";";

        cursor = db.rawQuery(query1, null);
        String run = "0";
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            run = cursor.getString(0);
            cursor.moveToNext();
        }
        stat1 = findViewById(R.id.stat1); stat1.setText(run);

        cursor = db.rawQuery(query2, null);
        String walk = "0";
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            walk = cursor.getString(0);
            cursor.moveToNext();
        }
        stat2 = findViewById(R.id.stat2); stat2.setText(walk);

        cursor = db.rawQuery(query3, null);
        String swim = "0";
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            swim = cursor.getString(0);
            cursor.moveToNext();
        }
        stat3 = findViewById(R.id.stat3); stat3.setText(swim);

        cursor = db.rawQuery(query4, null);
        String skate = "0";
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            skate = cursor.getString(0);
            cursor.moveToNext();
        }
        stat4 = findViewById(R.id.stat4); stat4.setText(skate);

        cursor = db.rawQuery(query5, null);
        String bike = "0";
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            bike = cursor.getString(0);
            cursor.moveToNext();
        }
        stat5 = findViewById(R.id.stat5); stat5.setText(bike);

        cursor = db.rawQuery(query6, null);
        String total = "0";
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            total = cursor.getString(0);
            cursor.moveToNext();
        }
        stat6 = findViewById(R.id.stat6); stat6.setText(total);
    }


    public void updateStats(){
        this.recreate();
    }
}
