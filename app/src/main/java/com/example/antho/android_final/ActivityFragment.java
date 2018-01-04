package com.example.antho.android_final;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;


public class ActivityFragment extends android.app.Fragment {

    Activity_PastRecord apr;
    String timeStamp;
    Button saveButton;
    EditText timeView;
    EditText commentView;
    EditText timeStampView;
    ActivityDatabaseHelper dbHelper;
    Spinner dropdown;
    ArrayAdapter<String> adapter;
    String[] items = new String[]{"Running", "Walking", "Skating", "Swimming", "Biking"};


    public ActivityFragment() {
        // Required empty public constructor
    }

    public ActivityFragment(Activity_PastRecord apr) {
        this.apr = apr;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_activity, container, false);

        dropdown= v.findViewById(R.id.spinner1); dropdown.setEnabled(false);
        timeView = v.findViewById(R.id.activity_time); timeView.setEnabled(false);
        commentView = v.findViewById(R.id.activity_comment); commentView.setEnabled(false);
        timeStampView = v.findViewById(R.id.activity_timeStamp); timeStampView.setEnabled(false);
        String type = getArguments().getString("type");

        dropdown.setAdapter(adapter);
        for (int i=0;i<dropdown.getCount();i++){
            if (dropdown.getItemAtPosition(i).toString().equalsIgnoreCase(type)){
                dropdown.setSelection(i);
                break;
            }
        }
        String time = getArguments().getString("time");
        String comment = getArguments().getString("comment");
        timeStamp = getArguments().getString("timestamp");

        timeView.setText(time);
        commentView.setText(comment);
        timeStampView.setText(timeStamp);


        Button deleteButton = v.findViewById(R.id.deleteButton);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getArguments().getBoolean("isPhone")){
                //    int test = Integer.parseInt(id);
                //    getActivity().setResult(test);
               //     getActivity().finish();
                } else {
                    String ts = timeStamp;
                    apr.deleteFragment(ts);
                }
            }
        });

        Button editButton = v.findViewById(R.id.editButton);
        saveButton = v.findViewById(R.id.saveButton);

        editButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                saveButton.setEnabled(true);
                dropdown.setEnabled(true);
                timeView.setEnabled(true);
                commentView.setEnabled(true);
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                SQLiteDatabase db = dbHelper.getWritableDatabase();

                Log.i(this.toString(), "Hey you got here! You might be doing things");

                Log.i(this.toString(), "Did you just finish doing things? Wow!");
                try{
                    db.execSQL("UPDATE " + dbHelper.TABLE_NAME+ " SET ACTIVITY_TYPE = '"+ dropdown.getSelectedItem().toString()+ "', ACTIVITY_TIME = '" + timeView.getText().toString() + "', ACTIVITY_COMMENTS = '" +commentView.getText().toString() + "' WHERE ACTIVITY_TIMESTAMP = '"+timeStamp+"';");
                    //db.insert(dbHelper.TABLE_NAME, null, cv);

                if (!getArguments().getBoolean("isPhone")){
                    apr.updateListView();
                }

               }catch (Exception e){
                   Log.i(this.toString(), "Error inserting values in database");
               }
            }
        });

        return v;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        dbHelper = new ActivityDatabaseHelper(activity);
        adapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, items);

    }



}
