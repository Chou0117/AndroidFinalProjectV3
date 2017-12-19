package com.example.antho.android_final;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

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

    private String autoLitres;
    private String autoPrice;
    private String autoMileage;

    private EditText litresET;
    private EditText priceET;
    private EditText mileageET;

    Button autoCreateEntryButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automobile);
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
    }
}
