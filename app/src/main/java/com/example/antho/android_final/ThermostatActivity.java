package com.example.antho.android_final;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class  ThermostatActivity extends AppCompatActivity {

    private ArrayList<String> thermostatTempAr = new ArrayList<>();
    private ArrayList<String> thermostatDayAr = new ArrayList<>();
    private ArrayList<String> thermostatTimeAr = new ArrayList<>();
    private String ACTIVITY_NAME = "ThermostatActivity";
    private Toolbar mTopToolbar;
    private ThermostatDatabaseHelper thermodbHelper;
    private SQLiteDatabase thermodb;
    private ListView thermostatList;
    private ThermoInfoAdapter informationAdapter;
    private Cursor cursor;
    private String inputDay = "";
    private String inputTimeH = "";
    private String inputTimeM = "";
    private String inputTemp = "";
    private String timeformat = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("House Thermostat");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thermostat);

        thermostatList = (ListView) findViewById(R.id.thermostat_list);

        informationAdapter = new ThermoInfoAdapter(this);
        thermostatList.setAdapter(informationAdapter);
        thermodbHelper = new ThermostatDatabaseHelper(this);
        thermodb = thermodbHelper.getWritableDatabase();
        final ContentValues cValues = new ContentValues();

        cursor = thermodb.query(thermodbHelper.TABLE_NAME, thermodbHelper.Column_names,null, null, null, null, null, null);
        int colIndexDA= cursor.getColumnIndex(ThermostatDatabaseHelper.KEY_DAY);
        int colIndexTI = cursor.getColumnIndex(ThermostatDatabaseHelper.KEY_TIME);
        int colIndexTE = cursor.getColumnIndex(ThermostatDatabaseHelper.KEY_TEMPATURE);


        mTopToolbar = (Toolbar) findViewById(R.id.appToolbar);
        setSupportActionBar(mTopToolbar);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String messageDA = cursor.getString(colIndexDA);
            String messageTI = cursor.getString(colIndexTI);
            String messageTE = cursor.getString(colIndexTE);
            thermostatDayAr.add(messageDA);
            thermostatTimeAr.add(messageTI);
            thermostatTempAr.add(messageTE);
            cursor.moveToNext();
        }

        thermostatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String msg = (String) informationAdapter.getItem(position);
                long msgId = informationAdapter.getItemId(position);
                String messageId = String.valueOf(msgId);

                    Intent intent = new Intent(ThermostatActivity.this, ThermostatDetails.class);
                    intent.putExtra("message", msg);
                    intent.putExtra("messageId", messageId);
                    startActivityForResult(intent, 10);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.thermostat_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        AlertDialog.Builder builder = null;

        if (id == R.id.about) {
            Toast.makeText(ThermostatActivity.this, "Version 1.3 By: Cameron O'Connor", Toast.LENGTH_LONG).show();
            return true;
        }

        if (id == R.id.instructions) {
            Log.d("Toolbar", "instructions");

            builder = new AlertDialog.Builder(this);
            String s1 = "Instructions for House Thermostat.";
            String s2 = "To add a new schedule for the thermostat, click on the plus sign on the toolbar. ";
            String s3 = "You can edit existing schedules by clicking on them. ";
            String s4 = "You may also save new schedules directly from existing ones. ";
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

        if (id == R.id.add) {
            Log.i(ACTIVITY_NAME, "add button clicked");
            LayoutInflater layoutInflater = getLayoutInflater();
            final LinearLayout rootTag = (LinearLayout) layoutInflater.inflate(R.layout.thermostat_custom_dialog, null);
            final Spinner etEnteredDay = (Spinner) rootTag.findViewById(R.id.thermostatDay);
            final TimePicker etEnteredTime = (TimePicker) rootTag.findViewById(R.id.thermostatTime);
            final EditText etEnteredTemp = (EditText) rootTag.findViewById(R.id.thermostatTempature);
            builder = new AlertDialog.Builder(ThermostatActivity.this);
            builder.setView(rootTag)
                    .setPositiveButton("Save Schedule", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            inputDay = etEnteredDay.getSelectedItem().toString();
                            thermostatDayAr.add(inputDay);

                            inputTimeH = etEnteredTime.getCurrentHour().toString();
                            inputTimeM = etEnteredTime.getCurrentMinute().toString();
                            String am_pm = (etEnteredTime.getCurrentHour() < 12) ? "AM" : "PM";
                            timeformat = (" " + inputTimeH + ":" + inputTimeM + " " + am_pm);
                            thermostatTimeAr.add(timeformat);

                            inputTemp = etEnteredTemp.getText().toString();
                            thermostatTempAr.add(inputTemp);

                            //inserts the new message into the database
                            ContentValues cValues = new ContentValues();
                            cValues.put(thermodbHelper.KEY_DAY, inputDay);
                            cValues.put(thermodbHelper.KEY_TIME, timeformat);
                            cValues.put(thermodbHelper.KEY_TEMPATURE, inputTemp);
                            thermodb.insert(thermodbHelper.TABLE_NAME,"null", cValues);

                            informationAdapter.notifyDataSetChanged();

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });

            builder.show();
            thermostatList = findViewById(R.id.thermostat_list);
            thermostatList.setAdapter(informationAdapter);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class ThermoInfoAdapter extends ArrayAdapter<String> {
        LayoutInflater inflater;

        public ThermoInfoAdapter(Context ctx) {
            super(ctx, 0);
            inflater = ThermostatActivity.this.getLayoutInflater();
        }

        public int getCount() {return thermostatTempAr.size();}

        public int getCountDay() {return thermostatDayAr.size();}

        public int getCountTime() {return thermostatTimeAr.size();}

        public int getCountTemp() {return thermostatTempAr.size();}

        public String getItemDay(int position) {return thermostatDayAr.get(position);}

        public String getItemTime(int position) {return thermostatTimeAr.get(position);}

        public String getItemTemp(int position) { return thermostatTempAr.get(position); }

        public View getView(int position, View convertView, ViewGroup Parent) {
            View result = convertView;
            result = inflater.inflate(R.layout.thermostat_item_row, null);

            TextView Day = result.findViewById(R.id.thermostat_day);
            Day.setText("Date: " + getItemDay(position));

            TextView Time = result.findViewById(R.id.thermostat_time);
            Time.setText(getItemTime(position));

            TextView Tempature = result.findViewById(R.id.thermostat_temp);
            Tempature.setText(getItemTemp(position));

            return result;
        }
    }

    @Override
    public void onResume() {
        Log.i(ACTIVITY_NAME, "In onResume()");
        super.onResume();
    }

    @Override
    public void onStart() {
        Log.i(ACTIVITY_NAME, "In onStart()");
        super.onStart();
    }

    @Override
    public void onPause() {
        Log.i(ACTIVITY_NAME, "In onPause()");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i(ACTIVITY_NAME, "In onStop()");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.i(ACTIVITY_NAME, "In onDestroy()");
        super.onDestroy();
        cursor.close();
        thermodb.close();
    }
}
