package com.example.antho.android_final;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ThermostatDatabaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase database;

    public static  final String TABLE_NAME = "THERMOSTAT";
    public static final String KEY_ID = "_id";
    public static final String KEY_DAY = "_day";
    public static final String KEY_TIME = "_time";
    public static final String KEY_TEMPATURE = "_tempature";

    public static final String[] Column_names = new String[]{KEY_DAY, KEY_TIME, KEY_TEMPATURE};

    private static final String DATABASE_NAME = "Thermostat.db";
    private static final int VERSION_NUM = 2;

    public ThermostatDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME + "( " + KEY_ID + " integer primary key autoincrement, " + KEY_DAY
            + " text not null, " + KEY_TIME
            + " integer not null, " + KEY_TEMPATURE + " integer not null);";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(ThermostatDatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(ThermostatDatabaseHelper.class.getName(),
                "Downgrading database from version " + newVersion + " to "
                        + oldVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        Log.i("Database ", "onOpen was called");
    }

    public void openDatabase() {
        database = this.getWritableDatabase();
    }

    public void closeDatabase() {
        if(database != null && database.isOpen()){
            database.close();
        }
    }

    public void insertEntry(String content) {
        ContentValues values = new ContentValues();
        values.put(KEY_DAY, content);
        values.put(KEY_TIME, content);
        values.put(KEY_TEMPATURE, content);
        database.insert(TABLE_NAME, null, values);
    }

    public void deleteItem(String id) {
        this.getWritableDatabase().execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + KEY_ID + " = " + id);
    }

    public Cursor getRecords() {
        return database.query(TABLE_NAME, null, null, null, null, null, null);
    }
}
