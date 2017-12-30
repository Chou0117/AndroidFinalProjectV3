package com.example.antho.android_final;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.support.design.widget.Snackbar.LENGTH_INDEFINITE;

//GENERAL REQUIREMENTS
//*A Fragment
//*Listview to present items;
// clicking shows item details
//*Items in the listview must be stored, adding and deletion of items
//*An Asynctask(open a database/retrieve data/save data)
//Progressbar
//*Button
//*EditText and an associated text input method
//*Toast,
//Snackbar,
//*custom dialog notification
//*Help menu that shows author, activity number and interface instructions
//alternate language

//ACTIVITY REQUIREMENTS
//*User is able to select LITRES, PRICE, KILOMETRES
//*entries displayed in a listview
//*database stores the time the information was recorded
//app should provide AVERAGE GAS PRICE for last month
//how much gas was purchased LAST MONTH
//AVERAGE GAS PER MONTH(?)


public class AutomobileActivity extends AppCompatActivity {

    private String ACTIVITY_NAME = "AutomobileActivity";

    ArrayList<String> litresArray = new ArrayList<>();
    ArrayList<String> priceArray = new ArrayList<>();
    ArrayList<String> mileageArray = new ArrayList<>();
    ArrayList<String> timeArray = new ArrayList<>();

    private String autoLitres;
    private String autoPrice;
    private String autoMileage;
    private String autoTime;

    private ListView litresListView;
    private ListView priceListView;
    private ListView mileageListView;
    private ListView timeListView;

    private Button autoCreateEntryButton;
    private AutoDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    Cursor cursor;
    private LitresAdapter litresAdapter;
    private PriceAdapter priceAdapter;
    private MileageAdapter mileageAdapter;
    private TimeAdapter timeAdapter;

    private TextView averageGasPriceTextView;
    private TextView totalLitresPurchasedTextView;
    private Button recalculateButton;

