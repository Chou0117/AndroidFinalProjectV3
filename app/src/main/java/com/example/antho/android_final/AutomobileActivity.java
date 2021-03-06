package com.example.antho.android_final;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

//GENERAL REQUIREMENTS
//*A Fragment
//*Listview to present items;
//*clicking shows item details
//*Items in the listview must be stored, adding and deletion of items
//*An Asynctask(open a database/retrieve data/save data)
//*Progressbar
//*Button
//*EditText and an associated text input method
//*Toast,
//*Snackbar,
//*custom dialog notification
//*Help menu that shows author, activity number and interface instructions
//*alternate language

//ACTIVITY REQUIREMENTS
//*User is able to select LITRES, PRICE, KILOMETRES
//*entries displayed in a listview
//*database stores the time the information was recorded
//*app should provide AVERAGE GAS PRICE for last month
//*how much gas was purchased LAST MONTH
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
    private ProgressBar progressBar;

    private View parentView;

    private AutoTask task;
    private float avgGas;
    private float totalLitres;


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
                LayoutInflater li= getLayoutInflater();
                final LinearLayout rootTag = (LinearLayout)li.inflate(R.layout.auto_custom_dialog_, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(AutomobileActivity.this);

                builder.setView(rootTag)
                        .setPositiveButton(R.string.autoConfirmEntry, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setAutoValues(((EditText)rootTag.findViewById(R.id.autoLitresEditText)).getText().toString(),
                                        ((EditText)rootTag.findViewById(R.id.autoPriceEditText)).getText().toString(),
                                        ((EditText)rootTag.findViewById(R.id.autoMileageEditText)).getText().toString());
                                insertIntoDataBase();
                                addToArrays();
                                notifyAdapters();
                                StartTask();
                                MakeSnack();
                            }
                        })
                        .setNegativeButton(R.string.autoCancel, new DialogInterface.OnClickListener() {
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
                        .setPositiveButton(R.string.autoConfirmEntry, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                updateDatabaseRow(1,position, ((EditText)rootTag.findViewById(R.id.editDatabaseEditText)).getText().toString());
                                StartTask();
                                MakeSnack();
                            }
                        })
                        .setNegativeButton(R.string.autoCancel, new DialogInterface.OnClickListener() {
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
                        .setPositiveButton(R.string.autoConfirmEntry, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                updateDatabaseRow(2,position, ((EditText)rootTag.findViewById(R.id.editDatabaseEditText)).getText().toString());
                                StartTask();
                                MakeSnack();
                            }
                        })
                        .setNegativeButton(R.string.autoCancel, new DialogInterface.OnClickListener() {
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
                        .setPositiveButton(R.string.autoConfirmEntry, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                updateDatabaseRow(3,position, ((EditText)rootTag.findViewById(R.id.editDatabaseEditText)).getText().toString());
                                StartTask();
                                MakeSnack();
                            }
                        })
                        .setNegativeButton(R.string.autoCancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                builder.show();
            }
        });
        timeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                LayoutInflater li= getLayoutInflater();
                final LinearLayout rootTag = (LinearLayout)li.inflate(R.layout.auto_edit_layout, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(AutomobileActivity.this);
                builder.setView(rootTag)
                        .setPositiveButton(R.string.autoConfirmEntry, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                updateDatabaseRow(4,position, ((EditText)rootTag.findViewById(R.id.editDatabaseEditText)).getText().toString());
                                StartTask();
                                MakeSnack();
                            }
                        })
                        .setNegativeButton(R.string.autoCancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                builder.show();
            }
        });

        StartTask();

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
        }else if (columnValue == 3){ //mileage
            autoMileage = adjustedString;
            mileageArray.set(position, autoMileage);
        }else{
            autoTime = adjustedString;
            timeArray.set(position, autoTime);
        }
        db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("LITRES", autoLitres);
        cv.put("PRICE", autoPrice);
        cv.put("MILEAGE", autoMileage);
        cv.put("TIME", autoTime);
        db.update("Auto_Table", cv,"_ID = " + position, null);

        notifyAdapters();

    }

    private void setAutoValues(String litres, String price, String mileage){
        autoLitres = litres;
        autoPrice = price;
        autoMileage = mileage;
        autoTime = DateFormat.getDateTimeInstance().format(new Date());
        if(autoTime.substring(5,6).equals(",")){
            autoTime = autoTime.substring(0,5) + " ";
        }
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

    public boolean onCreateOptionsMenu(Menu m){
        getMenuInflater().inflate(R.menu.toolbar_menu, m);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem mi){

        AlertDialog.Builder builder = null;

        switch(mi.getItemId()) {
            //ACTION 1
            case R.id.about:
                Toast t = Toast.makeText(AutomobileActivity.this, R.string.autoAbout,  Toast.LENGTH_LONG);
                t.show();
                break;

            //ACTION 2
            case R.id.help:
                builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.autoHelpOne) + "\n" + "\n" + getString(R.string.autoHelpTwo) + "\n" + "\n" + getString(R.string.autoHelpThree) + "\n" + "\n" + getString(R.string.autoHelpFour))
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

    private void StartTask(){
        task = new AutoTask();
        task.execute();

    }
    private void MakeSnack() {
        Snackbar.make(parentView, R.string.autoRecalculateSnack, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private class AutoTask extends AsyncTask<String, Integer, String>{

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar = (ProgressBar)findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected String doInBackground(String... strings) {

            publishProgress(25);

            totalLitres = 0;
            avgGas = 0;
            for(int i = 0; i < priceArray.size(); i++){
                avgGas += Float.parseFloat(priceArray.get(i));
            }
            publishProgress(50);

            avgGas = avgGas / priceArray.size();
            DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
            otherSymbols.setDecimalSeparator('.');
            otherSymbols.setGroupingSeparator(',');
            DecimalFormat df = new DecimalFormat("###.##", otherSymbols);
            avgGas = Float.parseFloat(df.format(avgGas));
            publishProgress(75);

            for(int i = 0; i < litresArray.size(); i++){
                totalLitres += Float.parseFloat(litresArray.get(i));
            }

            publishProgress(100);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.INVISIBLE);
            averageGasPriceTextView = (TextView)findViewById(R.id.avgGasPrice);
            totalLitresPurchasedTextView = (TextView)findViewById(R.id.gasAmountPurchased);
            averageGasPriceTextView.setText("$" + avgGas);
            totalLitresPurchasedTextView.setText("" + totalLitres + " Litres");
        }
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
