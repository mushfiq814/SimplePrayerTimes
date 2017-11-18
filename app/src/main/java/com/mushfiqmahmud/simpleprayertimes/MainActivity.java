package com.mushfiqmahmud.simpleprayertimes;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.mushfiqmahmud.simpleprayertimes.Utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

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
            tvFajrTime.setText(result[0]);
            tvZuhrTime.setText(result[1]);
            tvAsrTime.setText(result[2]);
            tvMaghribTime.setText(result[3]);
            tvIshaTime.setText(result[4]);
            tvDate.setText(result[5]);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        loadJSONResponse();
        return true;
    }
}
