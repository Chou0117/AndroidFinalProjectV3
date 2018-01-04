package com.example.antho.android_final;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;

import static android.graphics.BitmapFactory.decodeStream;
import static com.example.antho.android_final.FoodActivity.mTwoPane;
import static java.lang.String.join;

public class FoodFragment extends Fragment {
    FoodActivity foodActivity;
    Bundle arg;
    View v;
    ProgressBar progressBar;
    String d;
    String n;
    String c;
    String f;
    String h;
    EditText foodEditText;
    EditText calorieIn;
    EditText fatIn;
    EditText carbohydrateIn;
    DatePicker datePicker;
    TimePicker timePicker;
    int rid;
    FoodDatabaseHelper tempDBH;
    SQLiteDatabase db;


    public FoodFragment() {

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        arg = getArguments();
        v = inflater.inflate(R.layout.activity_food_fragment, container, false);
        d = "No Date";
        n = "No Food Name";
        c = "No Calories";
        f = "No Fat";
        h = "No Carbohydrate";
        if (arg != null) {
            d = arg.getString("DATE");
            n = arg.getString("NAME");
            c = arg.getString("CALORIES");
            f = arg.getString("FAT");
            h = arg.getString("CARBOHYDRATE");
        }
        TextView dateView = v.findViewById(R.id.fragFoodDate);
        dateView.setText(d);
        TextView nameView = v.findViewById(R.id.fragFoodName);
        nameView.setText(n);
        TextView calorieView = v.findViewById(R.id.fragFoodCalorie);
        calorieView.setText(c);
        TextView fatView = v.findViewById(R.id.fragFoodFat);
        fatView.setText(f);
        TextView carbohydrateView = v.findViewById(R.id.fragFoodCarbohydrate);
        carbohydrateView.setText(h);

        Button delete = v.findViewById(R.id.foodFragDeleteButton);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FoodActivity.class);

