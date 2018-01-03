package com.example.antho.android_final;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;

/**
 * Created by camer on 2018-01-02.
 */

public class ThermostatFragment extends Fragment {
    String Day, Time, Temp, ID;
    Button saveBtn, saveBtnNw;
    EditText thermostatTemp;
    Spinner thermostatDay;
    TimePicker thermostatTime;
    View myView;
    ThermostatFragment thermoDatabase;
    ThermostatActivity thermostatActivity;

    public ThermostatFragment() {
    }

    public static ThermostatFragment newInstance(ThermostatActivity thermoActivity) {
        ThermostatFragment fragment = new ThermostatFragment();
        Bundle args = new Bundle();
        fragment.thermostatActivity = thermoActivity;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.thermostat_fragment, container, false);

        saveBtn = (Button) myView.findViewById(R.id.button_save);
        saveBtnNw = (Button) myView.findViewById(R.id.button_new_save);
        thermostatTemp = (EditText) myView.findViewById(R.id.thermostatTempatureF);
        thermostatDay = (Spinner) myView.findViewById(R.id.thermostatDayF);
        thermostatTime = (TimePicker) myView.findViewById(R.id.thermostatTimeF);

        Bundle bundle = this.getArguments();
        ID = bundle.getString("Id");
        Day = bundle.getString("Day");
        Time = bundle.getString("Time");
        Temp = bundle.getString("Temp");


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ThermostatActivity.class);
                    getActivity().setResult(Integer.parseInt(ID), intent);
                    getActivity().finish();
            }
        });

        thermostatDay.setSelection(0);
        thermostatTime.setCurrentHour(0);
        thermostatTemp.setText(Temp);

        return myView;
    }
}