    private View parentView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automobile);
        dbHelper  = new AutoDatabaseHelper(this);
        litresAdapter = new LitresAdapter(this);
        priceAdapter = new PriceAdapter(this);
        mileageAdapter = new MileageAdapter(this);
        timeAdapter = new TimeAdapter(this);

        litresListView = (ListView)findViewById(R.id.autoLitresListView);
        litresListView.setAdapter(litresAdapter);
        priceListView = (ListView)findViewById(R.id.autoPriceListView);
        priceListView.setAdapter(priceAdapter);
        mileageListView = (ListView)findViewById(R.id.autoMileageListView);
        mileageListView.setAdapter(mileageAdapter);
        timeListView = (ListView)findViewById(R.id.autoTimeListView);
        timeListView.setAdapter(timeAdapter);

        db = dbHelper.getReadableDatabase();
        cursor = db.query(dbHelper.AUTO_TABLE, dbHelper.Column_Names,
                null, null, null, null, null, null);
        populateArrays();
        notifyAdapters();

        autoCreateEntryButton = (Button)findViewById(R.id.autoCreateEntryButton);
        autoCreateEntryButton.setBackgroundColor(Color.parseColor("#540000"));
        autoCreateEntryButton.setTextColor(Color.WHITE);
        autoCreateEntryButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.i(ACTIVITY_NAME, "Entry button clicked");
                LayoutInflater li= getLayoutInflater();
                final LinearLayout rootTag = (LinearLayout)li.inflate(R.layout.auto_custom_dialog_, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(AutomobileActivity.this);

                builder.setView(rootTag)
                        .setPositiveButton("Confirm Entry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setAutoValues(((EditText)rootTag.findViewById(R.id.autoLitresEditText)).getText().toString(),
                                        ((EditText)rootTag.findViewById(R.id.autoPriceEditText)).getText().toString(),
                                        ((EditText)rootTag.findViewById(R.id.autoMileageEditText)).getText().toString());
                                insertIntoDataBase();
                                addToArrays();
                                notifyAdapters();
                                Snackbar.make(parentView, "Make sure to recalculate your totals", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
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

        litresListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                LayoutInflater li= getLayoutInflater();
                final LinearLayout rootTag = (LinearLayout)li.inflate(R.layout.auto_edit_layout, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(AutomobileActivity.this);
                builder.setView(rootTag)
                        .setPositiveButton("Confirm Entry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                updateDatabaseRow(1,position, ((EditText)rootTag.findViewById(R.id.editDatabaseEditText)).getText().toString());
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
        priceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                LayoutInflater li= getLayoutInflater();
                final LinearLayout rootTag = (LinearLayout)li.inflate(R.layout.auto_edit_layout, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(AutomobileActivity.this);
                builder.setView(rootTag)
                        .setPositiveButton("Confirm Entry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                updateDatabaseRow(2,position, ((EditText)rootTag.findViewById(R.id.editDatabaseEditText)).getText().toString());
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

        mileageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                LayoutInflater li= getLayoutInflater();
                final LinearLayout rootTag = (LinearLayout)li.inflate(R.layout.auto_edit_layout, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(AutomobileActivity.this);
                builder.setView(rootTag)
                        .setPositiveButton("Confirm Entry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                updateDatabaseRow(3,position, ((EditText)rootTag.findViewById(R.id.editDatabaseEditText)).getText().toString());
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
        recalculateButton = (Button)findViewById(R.id.recalculateButton);
        recalculateButton.setBackgroundColor(Color.parseColor("#540000"));
        recalculateButton.setTextColor(Color.WHITE);
        recalculateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //Snackbar.make(view, "Testing snack", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                averageGasPriceTextView = (TextView)findViewById(R.id.avgGasPrice);
                averageGasPriceTextView.setText("$" + calculateAverageGasPrice());
                totalLitresPurchasedTextView = (TextView)findViewById(R.id.gasAmountPurchased);
                totalLitresPurchasedTextView.setText("" + calculateTotalLitres() + " Litres");
            }
        });
        averageGasPriceTextView = (TextView)findViewById(R.id.avgGasPrice);
        totalLitresPurchasedTextView = (TextView)findViewById(R.id.gasAmountPurchased);
        averageGasPriceTextView.setText("$" + calculateAverageGasPrice());
        totalLitresPurchasedTextView.setText("" + calculateTotalLitres() + " Litres");

        parentView = findViewById(R.id.toolBarContent);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void updateDatabaseRow(int columnValue, int position, String adjustedString){

        if(columnValue == 1){ //litres
            autoLitres = adjustedString;
            litresArray.set(position, autoLitres);
        }else if(columnValue == 2){ //price
            autoPrice = adjustedString;
            priceArray.set(position, autoPrice);
        }else{ //mileage
            autoMileage = adjustedString;
            mileageArray.set(position, autoMileage);
        }
        db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("LITRES", autoLitres);
        cv.put("PRICE", autoPrice);
        cv.put("MILEAGE", autoMileage);
        cv.put("TIME", autoTime);
        db.update("Auto_Table", cv,"_ID = " + position, null);

        litresAdapter.notifyDataSetChanged();
        priceAdapter.notifyDataSetChanged();
        mileageAdapter.notifyDataSetChanged();


    }

    private void setAutoValues(String litres, String price, String mileage){
        autoLitres = litres;
        autoPrice = price;
        autoMileage = mileage;
        autoTime = DateFormat.getDateTimeInstance().format(new Date());
        autoTime = autoTime.substring(0, 6);
        Log.i(ACTIVITY_NAME, "LITRES:" + litres + " PRICE:" + price + " MILEAGE:" + mileage + "TIME: " + autoTime);
    }

    private void insertIntoDataBase(){
        db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("LITRES", autoLitres);
        cv.put("PRICE", autoPrice);
        cv.put("MILEAGE", autoMileage);
        cv.put("TIME", autoTime);
        db.insert("Auto_Table","NullColumnName", cv);
    }

    private void addToArrays(){
        cursor.moveToLast();
        litresArray.add(autoLitres);
        priceArray.add(autoPrice);
        mileageArray.add(autoMileage);
        timeArray.add(autoTime);
    }

    private void populateArrays(){
        cursor = db.query(dbHelper.AUTO_TABLE, dbHelper.Column_Names,
                null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                litresArray.add(cursor.getString(1));
                priceArray.add(cursor.getString(2));
                mileageArray.add(cursor.getString(3));
                timeArray.add(cursor.getString(4));

                cursor.moveToNext();
            }
        }
    }

    private void notifyAdapters(){
        litresAdapter.notifyDataSetChanged();
        priceAdapter.notifyDataSetChanged();
        mileageAdapter.notifyDataSetChanged();
        timeAdapter.notifyDataSetChanged();
    }

    private float calculateAverageGasPrice(){
        float avgGas = 0;
        for(int i = 0; i < priceArray.size(); i++){
            avgGas += Float.parseFloat(priceArray.get(i));
        }
        avgGas = avgGas/priceArray.size();
        DecimalFormat df = new DecimalFormat("###.###");
        avgGas = Float.parseFloat(df.format(avgGas));
        return avgGas;
    }
    private float calculateTotalLitres(){
        float totalLitres = 0;
        for(int i = 0; i < litresArray.size(); i++){
            totalLitres += Float.parseFloat(litresArray.get(i));
        }
        return totalLitres;
    }

    public boolean onCreateOptionsMenu(Menu m){
        getMenuInflater().inflate(R.menu.toolbar_menu, m);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem mi){

        AlertDialog.Builder builder = null;

        switch(mi.getItemId()) {
            //ACTION 1
            case R.id.about:
                Log.d("Toolbar", "about");
                Toast t = Toast.makeText(AutomobileActivity.this, "Activity 4(Automobile) v1.0 was created by Lewis Rannells",  Toast.LENGTH_LONG);
                t.show();
                break;

            //ACTION 2
            case R.id.help:
                Log.d("Toolbar", "help");

                builder = new AlertDialog.Builder(this);
                String s1 = "Create an entry by clicking the 'ADD ENTRY' button. ";
                String s2 = "Total litres and the average gas price for the last month is displayed at the bottom the screen. ";
                String s3 = "After adding a new entry you wish to recalculate the bottom values click the 'Recalculate' button. ";
                String s4 = "Items except for the time the data was entered can be edited by clicking on an item in the table";
                builder.setMessage(s1 + "\n" + "\n" + s2 + "\n" + "\n" + s3 + "\n" + "\n" + s4)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                break;
        }
        if (builder != null){
            AlertDialog alert = builder.create();
            builder.show();
        }
        return true;
    }



    private class LitresAdapter extends ArrayAdapter<String> {

        public LitresAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {
            return litresArray.size();
        }

        public String getItem(int position) {
            return litresArray.get(position);
        }

        public View getView(int position, View convertView, ViewGroup Parent) {
            Log.i(ACTIVITY_NAME, "in getView");
            LayoutInflater inflater = AutomobileActivity.this.getLayoutInflater();
            View view;
            view = inflater.inflate(R.layout.auto_database_layout, null);
            TextView litresTextDB = view.findViewById(R.id.textDB);
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

    private class PriceAdapter extends ArrayAdapter<String> {

        public PriceAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {
            return priceArray.size();
        }

        public String getItem(int position) {
            return priceArray.get(position);
        }

        public View getView(int position, View convertView, ViewGroup Parent) {
            Log.i(ACTIVITY_NAME, "in getView");
            LayoutInflater inflater = AutomobileActivity.this.getLayoutInflater();
            View view;
            view = inflater.inflate(R.layout.auto_database_layout, null);
            TextView textDB = view.findViewById(R.id.textDB);
            textDB.setText(getItem(position));
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

    private class MileageAdapter extends ArrayAdapter<String> {

        public MileageAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {
            return mileageArray.size();
        }

        public String getItem(int position) {
            return mileageArray.get(position);
        }

        public View getView(int position, View convertView, ViewGroup Parent) {
            Log.i(ACTIVITY_NAME, "in getView");
            LayoutInflater inflater = AutomobileActivity.this.getLayoutInflater();
            View view;
            view = inflater.inflate(R.layout.auto_database_layout, null);
            TextView textDB = view.findViewById(R.id.textDB);
            textDB.setText(getItem(position));
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

    private class TimeAdapter extends ArrayAdapter<String> {

        public TimeAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {
            return timeArray.size();
        }

        public String getItem(int position) {
            return timeArray.get(position);
        }

        public View getView(int position, View convertView, ViewGroup Parent) {
            Log.i(ACTIVITY_NAME, "in getView");
            LayoutInflater inflater = AutomobileActivity.this.getLayoutInflater();
            View view;
            view = inflater.inflate(R.layout.auto_database_layout, null);
            TextView textDB = view.findViewById(R.id.textDB);
            textDB.setText(getItem(position));
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
