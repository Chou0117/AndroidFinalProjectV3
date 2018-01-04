package com.example.antho.android_final;

import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;

public class ThermostatActivity extends AppCompatActivity {

    public static boolean mTwoPane;
    Context ctx = this;
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
    private String messageDA, messageTE, messageTI;
    private ProgressBar progressbar;
    private ThermostatFragment thermostatFragment;
    private ThermostatActivity ta = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle(R.string.thermostatTitle);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thermostat);

        thermostatList = (ListView) findViewById(R.id.thermostat_list);

        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        progressbar.bringToFront();

        //Fragments
        //layout-sw600dp/
        if (findViewById(R.id.tabletFrame) != null) {
            Log.i("Info", "It's a tablet. Poor you.");
            mTwoPane = true;
        } else {
            Log.i("Info", "It's a phone. Congratulations!");
            mTwoPane = false;
        }

        informationAdapter = new ThermoInfoAdapter(this);
        thermostatList.setAdapter(informationAdapter);
        thermodbHelper = new ThermostatDatabaseHelper(this);
        thermodb = thermodbHelper.getWritableDatabase();
        final ContentValues cValues = new ContentValues();

        cursor = thermodb.query(thermodbHelper.TABLE_NAME, thermodbHelper.Column_names, null, null, null, null, null, null);
        int colIndexDA = cursor.getColumnIndex(ThermostatDatabaseHelper.KEY_DAY);
        int colIndexTI = cursor.getColumnIndex(ThermostatDatabaseHelper.KEY_TIME);
        int colIndexTE = cursor.getColumnIndex(ThermostatDatabaseHelper.KEY_TEMPATURE);

        mTopToolbar = (Toolbar) findViewById(R.id.appToolbar);
        setSupportActionBar(mTopToolbar);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            messageDA = cursor.getString(colIndexDA);
            messageTI = cursor.getString(colIndexTI);
            messageTE = cursor.getString(colIndexTE);
            thermostatDayAr.add(messageDA);
            thermostatTimeAr.add(messageTI);
            thermostatTempAr.add(messageTE);
            cursor.moveToNext();
        }

        thermostatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle arg = new Bundle();
                arg.putLong("KEY", informationAdapter.getItemId(position));
                arg.putString("DAY", informationAdapter.getItemDay(position));
                arg.putString("TIME", informationAdapter.getItemTime(position));
                arg.putString("TEMPATURE", informationAdapter.getItemTemp(position));

                if (mTwoPane) {
                    Log.i("Info", "Item Clicked in Tablet");
                    thermostatFragment = new ThermostatFragment(ta);
                    thermostatFragment.setArguments(arg);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.tabletFrame, thermostatFragment);
                    transaction.commit();

                } else {
                    Log.i("Info", "Show this if using phone");
                    Intent intent = new Intent(ThermostatActivity.this, ThermostatDetails.class);
                    intent.putExtras(arg);
                    startActivityForResult(intent, 999);
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("IMPORATNTATA", "YOU MADE IT HERE");
        if (resultCode == 0) {
            Log.i("IMPORATNTATA", "YOU MADE IT HERE AS TABLET");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //stuff that updates ui
                    informationAdapter.notifyDataSetChanged();
                }
            });
        } else {
            recreate();
        }
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

        Intent intent;
        switch (id) {

            case R.id.ActivityAct:
                intent = new Intent(ThermostatActivity.this, ActivityActivity.class);
                startActivity(intent);
                break;
            case R.id.FoodAct:
                intent = new Intent(ThermostatActivity.this, FoodActivity.class);
                startActivity(intent);
                break;
            case R.id.AutoAct:
                intent = new Intent(ThermostatActivity.this, AutomobileActivity.class);
                startActivity(intent);
                break;
        }

        if (id == R.id.about) {
            Toast.makeText(ThermostatActivity.this, getString(R.string.thermostatTitle) + "\n" + getString(R.string.thermostatVersion) + "\n" + getString(R.string.thermostatCreator), Toast.LENGTH_LONG).show();
            return true;
        }

        if (id == R.id.instructions) {
            Log.d("Toolbar", "instructions");

            builder = new AlertDialog.Builder(this);
            String s1 = getString(R.string.thermoString1);
            String s2 = getString(R.string.thermoString2);
            String s3 = getString(R.string.thermoString3);
            String s4 = getString(R.string.thermoString4);
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
                    .setPositiveButton(getString(R.string.thermobutton_schedule), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            inputDay = etEnteredDay.getSelectedItem().toString();
                            thermostatDayAr.add(inputDay);

                            inputTimeH = etEnteredTime.getCurrentHour().toString();
                            inputTimeM = etEnteredTime.getCurrentMinute().toString();
                            inputTimeM = ("00" + inputTimeM).substring(inputTimeM.length());
                            timeformat = (inputTimeH + ":" + inputTimeM);
                            thermostatTimeAr.add(timeformat);

                            inputTemp = etEnteredTemp.getText().toString();
                            thermostatTempAr.add(inputTemp);

                            WriteTask write = new WriteTask(ctx);
                            write.execute();

                            if (findViewById(R.id.tabletFrame) == null) {
                                Snackbar snackbar = Snackbar.make(findViewById(R.id.thermostatlayout), getString(R.string.thermoSuccess), Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            } else {
                                Snackbar snackbar = Snackbar.make(findViewById(R.id.thermostatlayout600), getString(R.string.thermoSuccess), Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            }
                        }
                    })
                    .setNegativeButton(getString(R.string.thermobutton_Cancel), new DialogInterface.OnClickListener() {
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

   public class ThermoInfoAdapter extends ArrayAdapter<String> {
        LayoutInflater inflater;

        public ThermoInfoAdapter(Context ctx) {
            super(ctx, 0);
            inflater = ThermostatActivity.this.getLayoutInflater();
        }

        public int getCount() {
            return thermostatTempAr.size();
        }

        public int getCountDay() {
            return thermostatDayAr.size();
        }

        public int getCountTime() {
            return thermostatTimeAr.size();
        }

        public int getCountTemp() {
            return thermostatTempAr.size();
        }

        @Override
        public long getItemId(int position) {

            cursor = thermodb.query(thermodbHelper.TABLE_NAME, thermodbHelper.Column_names,
                    null, null, null, null, null, null);
            cursor.moveToPosition(position);
            Log.i("Position", "" + position);
//            cursor.getString(0);
            return Long.parseLong(cursor.getString(0));
        }

        public String getItemDay(int position) {
            return thermostatDayAr.get(position);
        }

        public String getItemTime(int position) {
            return thermostatTimeAr.get(position);
        }

        public String getItemTemp(int position) {
            return thermostatTempAr.get(position);
        }

        public View getView(int position, View convertView, ViewGroup Parent) {
            View result = convertView;
            result = inflater.inflate(R.layout.thermostat_item_row, null);

            TextView Day = result.findViewById(R.id.thermostat_day);
            Day.setText(getString(R.string.thermoDate) + " " + getItemDay(position) + "  ");

            TextView Time = result.findViewById(R.id.thermostat_time);
            Time.setText(getItemTime(position));

            TextView Tempature = result.findViewById(R.id.thermostat_temp);
            Tempature.setText(getItemTemp(position));

            return result;
        }
    }

    public void updateListView(){
        Log.i("updateListView", "Inside the updateListView");
        recreate();
    }

    public class WriteTask extends AsyncTask<String, Void, String> {
        Context ctx;

        WriteTask(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        protected String doInBackground(String... params) {

            thermodbHelper = new ThermostatDatabaseHelper(getApplicationContext());
            thermodb = thermodbHelper.getWritableDatabase();

            try {
                //inserts the new message into the database
                ContentValues cValues = new ContentValues();
                cValues.put(thermodbHelper.KEY_DAY, inputDay);
                cValues.put(thermodbHelper.KEY_TIME, timeformat);
                cValues.put(thermodbHelper.KEY_TEMPATURE, inputTemp);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //stuff that updates ui
                        informationAdapter.notifyDataSetChanged();
                    }
                });

                try {
                    thermodb.insert(thermodbHelper.TABLE_NAME, "null", cValues);
                    Thread.sleep(300);
                    onPostExecute();
                } catch (Exception e) {
                    Log.i(this.toString(), "Error inserting values in database");
                }

            } finally {

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            // SHOW THE SPINNER WHILE LOADING FEEDS
                progressbar.setVisibility(View.VISIBLE);
        }

        protected void onPostExecute() {
            // HIDE THE SPINNER AFTER LOADING FEEDS
            progressbar.setVisibility(View.INVISIBLE);
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
