package com.example.antho.android_final;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FoodActivity extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "FoodActivity";

/*
    TODO Database
    TODO Calories, Total Fat, Total Carbohydrate
    TODO Current / Yesterday Average Calories
    TODO Order Sort
*/

    //Spinner
    ArrayAdapter<String> foodTypeAdapter;
    Spinner foodTypeDropdown;
    //Input Edit Text
    EditText foodEditText;
    //Add Item Button
    Button foodAddButton;
    //ListView
    ArrayList<String> foodInformation = new ArrayList<>();
    ListView foodWindow;
    InfoAdapter informationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Food Nutrition Information Tracker");
        Log.i(ACTIVITY_NAME, "In onCreate()");
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_food);


        //Set up food type Spinner
        foodTypeDropdown = findViewById(R.id.foodTypeSpinner);
        String[] items = new String[]{"Meal", "Drink", "Snack"};
        foodTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        foodTypeDropdown.setAdapter(foodTypeAdapter);

        foodEditText = findViewById(R.id.foodEditText);
        foodAddButton = findViewById(R.id.foodAddButton);
        foodWindow = findViewById(R.id.foodWindow);
        informationAdapter = new InfoAdapter(this);

        foodAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputMSG = foodEditText.getText().toString();
                foodInformation.add(inputMSG);

//                values.put(tempDBH.Column_Names[1], inputMSG);
//                db.insert(tempDBH.MESSAGE_TABLE, null, values);

                informationAdapter.notifyDataSetChanged();
                foodEditText.setText("");
            }
        });

        foodWindow.setAdapter(informationAdapter);
    }

    private class InfoAdapter extends ArrayAdapter<String> {

        public InfoAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {
            return foodInformation.size();
        }

        public String getItem(int position) {
            return foodInformation.get(position);
        }

        public View getView(int position, View convertView, ViewGroup Parent) {
            LayoutInflater inflater = FoodActivity.this.getLayoutInflater();
            View result;
            result = inflater.inflate(R.layout.food_item_row, null);

            TextView foodItemType = result.findViewById(R.id.food_item_type_text);
            foodItemType.setText(foodTypeDropdown.getSelectedItem().toString()+":  ");

            TextView foodItem = result.findViewById(R.id.food_item_info_text);
            foodItem.setText(getItem(position));

            TextView time = result.findViewById(R.id.food_item_time);
            long currentTime = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd  HH:mm");
            Date netDate = (new Date(currentTime));
            time.setText(sdf.format(netDate));

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
}
