package com.example.antho.android_final;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static java.lang.Long.parseLong;

public class Activity_PastRecord extends AppCompatActivity {

    ListView lv;
    ActivityDatabaseHelper adh;
    SQLiteDatabase db;
    Cursor cursor;
    ActivityAdapter activityAdapter;

    ArrayList<String> typeList =  new ArrayList<>();
    ArrayList<String> timeList =  new ArrayList<>();
    ArrayList<String> commentList =  new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__past_record);

        adh = new ActivityDatabaseHelper(this);
        db = adh.getReadableDatabase();
        String[] allColumns = {ActivityDatabaseHelper.ACTIVITY_TYPE, ActivityDatabaseHelper.ACTIVITY_TIME, ActivityDatabaseHelper.ACTIVITY_COMMENTS};
        cursor = db.query(adh.TABLE_NAME, allColumns, null, null, null, null, null);


        int r = cursor.getColumnIndex(ActivityDatabaseHelper.ACTIVITY_TYPE);
        int s = cursor.getColumnIndex(ActivityDatabaseHelper.ACTIVITY_TIME);
        int t = cursor.getColumnIndex(ActivityDatabaseHelper.ACTIVITY_COMMENTS);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            typeList.add(cursor.getString(r));
            timeList.add(cursor.getString(s));
            commentList.add(cursor.getString(t));
            cursor.moveToNext();
        }

        lv = (ListView) findViewById(R.id.list_view);
        activityAdapter = new ActivityAdapter(this);
        lv.setAdapter(activityAdapter);
        View v = new View(this);
        activityAdapter.getView(1,v,null);
        //activityAdapter.notifyDataSetChanged();
        Log.i("Hello2.0", "toast");
    }

    private class ActivityAdapter extends ArrayAdapter<String> {

        public ActivityAdapter(Context ctx){

            super(ctx,0);
            Log.i("Hello2.0", "ArrayAdapters are stupid Hi");
        }

        public int getCount(){
            return typeList.size();
        }
//        public int getCountTime(){
//            return timeList.size();
//        }
//        public int getCountComments(){
//            return commentList.size();
//        }

        public String getItem(int position){
            return typeList.get(position);
        }

        public String getTime(int position){
            return timeList.get(position);
        }

        public String getComment(int position){
            return commentList.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = Activity_PastRecord.this.getLayoutInflater();
            Log.i("Hello2.0", "ArrayAdapters are stupid");
            convertView = inflater.inflate(R.layout.activity_list_item, null);
//            if(position%2==0)
//                result = inflater.inflate(R.layout.chat_row_incoming, null);
//            else
//                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            Log.i("Hello2.0", getItem(position));
            TextView message = convertView.findViewById(R.id.message_type);
            message.setText(getItem(position));

            TextView time = convertView.findViewById(R.id.message_time);
            time.setText(getTime(position));

            TextView comment = convertView.findViewById(R.id.message_comment);
            comment.setText(getComment(position));

            return convertView;
        }

        @Override
        public long getItemId(int position){
            //String[] allColumns = {ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE};
            //cursor =db.query(adh.TABLE_NAME, allColumns, null, null, null, null, null);
            cursor.moveToPosition(position);
            long id = parseLong(cursor.getString(0));
            return id;
        }
    }

}
