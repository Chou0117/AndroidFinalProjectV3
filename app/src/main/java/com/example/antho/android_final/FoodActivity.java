package com.example.antho.android_final;

import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FoodActivity extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "FoodActivity";

    /*
        TODO Database
        TODO Calories, Total Fat, Total Carbohydrate
        TODO Current / Yesterday Average Calories
        TODO Order Sort
    */
    //Frame
    public static boolean mTwoPane;

    //    MessageFragment messageFragment = new MessageFragment(this);
    //Spinner
//    private ArrayAdapter<String> foodTypeAdapter;
//    private Spinner foodTypeDropdown;
    //Input Edit Text
    private EditText foodEditText;
    private EditText calorieIn;
    private EditText fatIn;
    private EditText carbohydrateIn;
    DatePicker datePicker;
    TimePicker timePicker;
    //Add Item Button
    private Button foodAddButton;
    //Array for inputs
    private ArrayList<String> foodInformation = new ArrayList<>();
    private ArrayList<Integer> calorieValue = new ArrayList<>();
    private ArrayList<Integer> fatValue = new ArrayList<>();
    private ArrayList<Integer> carbohydrateValue = new ArrayList<>();
    private ArrayList<String> dateValue = new ArrayList<>();
    //ListView
    private ListView foodWindow;
    private InfoAdapter informationAdapter;
    //Database
    private FoodDatabaseHelper tempDBH;
    private SQLiteDatabase db;
    private ContentValues values = new ContentValues();
    private Cursor cursor;
    LinearLayout rootTag;
    LayoutInflater li;
    AlertDialog.Builder builder1;

    FoodActivity fa = this;
    FoodFragment mf;
    Bundle arg;
    Long iD;
    int rid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(ACTIVITY_NAME, "In onCreate()");
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTitle(R.string.foodActivityTitle);
        setContentView(R.layout.activity_food);


        if (findViewById(R.id.tabletFrame) != null) {
            Log.i("Info", "It's a tablet");
            mTwoPane = true;
        }


//        //Set up food type Spinner
//        foodTypeDropdown = findViewById(R.id.foodTypeSpinner);
//        String[] items = new String[]{"Meal", "Drink", "Snack"};
//        foodTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//        foodTypeDropdown.setAdapter(foodTypeAdapter);

