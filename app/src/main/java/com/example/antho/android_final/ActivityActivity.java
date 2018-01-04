package com.example.antho.android_final;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class ActivityActivity extends AppCompatActivity {

    TextView stat1;
    TextView stat2;
    TextView stat3;
    TextView stat4;
    TextView stat5;
    TextView stat6;
    TextView timestat1;
    TextView timestat2;
    SQLiteDatabase db;
    ActivityDatabaseHelper adh;
    Cursor cursor;
    Toolbar mTopToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity);

        //Actions for the three initial buttons on the homepage.
        final Button newActivity = (Button) findViewById(R.id.activity_Button1);
        newActivity.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ActivityActivity.this, Activity_NewRecord.class);
                startActivityForResult(intent, 10);
            }
        });
        final Button pastActivity = (Button) findViewById(R.id.activity_Button2);
        pastActivity.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ActivityActivity.this, Activity_PastRecord.class);
                startActivityForResult(intent, 10);
            }
        });

        adh = new ActivityDatabaseHelper(this);
        db = adh.getReadableDatabase();


        String[] allColumns = {ActivityDatabaseHelper.ACTIVITY_TYPE, ActivityDatabaseHelper.ACTIVITY_TIME, ActivityDatabaseHelper.ACTIVITY_COMMENTS, ActivityDatabaseHelper.ACTIVITY_TIMESTAMP};

        String query1 = "SELECT COUNT(" + adh.ACTIVITY_TYPE + ") FROM " + adh.TABLE_NAME + " WHERE " + adh.ACTIVITY_TYPE + " = 'Running';";
        String query2 = "SELECT COUNT(" + adh.ACTIVITY_TYPE + ") FROM " + adh.TABLE_NAME + " WHERE " + adh.ACTIVITY_TYPE + " = 'Walking';";
        String query3 = "SELECT COUNT(" + adh.ACTIVITY_TYPE + ") FROM " + adh.TABLE_NAME + " WHERE " + adh.ACTIVITY_TYPE + " = 'Biking';";
        String query4 = "SELECT COUNT(" + adh.ACTIVITY_TYPE + ") FROM " + adh.TABLE_NAME + " WHERE " + adh.ACTIVITY_TYPE + " = 'Skating';";
        String query5 = "SELECT COUNT(" + adh.ACTIVITY_TYPE + ") FROM " + adh.TABLE_NAME + " WHERE " + adh.ACTIVITY_TYPE + " = 'Swimming';";
        String query6 = "SELECT COUNT(" + adh.ACTIVITY_TYPE + ") FROM " + adh.TABLE_NAME + ";";
        String query7 = "SELECT AVG(" + adh.ACTIVITY_TIME + ") FROM " + adh.TABLE_NAME + ";";
        String query8 = "SELECT SUM(" + adh.ACTIVITY_TIME + ") FROM " + adh.TABLE_NAME + ";";

        cursor = db.rawQuery(query1, null);
        String run = "0";
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            run = cursor.getString(0);
            cursor.moveToNext();
        }
        stat1 = findViewById(R.id.stat1);
        stat1.setText(run);

        cursor = db.rawQuery(query2, null);
        String walk = "0";
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            walk = cursor.getString(0);
            cursor.moveToNext();
        }
        stat2 = findViewById(R.id.stat2);
        stat2.setText(walk);

        cursor = db.rawQuery(query3, null);
        String swim = "0";
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            swim = cursor.getString(0);
            cursor.moveToNext();
        }
        stat3 = findViewById(R.id.stat3);
        stat3.setText(swim);

        cursor = db.rawQuery(query4, null);
        String skate = "0";
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            skate = cursor.getString(0);
            cursor.moveToNext();
        }
        stat4 = findViewById(R.id.stat4);
        stat4.setText(skate);

        cursor = db.rawQuery(query5, null);
        String bike = "0";
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            bike = cursor.getString(0);
            cursor.moveToNext();
        }
        stat5 = findViewById(R.id.stat5);
        stat5.setText(bike);

        cursor = db.rawQuery(query6, null);
        String total = "0";
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            total = cursor.getString(0);
            cursor.moveToNext();
        }
        stat6 = findViewById(R.id.stat6);
        stat6.setText(total);

        cursor = db.rawQuery(query7, null);
        String time = "0";
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            time = cursor.getString(0);
            cursor.moveToNext();
        }
        timestat1 = findViewById(R.id.timestat1);
        timestat1.setText(time);

        cursor = db.rawQuery(query8, null);
        String timepermonth = "0";
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            timepermonth = cursor.getString(0);
            cursor.moveToNext();
        }
        timestat2 = findViewById(R.id.timestat2);
        timestat2.setText(timepermonth);

        mTopToolbar = findViewById(R.id.appToolbar);
        setSupportActionBar(mTopToolbar);
    }


    public void updateStats() {
        this.recreate();
    }

    @Override
    public void onResume() {
        super.onResume();
        adh = new ActivityDatabaseHelper(this);
        db = adh.getReadableDatabase();


        String[] allColumns = {ActivityDatabaseHelper.ACTIVITY_TYPE, ActivityDatabaseHelper.ACTIVITY_TIME, ActivityDatabaseHelper.ACTIVITY_COMMENTS, ActivityDatabaseHelper.ACTIVITY_TIMESTAMP};

        String query1 = "SELECT COUNT(" + adh.ACTIVITY_TYPE + ") FROM " + adh.TABLE_NAME + " WHERE " + adh.ACTIVITY_TYPE + " = 'Running';";
        String query2 = "SELECT COUNT(" + adh.ACTIVITY_TYPE + ") FROM " + adh.TABLE_NAME + " WHERE " + adh.ACTIVITY_TYPE + " = 'Walking';";
        String query3 = "SELECT COUNT(" + adh.ACTIVITY_TYPE + ") FROM " + adh.TABLE_NAME + " WHERE " + adh.ACTIVITY_TYPE + " = 'Biking';";
        String query4 = "SELECT COUNT(" + adh.ACTIVITY_TYPE + ") FROM " + adh.TABLE_NAME + " WHERE " + adh.ACTIVITY_TYPE + " = 'Skating';";
        String query5 = "SELECT COUNT(" + adh.ACTIVITY_TYPE + ") FROM " + adh.TABLE_NAME + " WHERE " + adh.ACTIVITY_TYPE + " = 'Swimming';";
        String query6 = "SELECT COUNT(" + adh.ACTIVITY_TYPE + ") FROM " + adh.TABLE_NAME + ";";

        cursor = db.rawQuery(query1, null);
        String run = "0";
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            run = cursor.getString(0);
            cursor.moveToNext();
        }
        stat1 = findViewById(R.id.stat1);
        stat1.setText(run);

        cursor = db.rawQuery(query2, null);
        String walk = "0";
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            walk = cursor.getString(0);
            cursor.moveToNext();
        }
        stat2 = findViewById(R.id.stat2);
        stat2.setText(walk);

        cursor = db.rawQuery(query3, null);
        String swim = "0";
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            swim = cursor.getString(0);
            cursor.moveToNext();
        }
        stat3 = findViewById(R.id.stat3);
        stat3.setText(swim);

        cursor = db.rawQuery(query4, null);
        String skate = "0";
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            skate = cursor.getString(0);
            cursor.moveToNext();
        }
        stat4 = findViewById(R.id.stat4);
        stat4.setText(skate);

        cursor = db.rawQuery(query5, null);
        String bike = "0";
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            bike = cursor.getString(0);
            cursor.moveToNext();
        }
        stat5 = findViewById(R.id.stat5);
        stat5.setText(bike);

        cursor = db.rawQuery(query6, null);
        String total = "0";
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            total = cursor.getString(0);
            cursor.moveToNext();
        }
        stat6 = findViewById(R.id.stat6);
        stat6.setText(total);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        AlertDialog.Builder builder = null;

        if (id == R.id.about) {
            Toast.makeText(ActivityActivity.this, "Activity Tracker." + "\n" + "Version 1.3" + "\n" + "By: Anthony Geleynse", Toast.LENGTH_LONG).show();
            return true;
        }

        if (id == R.id.help) {
            Log.d("Toolbar", "instructions");

            builder = new AlertDialog.Builder(this);
            String s1 = "Instructions for Activity Tracker.";
            String s2 = "To add a new Activity click on the 'New Activity' button. You can then choose your options and save your activity. ";
            String s3 = "If you click on the Past Activities button you can see all past activities and by clicking on them you can either edit or delete them. ";
            String s4 = "You can see your overall statistic here on the HomePage ";
            builder.setMessage(s1 + "\n" + "\n" + s2 + "\n" + "\n" + s3 + "\n" + "\n" + s4).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            if (builder != null) {
                AlertDialog alert = builder.create();
                builder.show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
