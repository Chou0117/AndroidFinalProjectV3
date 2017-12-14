package com.example.antho.android_final;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//GENERAL REQUIREMENTS
//A Fragment
//Listview to present items; clicking shows item details
//Items in the listview must be stored, adding and deletion of items
//An Asynctask(open a database/retrieve data/save data)
//Progressbar
//Button
//EditText and an associated text input method
//Toast, Snackbar, custom dialog notification
//Help menu that shows author, activity number and interface instructions
//alternate language 

//ACTIVITY REQUIREMENTS
//Lewis Last Edit on Dec 9th
//User is able to select LITRES, PRICE, KILOMETRES
//entries displayed in a listview
//database stores the time the information was recorded
//app should provide AVERAGE GAS PRICE for last month
//how much gas was purchased LAST MONTH
//AVERAGE GAS PER MONTH(?)

public class AutomobileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automobile);
    }
}
