package com.mushfiqmahmud.simpleprayertimes;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import com.mushfiqmahmud.simpleprayertimes.Utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    TextView tvFajrTime;
    TextView tvZuhrTime;
    TextView tvAsrTime;
    TextView tvMaghribTime;
    TextView tvIshaTime;
    TextView tvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvFajrTime = (TextView) findViewById(R.id.fajr_time);
        tvZuhrTime = (TextView) findViewById(R.id.zuhr_time);
        tvAsrTime = (TextView) findViewById(R.id.asr_time);
        tvMaghribTime = (TextView) findViewById(R.id.maghrib_time);
        tvIshaTime = (TextView) findViewById(R.id.isha_time);
        tvDate = (TextView) findViewById(R.id.tv_date);

        loadJSONResponse();
    }

    private void loadJSONResponse() {
        URL url = NetworkUtils.buildUrl();
        new fetchPrayerTimes().execute(url);
    }

    public class fetchPrayerTimes extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String JSONResponse = null;
            try {
                JSONResponse = NetworkUtils.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return JSONResponse;
        }

        @Override
        protected void onPostExecute(String s) {
            String[] result = null;
            try {
                result = NetworkUtils.getPrayerTimes(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Date/time pattern of input date
            DateFormat df = new SimpleDateFormat("HH:mm");
            //Date/time pattern of desired output date
            DateFormat outputformat = new SimpleDateFormat("hh:mm aa");
            Date date = null;
            String output = null;

            for (int i=0; i<result.length; i++) {

                try{
                    //Conversion of input String to date
                    date = df.parse(result[i]);
                    //old date format to new date format
                    output = outputformat.format(date);
                    result[i] = output;
                }catch(ParseException pe){
                    pe.printStackTrace();
                }
            }

            tvFajrTime.setText(result[0]);
            tvZuhrTime.setText(result[1]);
            tvAsrTime.setText(result[2]);
            tvMaghribTime.setText(result[3]);
            tvIshaTime.setText(result[4]);
            tvDate.setText(result[5]);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        loadJSONResponse();
//        return true;
//    }
}
