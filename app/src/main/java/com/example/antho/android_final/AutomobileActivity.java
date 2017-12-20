package com.example.antho.android_final;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

//GENERAL REQUIREMENTS
//A Fragment
//Listview* to present items; clicking shows item details
//Items in the listview must be stored, adding and deletion of items
//An Asynctask(open a database/retrieve data/save data)
//Progressbar
//Button*
//EditText* and an associated text input method
//Toast*, Snackbar, custom dialog notification*
//Help menu that shows author, activity number and interface instructions
//alternate language

//ACTIVITY REQUIREMENTS
//User is able to select LITRES, PRICE, KILOMETRES
//entries displayed in a listview
//database stores the time the information was recorded
//app should provide AVERAGE GAS PRICE for last month
//how much gas was purchased LAST MONTH
//AVERAGE GAS PER MONTH(?)

//IMPLEMENTATION PLAN
//Button that pops up a custom dialog to add a new item to the list
//Listview that shows 4columns (Date, Litres, Price, KM(?))
//Selecting items from the listview...
//Toast that pops up when a new entry is made

public class AutomobileActivity extends AppCompatActivity {

    private String ACTIVITY_NAME = "AutomobileActivity";

    ArrayList<String> autoArray = new ArrayList<>();

    private String autoLitres;
    private String autoPrice;
    private String autoMileage;

    private ListView listView;
    private Button autoCreateEntryButton;
    private AutoDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    Cursor cursor;
    private AutoAdapter autoAdapter;

    ListView autoLitresListView;
    ListView autoPriceListView;
    ListView autoMileageListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automobile);
        dbHelper  = new AutoDatabaseHelper(this);
        autoAdapter = new AutoAdapter(this);
        listView = (ListView)findViewById(R.id.autoLitresListView);
        listView.setAdapter(autoAdapter);
        autoLitresListView = (ListView)findViewById(R.id.autoLitresListView);
        autoPriceListView = (ListView)findViewById(R.id.autoPriceListView);
        autoMileageListView  = (ListView)findViewById(R.id.autoMileageListView);

        autoCreateEntryButton = (Button)findViewById(R.id.autoCreateEntryButton);
        autoCreateEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(ACTIVITY_NAME, "Entry button clicked");


                LayoutInflater li= getLayoutInflater();
                final LinearLayout rootTag = (LinearLayout)li.inflate(R.layout.auto_custom_dialog_, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(AutomobileActivity.this);

                builder.setView(rootTag)
                        .setPositiveButton("Confirm Message", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setAutoValues(((EditText)rootTag.findViewById(R.id.autoLitresEditText)).getText().toString(),
                                        ((EditText)rootTag.findViewById(R.id.autoPriceEditText)).getText().toString(),
                                        ((EditText)rootTag.findViewById(R.id.autoMileageEditText)).getText().toString());

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                builder.show();

            }
        });
    }

    public void setAutoValues(String litres, String price, String mileage){
        autoLitres = litres;
        autoPrice = price;
        autoMileage = mileage;
        Log.i(ACTIVITY_NAME, "LITRES:" + litres + " PRICE:" + price + " MILEAGE:" + mileage);

        db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("LITRES", autoLitres);
        cv.put("PRICE", autoPrice);
        cv.put("MILEAGE", autoMileage);
        db.insert("Auto_Table","NullColumnName", cv);
        autoAdapter.notifyDataSetChanged();

        cursor = db.query(dbHelper.AUTO_TABLE, dbHelper.Column_Names,
                null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                autoArray.add(cursor.getString(1));
                Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_LITRES)));
                cursor.moveToNext();
            }
        }
    }


    private class AutoAdapter extends ArrayAdapter<String> {

        public AutoAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {
            return autoArray.size();
        }

        public String getItem(int position) {
            return autoArray.get(position);
        }

        public View getView(int position, View convertView, ViewGroup Parent) {
            Log.i(ACTIVITY_NAME, "in getView");
            LayoutInflater inflater = AutomobileActivity.this.getLayoutInflater();
            View view;
            //if (position % 2 == 0)
                view = inflater.inflate(R.layout.auto_database_layout, null);
            //else
              //  result = inflater.inflate(R.layout.chat_row_outgoing, null);

            TextView litresTextDB = view.findViewById(R.id.litresTextDB);
            litresTextDB.setText(getItem(position));
            return view;
        }
        public long getItemId(int position) {
            cursor.moveToPosition(position);
            long databaseID = 0;
            if(cursor.getCount() > position){
                databaseID = cursor.getLong(0);
            }
            return databaseID;
        }

    }

}