                rid = Integer.parseInt(arg.getString("_ID"));
                int deleteRN = rid + 1;
                getActivity().setResult(deleteRN, intent);
                if (!mTwoPane)
                    getActivity().finish();
                else {
                    foodActivity.deleteItem(rid);
                }
            }
        });

        Button edit = v.findViewById(R.id.foodFragEditButton);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rid = Integer.parseInt(arg.getString("_ID"));
                int editRN = -1 * (rid + 1);

                Log.i("Result Code", "" + rid);
                Log.i("Result Code", "" + editRN);
                editBox(inflater);
            }
        });

        /* Async Task */
        progressBar = v.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        new FoodQuery().execute("Food Pic Search");

        return v;
    }

    public void editBox(LayoutInflater inflater) {

        LayoutInflater li = inflater;
        View rootTag = (LinearLayout) li.inflate(R.layout.food_input, null);

        foodEditText = rootTag.findViewById(R.id.foodEditText);
        foodEditText.setText(n);
        calorieIn = rootTag.findViewById(R.id.calories);
        calorieIn.setText(c);
        fatIn = rootTag.findViewById(R.id.fat);
        fatIn.setText(f);
        carbohydrateIn = rootTag.findViewById(R.id.carbohydrate);
        carbohydrateIn.setText(h);
        datePicker = rootTag.findViewById(R.id.datePicker);
        timePicker = rootTag.findViewById(R.id.timePicker);

        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage(R.string.foodActivityAddGreetingMsg)
                .setView(rootTag)
                .setNegativeButton(R.string.foodActivityCancelButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setPositiveButton(R.string.foodActivityAddButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        String foodName = foodEditText.getText().toString();
                        int calorie = Integer.parseInt(calorieIn.getText().toString());
                        int fat = Integer.parseInt(fatIn.getText().toString());
                        int carbohydrate = Integer.parseInt(carbohydrateIn.getText().toString());

                        int month = datePicker.getMonth() + 1;
                        String m = "" + month;
                        if (month < 10) m = "0" + month;

                        int day = datePicker.getDayOfMonth();
                        String d = "" + day;
                        if (day < 10) d = "0" + day;

                        int hour = timePicker.getCurrentHour();
                        String h = "" + hour;
                        if (hour < 10) h = "0" + hour;

                        int minutes = timePicker.getCurrentMinute();
                        String mi = "" + minutes;
                        if (minutes < 10) mi = "0" + minutes;

                        String date = m + "/" + d + "  " + h + ":" + mi;

                        ContentValues values = new ContentValues();
                        tempDBH = new FoodDatabaseHelper(getActivity().getApplicationContext());
                        values.put(tempDBH.Column_Names[2], foodName);
                        values.put(tempDBH.Column_Names[3], calorie);
                        values.put(tempDBH.Column_Names[4], fat);
                        values.put(tempDBH.Column_Names[5], carbohydrate);
                        values.put(tempDBH.Column_Names[6], date);

                        db = tempDBH.getWritableDatabase();
                        db.update(tempDBH.FOOD_TABLE, values, tempDBH.Column_Names[0] + " = '" + rid + "'", null);

                        Toast t = Toast.makeText(getActivity().getApplicationContext(), R.string.foodActivityAddSuccessMsg, Toast.LENGTH_SHORT);
                        t.show();
                        if (!mTwoPane) getActivity().finish();
                        else {
                            foodActivity.editItem(rid);
                        }
                    }
                });
        builder1.create().show();
    }

    public void setParentActivity(Activity a) {
        foodActivity =(FoodActivity) a;
    }

    class FoodQuery extends AsyncTask<String, Integer, String> {
        String className = "Async Task Debug";
        StringBuilder food = new StringBuilder(n);
        String foodWeb;
        Bitmap currentFood;

        public boolean fileExistence(String fname) {
            File file = getActivity().getFileStreamPath(fname);
            return file.exists();
        }

        @Override
        protected String doInBackground(String... strings) {
            for (int i = 0; i < food.length(); i++) {
                if (food.charAt(i) == ' ') food.replace(i, i, "+");
            }
        /* http://www.freepngimg.com/ */
            foodWeb = "http://www.freepngimg.com/search/?query=" + food + "&pg=1";
            Log.i(className, foodWeb);
            try {

                if (!fileExistence(food + ".png")) {
                    Log.i(className, foodWeb);
                    URL url = new URL(foodWeb);

                    Log.i(className, "Start Http");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    publishProgress(20);
                    Log.i(className, "Start Connection");
                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    Log.i(className, "Before Connecting");
                    conn.connect();

                    publishProgress(40);
                    Log.i(className, "Connected");

                    Log.i(className, "Start Factory new Instance");
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

                    Log.i(className, "Start set NameSpace");
                    factory.setNamespaceAware(false);
                    Log.i(className, "Start set new Parser");
                    XmlPullParser parser = factory.newPullParser();
                    Log.i(className, "Start set feature");
                    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    Log.i(className, "Start get inputStream");
                    InputStream input = conn.getInputStream();
                    Log.i(className, "Start set input");
                    parser.setInput(input, "UTF-8");

                    int eventType = parser.getEventType();

                    publishProgress(60);
                    Log.i(className, "Start scanning tags");
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_TAG) {

                            if (parser.getName().equals("meta")) {

                                String imageName = "og:image";
                                if (imageName.equals(parser.getAttributeValue(null, "property"))) {

                                    String urlImage = parser.getAttributeValue(null, "content");

                                    Log.i(className, urlImage);
                                    URL imageURL = new URL(urlImage);
                                    HttpURLConnection connection = (HttpURLConnection) imageURL.openConnection();
                                    connection.setReadTimeout(10000);
                                    connection.setConnectTimeout(30000);
                                    connection.setRequestMethod("GET");
                                    connection.setDoInput(true);

                                    Log.i(className, "Before Connection 2");
//                                    connection.connect();

                                    publishProgress(80);
                                    Log.i(className, "Connection 2 start");

                                    Log.i(className, "Start Input Stream");

                                    InputStream imageInput = new BufferedInputStream(connection.getInputStream());
//                                    InputStream imageInput = new BufferedInputStream(connection.getInputStream());
//                                    BitmapFactory.Options opts = new BitmapFactory.Options();
//                                    int width = opts.outWidth;
//                                    int height = opts.outHeight;
//                                    int largerSide = Math.max(width, height);
//                                    opts.inJustDecodeBounds = false; // This time it's for real!
//                                    int sampleSize = 50; // Calculate your sampleSize here
//                                    opts.inSampleSize = sampleSize;
//                                    Log.i(className, "BitmapFactory DecodeStream");
//                                    Bitmap image = BitmapFactory.decodeStream(imageInput,null,opts);

                                    Log.i(className, "BitmapFactory DecodeStream");
                                    BitmapFactory.Options bounds = new BitmapFactory.Options();
                                    bounds.inJustDecodeBounds = false;
                                    bounds.inSampleSize = 16;
                                    Bitmap image = BitmapFactory.decodeStream(imageInput, null, bounds);

                                    Log.i(className, "finished Input Stream");
                                    currentFood = image;

                                    if (image != null) {
                                        Log.i(className, "Start output Stream");

                                        FileOutputStream outputStream = getActivity().openFileOutput(food + ".png", Context.MODE_PRIVATE);
                                        image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);

                                        Log.i(className, "image saved");
                                        outputStream.flush();
                                        outputStream.close();
                                    }

                                    break;
                                }
                            }
                        }


//                        String test = "/head";
//                        if (test.equals(parser.getName()))
//                        {
//                            currentFood = BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.picnotfound);
//                            break;
//                        }
                        eventType = parser.next();
                    }


                } else {
                    Log.i(className, "File found! Load file!");
                    FileInputStream fis = null;
                    fis = getActivity().openFileInput(food + ".png");
                    currentFood = decodeStream(fis);
                }
                publishProgress(100);
            } catch (SocketTimeoutException e) {
                Log.i(className, "Time Out Error" + e.getMessage());
            } catch (XmlPullParserException e) {
                Log.i(className, "XPP Error" + e.getMessage());
            } catch (ProtocolException e) {
                Log.i(className, "Protocol Error" + e.getMessage());
            } catch (IOException e) {
                Log.i(className, "IO Error" + e.getMessage());
            }

            return null;

        }


        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ImageView imageView = v.findViewById(R.id.currentFood);
            imageView.setImageBitmap(currentFood);
            progressBar.setVisibility(View.INVISIBLE);
        }


    }

}
