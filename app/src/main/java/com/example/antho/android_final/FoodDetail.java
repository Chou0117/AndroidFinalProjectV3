package com.example.antho.android_final;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FoodDetail extends Activity {
    Bundle arg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        arg = getIntent().getExtras();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        FoodFragment mf = new FoodFragment();

        mf.setArguments(arg);
        transaction.replace(R.id.phoneFrame, mf);
        transaction.commit();
    }
}
