package com.example.antho.android_final;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class Activity_NewRecord extends AppCompatActivity {

    SQLiteDatabase database;
    ActivityDatabaseHelper adh;
    ActivityAdapter aa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__new_record);

    }

    private class ActivityAdapter extends ArrayAdapter<String>{

        public ActivityAdapter(Context ctx){
            super(ctx, 0);
        }

    }
}