//        //Components
        foodAddButton = findViewById(R.id.foodAddButton);
        foodWindow = findViewById(R.id.foodWindow);
        informationAdapter = new InfoAdapter(this);

        //Database
        tempDBH = new FoodDatabaseHelper(getApplicationContext());
        db = tempDBH.getWritableDatabase();
        cursor = db.query(tempDBH.FOOD_TABLE, tempDBH.Column_Names,
                null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                foodInformation.add(cursor.getString(2));
                calorieValue.add(cursor.getInt(3));
                fatValue.add(cursor.getInt(4));
                carbohydrateValue.add(cursor.getInt(5));
                dateValue.add(cursor.getString(6));
                cursor.moveToNext();
            }
            Log.i(ACTIVITY_NAME, "Cursor’s  column count =" + cursor.getColumnCount());
        }

        foodAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                li = getLayoutInflater();
                rootTag = (LinearLayout) li.inflate(R.layout.food_input, null);
                builder1 = new AlertDialog.Builder(FoodActivity.this);
                builder1.setMessage(R.string.foodActivityAddGreetingMsg)
                        .setView(rootTag)
                        .setNegativeButton(R.string.foodActivityCancelButton, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        })
                        .setPositiveButton(R.string.foodActivityAddButton, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
//                                String foodType = foodTypeDropdown.getSelectedItem().toString();

                                foodEditText = rootTag.findViewById(R.id.foodEditText);
                                String foodName = foodEditText.getText().toString();

                                calorieIn = rootTag.findViewById(R.id.calories);
                                int calorie = Integer.parseInt(calorieIn.getText().toString());

                                fatIn = rootTag.findViewById(R.id.fat);
                                int fat = Integer.parseInt(fatIn.getText().toString());

                                carbohydrateIn = rootTag.findViewById(R.id.carbohydrate);
                                int carbohydrate = Integer.parseInt(carbohydrateIn.getText().toString());

                                datePicker = rootTag.findViewById(R.id.datePicker);

                                int year = datePicker.getYear();
                                String y = "" + year;

                                int month = datePicker.getMonth() + 1;
                                String m = "" + month;
                                if (month < 10) m = "0" + month;

                                int day = datePicker.getDayOfMonth();
                                String d = "" + day;
                                if (day < 10) d = "0" + day;

                                timePicker = rootTag.findViewById(R.id.timePicker);
                                int hour = timePicker.getCurrentHour();
                                String h = "" + hour;
                                if (hour < 10) h = "0" + hour;

                                int minutes = timePicker.getCurrentMinute();
                                String mi = "" + minutes;
                                if (minutes < 10) mi = "0" + minutes;

                                String date = y + "-" + m + "-" + d + "  " + h + ":" + mi;

                                foodInformation.add(foodName);
                                calorieValue.add(calorie);
                                fatValue.add(fat);
                                carbohydrateValue.add(carbohydrate);
                                dateValue.add(date);

//                                values.put(tempDBH.Column_Names[1], foodType);
                                values.put(tempDBH.Column_Names[2], foodName);
                                values.put(tempDBH.Column_Names[3], calorie);
                                values.put(tempDBH.Column_Names[4], fat);
                                values.put(tempDBH.Column_Names[5], carbohydrate);
                                values.put(tempDBH.Column_Names[6], date);
                                db.insert(tempDBH.FOOD_TABLE, null, values);

                                informationAdapter.notifyDataSetChanged();
                                Toast t = Toast.makeText(getApplicationContext(), R.string.foodActivityAddSuccessMsg, Toast.LENGTH_SHORT);
                                t.show();

                                updateStatistic();
                            }
                        });
                builder1.create().show();

            }
        });

        foodWindow.setAdapter(informationAdapter);


        foodWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {


                iD = id;
                arg = new Bundle();
                arg.putString(tempDBH.Column_Names[0], "" + id);
                arg.putString(tempDBH.Column_Names[2], informationAdapter.getItem(position, 2));
                arg.putString(tempDBH.Column_Names[3], informationAdapter.getItem(position, 3));
                arg.putString(tempDBH.Column_Names[4], informationAdapter.getItem(position, 4));
                arg.putString(tempDBH.Column_Names[5], informationAdapter.getItem(position, 5));
                arg.putString(tempDBH.Column_Names[6], informationAdapter.getItem(position, 6));

                if (mTwoPane) {

                    Log.i("Info", "Item Clicked in Tablet");
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    mf = new FoodFragment();
                    mf.setParentActivity(fa);
                    mf.setArguments(arg);
                    transaction.replace(R.id.tabletFrame, mf);
                    transaction.commit();

                } else {
                    Log.i("Info", "Show this if using phone");
                    Intent intent = new Intent(FoodActivity.this, FoodDetail.class);
                    intent.putExtras(arg);
                    startActivityForResult(intent, 999);
                }

            }
        });

        /* Tool Bar */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        updateStatistic();
    }

    public void updateStatistic() {
        Calendar td = Calendar.getInstance();
        td.add(Calendar.DATE,0);
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        String today = dateFormat1.format(td.getTime());

        Calendar yd = Calendar.getInstance();
        yd.add(Calendar.DATE, -1);
        String yesterday = dateFormat1.format(yd.getTime());
        int totalCalories = 0;
        int totalFat = 0;
        int totalCarbohydrate = 0;
        int yesterdaysTotal = 0;
        double everydayAVG = 0;

        cursor = db.query(tempDBH.FOOD_TABLE, tempDBH.Column_Names,
                null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String date = cursor.getString(6).substring(0, 10).trim();
            if (date.equals(today)) {
                totalCalories += cursor.getInt(3);

                totalFat += cursor.getInt(4);
                totalCarbohydrate += cursor.getInt(5);
            }
            if (date.equals(yesterday)) {
                yesterdaysTotal += cursor.getInt(3);
            }
            cursor.moveToNext();
        }

        TextView todayCal = findViewById(R.id.foodTotalCalorie);
        todayCal.setText("" + totalCalories);

        TextView todayFat = findViewById(R.id.foodTotalFat);
        todayFat.setText("" + totalFat);

        TextView todayCar = findViewById(R.id.foodTotalCarbohydrate);
        todayCar.setText("" + totalCarbohydrate);

        TextView yesterdayCal = findViewById(R.id.calorieslabelTotalYesterday);
        yesterdayCal.setText("" + yesterdaysTotal);

        String query = "SELECT strftime('%Y-%m-%d',"+tempDBH.Column_Names[6]+") as day,AVG("+tempDBH.Column_Names[3] +") FROM "+tempDBH.FOOD_TABLE+" group by day";
//        String query = "SELECT strftime('%Y-%m-%d'," + tempDBH.Column_Names[6] + ") as day FROM " + tempDBH.FOOD_TABLE;
//        String query = "SELECT strftime('%Y-%m-%d',"+tempDBH.Column_Names[6]+") as day FROM " + tempDBH.FOOD_TABLE;
        cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Log.i("T everyDay AVG Day", "" + cursor.getString(0));
            Log.i("T everyDay AVG Cal",""+cursor.getString(1));
            everydayAVG += Double.parseDouble(cursor.getString(1));
            cursor.moveToNext();
        }
        int days = cursor.getCount();

        Log.i("T everyDay AVG Clc", "" + cursor.getColumnCount());
        Log.i("T everyDay AVG Cln", "" + cursor.getColumnName(0));
        Log.i("T everyDay AVG Cln",""+cursor.getColumnName(1));
        Log.i("T everyDay AVG Cnt", "" + cursor.getCount());

        DecimalFormat df = new DecimalFormat("#.00");

        TextView calAVG = findViewById(R.id.caloriesAvg);
        calAVG.setText("" + df.format(everydayAVG/days));
    }

    //Item Deleting
    public void deleteItem(int i) {
        db.execSQL("delete from " + tempDBH.FOOD_TABLE + " where " + tempDBH.Column_Names[0] + " =" + i + ";");

        getFragmentManager().beginTransaction().remove(mf).commit();
        recreate();
    }

    //Item Editing
    public void editItem() {

        getFragmentManager().beginTransaction().remove(mf).commit();
        recreate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Delete", "" + requestCode);
        Log.i("Delete", "Item Deleting");
        if (requestCode == 999) {
            if (resultCode > 0) {
                rid = resultCode - 1;
                deleteItem(rid);
                updateStatistic();
            }

        }

        recreate();
    }

    private class InfoAdapter extends ArrayAdapter<String> {

        public InfoAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {
            return foodInformation.size();
        }

        @Override
        public long getItemId(int position) {

            cursor = db.query(tempDBH.FOOD_TABLE, tempDBH.Column_Names,
                    null, null, null, null, null, null);
            cursor.moveToPosition(position);
            Log.i("Position", "" + position);
//            cursor.getString(0);
            return Long.parseLong(cursor.getString(0));
        }

        public String getItem(int position, int column) {
            cursor = db.query(tempDBH.FOOD_TABLE, tempDBH.Column_Names,
                    null, null, null, null, null, null);
            cursor.moveToPosition(position);
            return cursor.getString(column);
        }

        public View getView(int position, View convertView, ViewGroup Parent) {
            LayoutInflater inflater = FoodActivity.this.getLayoutInflater();
            View result;
            result = inflater.inflate(R.layout.food_item_row, null);

//            TextView foodItemType = result.findViewById(R.id.food_item_type_text);
//            foodItemType.setText(foodTypeDropdown.getSelectedItem().toString() + ":  ");

            TextView foodItem = result.findViewById(R.id.food_item_info_text);
            foodItem.setText(getItem(position, 2));

            TextView calItem = result.findViewById(R.id.cal_item_info_text);
            calItem.setText(getItem(position, 3));
            TextView fatItem = result.findViewById(R.id.fat_item_info_text);
            fatItem.setText(getItem(position, 4));
            TextView carItem = result.findViewById(R.id.car_item_info_text);
            carItem.setText(getItem(position, 5));

            TextView time = result.findViewById(R.id.food_item_time);
            time.setText(getItem(position, 6));

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
//        db.close();
    }

    public boolean onCreateOptionsMenu(Menu m) {
        getMenuInflater().inflate(R.menu.food_toolbar_menu, m);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem mi) {

        AlertDialog.Builder instruction;
        Intent intent;
        switch (mi.getItemId()) {

            case R.id.ActivityAct:
                intent = new Intent(FoodActivity.this, ActivityActivity.class);
                startActivity(intent);
                break;
            case R.id.ThermoAct:
                intent = new Intent(FoodActivity.this, ThermostatActivity.class);
                startActivity(intent);
                break;
            case R.id.AutoAct:
                intent = new Intent(FoodActivity.this, AutomobileActivity.class);
                startActivity(intent);
                break;

            case R.id.info:
                Toast toast = Toast.makeText(FoodActivity.this, R.string.foodToolbarAboutMsg, Toast.LENGTH_LONG);
                toast.show();
                break;

            case R.id.help:

                instruction = new AlertDialog.Builder(this);
//                String s1 = "You eat....";
//                String s2 = "You track....";
//                String s3 = "If not....";
//                String s4 = "You fat....";
                instruction.setMessage("You eat...." + "\n" + "\n" + "You track...." + "\n" + "\n" + "If not...." + "\n" + "\n" + "You fat....")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = instruction.create();
                instruction.show();
                break;
        }
//        if (instruction != null){
//            AlertDialog alert = instruction.create();
//            instruction.show();
//        }
        return true;
    }
}
