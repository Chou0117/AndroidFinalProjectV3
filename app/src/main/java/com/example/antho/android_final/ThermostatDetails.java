package com.example.antho.android_final;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

/**
 * Created by camer on 2018-01-02.
 */

public class ThermostatDetails extends Activity {
    Bundle arg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thermostat_details);

        arg = getIntent().getExtras();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        ThermostatFragment tf = new ThermostatFragment();

        tf.setArguments(arg);
        transaction.replace(R.id.fragment_container, tf);
        transaction.commit();
    }
}