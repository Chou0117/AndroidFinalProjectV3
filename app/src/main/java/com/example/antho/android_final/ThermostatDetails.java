package com.example.antho.android_final;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by camer on 2018-01-02.
 */

public class ThermostatDetails extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thermostat_details);

        String ID = getIntent().getStringExtra("Id");
        String Day = getIntent().getStringExtra("Day");
        String Time = getIntent().getStringExtra("Time");
        String Temp = getIntent().getStringExtra("Temp");
        Bundle bundle = new Bundle();
        bundle.putString("Id", ID);
        bundle.putString("Day", Day);
        bundle.putString("Time", Time);
        bundle.putString("Temp", Temp);

        ThermostatFragment messageFragment = new ThermostatFragment();
        messageFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().add(R.id.fragment_container, messageFragment).commit();
    }
}