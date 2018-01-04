package com.example.antho.android_final;

import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static java.lang.Long.parseLong;

public class Activity_PastRecord extends AppCompatActivity {


    ActivityFragment af = new ActivityFragment(this);
    ListView lv;
    ActivityDatabaseHelper adh;
    SQLiteDatabase db;
    Cursor cursor;
    ActivityAdapter activityAdapter;
    Context ctx = this;

    ArrayList<String> typeList =  new ArrayList<>();
    ArrayList<String> timeList =  new ArrayList<>();
    ArrayList<String> commentList =  new ArrayList<>();
    ArrayList<String> timeStamp =  new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__past_record);

        adh = new ActivityDatabaseHelper(this);
        db = adh.getReadableDatabase();
        String[] allColumns = {ActivityDatabaseHelper.ACTIVITY_TYPE, ActivityDatabaseHelper.ACTIVITY_TIME, ActivityDatabaseHelper.ACTIVITY_COMMENTS, ActivityDatabaseHelper.ACTIVITY_TIMESTAMP};
        cursor = db.query(adh.TABLE_NAME, allColumns, null, null, null, null, null);


        int r = cursor.getColumnIndex(ActivityDatabaseHelper.ACTIVITY_TYPE);
        Log.i("Test number", r+"");
        int s = cursor.getColumnIndex(ActivityDatabaseHelper.ACTIVITY_TIME);
        int t = cursor.getColumnIndex(ActivityDatabaseHelper.ACTIVITY_COMMENTS);
        int u = cursor.getColumnIndex(ActivityDatabaseHelper.ACTIVITY_TIMESTAMP);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            typeList.add(cursor.getString(r));
            timeList.add(cursor.getString(s));
            commentList.add(cursor.getString(t));
            timeStamp.add(cursor.getString(u));
            cursor.moveToNext();
        }

        lv = (ListView) findViewById(R.id.list_view);
        activityAdapter = new ActivityAdapter(this);
        lv.setAdapter(activityAdapter);
        Log.i("Hello2.0", "toast");


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle info = new Bundle();
                boolean isPhone = findViewById(R.id.frameLayout)==null;
                if (isPhone){
                    //will be called if the user is on a phone
                    Intent intent = new Intent(Activity_PastRecord.this, Activity_MessageDetails.class);

                    info.putString("id",""+id);
                    info.putString("type", "" + activityAdapter.getItem(position));
                    info.putString("time", "" + activityAdapter.getTime(position));
                    info.putString("comment", "" + activityAdapter.getComment(position));
                    info.putString("timestamp", "" + activityAdapter.getTimeStamp(position));
                    info.putBoolean("isPhone", isPhone);
                    intent.putExtras(info);
                    startActivityForResult(intent, 666);
                } //will be called if the user is on a tablet
                else {
                    info.putString("id",""+id);
                    info.putString("type", "" + activityAdapter.getItem(position));
                    info.putString("time", "" + activityAdapter.getTime(position));
                    info.putString("comment", "" + activityAdapter.getComment(position));
                    info.putString("timestamp", "" + activityAdapter.getTimeStamp(position));
                    info.putBoolean("isPhone", isPhone);
                    af.setArguments(info);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.frameLayout, af);
                    ft.commit();
                }
            }
        });

    }

    public class ActivityAdapter extends ArrayAdapter<String> {

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

        public String getTimeStamp(int position){
            return timeStamp.get(position);
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
            Log.i("Hello2.7", getItem(position));
            TextView message = convertView.findViewById(R.id.message_type);
            message.setText(getItem(position));

            TextView time = convertView.findViewById(R.id.message_time);
            time.setText(getTime(position));

            TextView comment = convertView.findViewById(R.id.message_comment);
            comment.setText(getComment(position));

            TextView timeStamp = convertView.findViewById(R.id.message_timeStamp);
            timeStamp.setText(getTimeStamp(position));

            return convertView;
        }

//        @Override
//        public long getItemId(int position){
//            //String[] allColumns = {ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE};
//            //cursor =db.query(adh.TABLE_NAME, allColumns, null, null, null, null, null);
//            cursor.moveToPosition(position);
//            long id = parseLong(cursor.getString(0));
//            return id;
//        }
    }


    public void deleteFragment(String resultcode){
        db.execSQL("delete from " + adh.TABLE_NAME + " where " + adh.ACTIVITY_TIMESTAMP + "='" + resultcode + "';");
        getFragmentManager().beginTransaction().remove(af).commit();
        recreate();
    }

    public void updateListView(){
        Log.i("Hello3.0", "Lewis's method");
        this.recreate();
    }

    @Override
    protected void onActivityResult(int requestcode, int resultcode, Intent data){
        this.recreate();
    }

}
