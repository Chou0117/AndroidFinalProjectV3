package com.example.antho.android_final;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class Activity_NewRecord extends AppCompatActivity {

    SQLiteDatabase database;
    ActivityDatabaseHelper adh;
    ActivityAdapter aa;
    Cursor cursor;
    EditText et;
    EditText et2;
    Button submitButton;
    RadioGroup radioGroup;
    RadioButton radioButton;
    ArrayList<String> activityList = new ArrayList<String>();
    Context ctx = this;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__new_record);

        adh = new ActivityDatabaseHelper(this);
        aa = new ActivityAdapter(this);

        database = adh.getReadableDatabase();
        //String[] allColumns = {ActivityDatabaseHelper.PRIMARY_KEY, ActivityDatabaseHelper.ACTIVITY_TYPE, ActivityDatabaseHelper.ACTIVITY_TIME, ActivityDatabaseHelper.ACTIVITY_COMMENTS};
        //cursor = database.query(adh.TABLE_NAME, allColumns, null, null, null, null, null);

        radioGroup = findViewById((R.id.radioGroup));
        et = findViewById(R.id.newRecordTime);
        et2 = findViewById(R.id.newRecordComments);

        submitButton = findViewById(R.id.submit_button);

        pb = findViewById(R.id.progress_Bar);
        database = adh.getWritableDatabase();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isInteger(et.getText().toString())){
                    WriteTask write = new WriteTask(ctx);
                    write.execute();
                    Toast toast = new Toast(ctx);
                    toast.makeText(ctx, R.string.activity_toast1, toast.LENGTH_LONG).show();
                }else{
                    Toast toast = new Toast(ctx);
                    toast.makeText(ctx, R.string.activity_toast2, toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private class ActivityAdapter extends ArrayAdapter<String>{

        public ActivityAdapter(Context ctx){
            super(ctx, 0);
        }

    }

    public class WriteTask extends AsyncTask<String, Integer, String>{
        Context ctx;
        WriteTask(Context ctx){
            this.ctx = ctx;
        }

        @Override
        protected String doInBackground(String... params){

            ActivityDatabaseHelper dbHelper = new ActivityDatabaseHelper(getBaseContext());
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            try{

                ContentValues cv = new ContentValues();
                int selectedID = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(selectedID);
                cv.put("ACTIVITY_TYPE", radioButton.getText().toString());
                publishProgress(25);
                cv.put("ACTIVITY_TIME", Integer.parseInt(et.getText().toString()));
                publishProgress(50);
                cv.put("ACTIVITY_COMMENTS", et2.getText().toString());
                publishProgress(75);
                try{
                    database.insert(adh.TABLE_NAME, null, cv);
                    onPostExecute();
                }catch (Exception e){
                    Log.i(this.toString(), "Error inserting values in database");
                }
            } finally {
                db.close();
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Integer ...value) {super.onProgressUpdate(value);
        }


        @Override
        protected void onPreExecute() {
            // SHOW THE SPINNER WHILE LOADING FEEDS
            pb.setVisibility(View.VISIBLE);
        }

        protected void onPostExecute(){
            pb.setVisibility(View.INVISIBLE);
        }

    }


    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

}
