package com.example.antho.android_final;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class ActivityDatabaseHelper extends SQLiteOpenHelper {

    public final static String TABLE_NAME = "Activities";
    final static String DATABASE_NAME = "messages.db";
    final static int VERSION_NUM = 1;
    final static String PRIMARY_KEY = "_ID";
    final static String ACTIVITY_TYPE = "KEY_MESSAGE";
    final static String ACTIVITY_TIME = "ACTIVITY_TIME";
    final static String ACTIVITY_COMMENTS = "ACTIVITY_COMMENTS";
    final static String[] ColumnName = new String[]{
            PRIMARY_KEY,
            ACTIVITY_TYPE,
            ACTIVITY_TIME,
            ACTIVITY_COMMENTS
    };


    private static final String DATABASE_CREATE="CREATE TABLE "+ TABLE_NAME + " ( " + PRIMARY_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ACTIVITY_TYPE + " TEXT NOT NULL, " + ACTIVITY_TIME + " INTEGER, " + ACTIVITY_COMMENTS + " TEXT);";

    public ActivityDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        db.execSQL(DATABASE_CREATE);
        Log.i("ChatDatabaseHelper", "Calling onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV){
        Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldV + " newVersion=" + newV);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldV, int newV){
        Log.i("ChatDatabaseHelper", "Calling onDowngrade, oldVersion=" + oldV + " newVersion=" + newV);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
