package com.example.antho.android_final;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Activity_MessageDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__message_details);

        Bundle args = getIntent().getExtras();
        Activity_PastRecord ap = new Activity_PastRecord();

        ActivityFragment af = new ActivityFragment(ap);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        af.setArguments(args);
        ft.replace(R.id.message_frame, af);
        ft.commit();
    }
}
