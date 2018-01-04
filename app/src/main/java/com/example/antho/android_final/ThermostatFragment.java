package com.example.antho.android_final;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;

import static com.example.antho.android_final.ThermostatActivity.mTwoPane;
import static com.example.antho.android_final.ThermostatDatabaseHelper.KEY_ID;

/**
 * Created by camer on 2018-01-02.
 */

public class ThermostatFragment extends Fragment {
    String totaltime, num1, num2, hold1, hold2;
    String[] time;
    Long idforhold;
    Integer Day = 0;
    Integer Num01, Num02;
    Button saveBtn, saveBtnNw, deleteBtn;
    EditText thermostatTemp;
    Spinner thermostatDay;
    TimePicker thermostatTime;
    View myView;
    Bundle arg;
    private ThermostatDatabaseHelper thermodbHelper;
    private SQLiteDatabase thermodb;

    public ThermostatFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        arg = getArguments();
        myView = inflater.inflate(R.layout.thermostat_fragment, container, false);

        Long ke;
        String da = "No Day";
        String ti = "No Time";
        String te = "No Tempature";

        if (arg != null) {
            ke = arg.getLong("KEY");
            idforhold = ke;
            da = arg.getString("DAY");
            if (da.equals("Monday")) {
                Day = 0;
            }
            if (da.equals("Tuesday")) {
                Day = 1;
            }
            if (da.equals("Wednesday")) {
                Day = 2;
            }
            if (da == "Thursday") {
                Day = 3;
            }
            if (da == "Friday") {
                Day = 4;
            }
            if (da == "Saturday") {
                Day = 5;
            }
            if (da.equals("Sunday")) {
                Day = 6;
            }
            ti = arg.getString("TIME");
            time = ti.split(":");
            num1 = time[0];
            Num01 = Integer.parseInt(num1);
            num2 = time[1];
            Num02 = Integer.parseInt(num2);

            te = arg.getString("TEMPATURE");
        }

        saveBtn = (Button) myView.findViewById(R.id.button_save);
        saveBtnNw = (Button) myView.findViewById(R.id.button_new_save);
        deleteBtn = (Button) myView.findViewById(R.id.button_delete);

        thermostatDay = (Spinner) myView.findViewById(R.id.thermostatDayF);
        thermostatDay.setSelection(Day);
        thermostatTime = (TimePicker) myView.findViewById(R.id.thermostatTimeF);
        thermostatTime.setCurrentHour(Num01);
        thermostatTime.setCurrentMinute(Num02);
        thermostatTemp = (EditText) myView.findViewById(R.id.thermostatTempatureF);
        thermostatTemp.setText(te);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thermodbHelper = new ThermostatDatabaseHelper(getActivity());
                thermodb = thermodbHelper.getWritableDatabase();

                hold1 = thermostatTime.getCurrentHour().toString();
                hold2 = thermostatTime.getCurrentMinute().toString();
                hold2 = ("00" + hold2).substring(hold2.length());
                totaltime = (hold1 + ":" + hold2);

                ContentValues cValues = new ContentValues();
                cValues.put(thermodbHelper.KEY_DAY, thermostatDay.getSelectedItem().toString());
                cValues.put(thermodbHelper.KEY_TIME, totaltime);
                cValues.put(thermodbHelper.KEY_TEMPATURE, thermostatTemp.getText().toString());

                thermodb.update(thermodbHelper.TABLE_NAME, cValues, "_id = " + idforhold, null);

                if (!mTwoPane)
                    getActivity().finish();
                else {
                    //thermostatActivity.saveItem();
                }
            }
        });

        saveBtnNw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thermodbHelper = new ThermostatDatabaseHelper(getActivity());
                thermodb = thermodbHelper.getWritableDatabase();

                hold1 = thermostatTime.getCurrentHour().toString();
                hold2 = thermostatTime.getCurrentMinute().toString();
                hold2 = ("00" + hold2).substring(hold2.length());
                totaltime = (hold1 + ":" + hold2);

                ContentValues cValues = new ContentValues();
                cValues.put(thermodbHelper.KEY_DAY, thermostatDay.getSelectedItem().toString());
                cValues.put(thermodbHelper.KEY_TIME, totaltime);
                cValues.put(thermodbHelper.KEY_TEMPATURE, thermostatTemp.getText().toString());

                thermodb.insert(thermodbHelper.TABLE_NAME, "null", cValues);

                if (!mTwoPane)
                    getActivity().finish();
                else {
//                    thermostatActivity.saveNewItem();
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thermodbHelper = new ThermostatDatabaseHelper(getActivity());
                thermodb = thermodbHelper.getWritableDatabase();

                thermodb.delete(thermodbHelper.TABLE_NAME, "_id = " + idforhold, null);

                if (!mTwoPane)
                    getActivity().finish();
                else {
//                    thermostatActivity.deleteItem();
                }
            }
        });

        return myView;
    }
}
