package com.example.antho.android_final;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FoodDatabaseHelper extends SQLiteOpenHelper {

    private static final String ACTIVITY_NAME = "FoodDatabaseHelper";

    private static final String DATABASE_NAME = "FoodInfo.db";
    private static final int VERSION_NUM = 1;
    public static final String FOOD_TABLE = "Food_Table";
    //PRIMARY KEY
    public static final String PRIMARY_KEY = "_ID";
    public static final String COLUMN_TYPES = "TYPES";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_CALORIES = "CALORIES";
    public static final String COLUMN_FAT = "FAT";
    public static final String COLUMN_CARBOHYDRATE = "CARBOHYDRATE";
    public static final String COLUMN_DATE = "DATE";

    public static final String[] Column_Names = new String[]{
            PRIMARY_KEY,
            COLUMN_TYPES,
            COLUMN_NAME,
            COLUMN_CALORIES,
            COLUMN_FAT,
            COLUMN_CARBOHYDRATE,
            COLUMN_DATE
    };

    public FoodDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);

}
    public String CREATE_AUTO_TABLE =
            "create table "  + FOOD_TABLE  + " ( " + PRIMARY_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TYPES + " numeric, " + COLUMN_NAME + " string, " + COLUMN_CALORIES + " numeric, " + COLUMN_FAT + " numeric, " + COLUMN_CARBOHYDRATE + " numeric, " + COLUMN_DATE + " date);";

    @Override
    public void onCreate(SQLiteDatabase db){
        Log.i(ACTIVITY_NAME, "Calling onCreate");
        Log.i(ACTIVITY_NAME, CREATE_AUTO_TABLE);
        db.execSQL(CREATE_AUTO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        Log.i(ACTIVITY_NAME, "Calling onUpgrade, oldVersion=" + oldVer + "newVersion="+ newVer);
        db.execSQL("DROP TABLE IF EXISTS " + FOOD_TABLE);
        onCreate(db);

    }
}
