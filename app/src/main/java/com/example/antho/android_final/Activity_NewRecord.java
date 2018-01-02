package com.example.antho.android_final;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

public class Activity_NewRecord extends AppCompatActivity {

    SQLiteDatabase database;
    ActivityDatabaseHelper adh;
    ActivityAdapter aa;
    Cursor cursor;
    EditText et;
    EditText et2;
    Button submitButton;
    RadioGroup radioGroup;
    RadioButton radioButton;
    ArrayList<String> activityList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__new_record);

        adh = new ActivityDatabaseHelper(this);
        aa = new ActivityAdapter(this);

        database = adh.getReadableDatabase();
        String[] allColumns = {ActivityDatabaseHelper.PRIMARY_KEY, ActivityDatabaseHelper.ACTIVITY_TYPE, ActivityDatabaseHelper.ACTIVITY_TIME, ActivityDatabaseHelper.ACTIVITY_COMMENTS};
        cursor = database.query(adh.TABLE_NAME, allColumns, null, null, null, null, null);

        radioGroup = findViewById((R.id.radioGroup));
        et = findViewById(R.id.newRecordTime);
        et2 = findViewById(R.id.newRecordComments);

        submitButton = findViewById(R.id.submit_button);

        database = adh.getWritableDatabase();
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(this.toString(), "Reached onClick");
                ContentValues cv = new ContentValues();
                int selectedID = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedID);
                cv.put("ACTIVITY_TYPE", radioButton.getText().toString());
                cv.put("ACTIVITY_TIME", et.getText().toString());
                cv.put("ACTIVITY_COMMENTS", et2.getText().toString());
                try{
                    database.insert(adh.TABLE_NAME, null, cv);
                }catch (Exception e){
                    Log.i(this.toString(), "Error inserting values in database");
                }
                aa.notifyDataSetChanged();
                et.setText("");
                et2.setText("");
                radioGroup.clearCheck();
            }
        });
    }

    private class ActivityAdapter extends ArrayAdapter<String>{

        public ActivityAdapter(Context ctx){
            super(ctx, 0);
        }

    }
}
