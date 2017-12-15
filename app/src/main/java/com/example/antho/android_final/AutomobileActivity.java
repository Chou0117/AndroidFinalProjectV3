package com.example.antho.android_final;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
    Button addEntryButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automobile);

        addEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(ACTIVITY_NAME, "Entry button clicked");
            }
        });
    }
}
