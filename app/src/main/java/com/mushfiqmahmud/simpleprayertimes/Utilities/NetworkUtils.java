package com.mushfiqmahmud.simpleprayertimes.Utilities;

import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by mushf on 11/18/2017.
 */

public final class NetworkUtils {

    private static final String BASE_URL = "http://api.aladhan.com/timings/0?";

    private static final String LATITUDE_PARAM = "latitude";
    private static final String latitude = "27.950575";
    private static final String LONGITUDE_PARAM = "longitude";
    private static final String longitude = "-82.4571776";
    private static final String METHOD_PARAM = "method";
    private static final String method = "2";

    public static URL buildUrl() {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(LATITUDE_PARAM, latitude)
                .appendQueryParameter(LONGITUDE_PARAM, longitude)
                .appendQueryParameter(METHOD_PARAM, method)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v("buidUrl", "Built URI " + url);
        return url;
    }

    public static String[] getPrayerTimes(String JSONString) throws JSONException {
        String[] times = new String[6];
        JSONObject prayerTimes = new JSONObject(JSONString);
        JSONObject data = prayerTimes.getJSONObject("data");
        JSONObject timings = data.getJSONObject("timings");
        times[0] = timings.getString("Fajr");
        times[1] = timings.getString("Dhuhr");
        times[2] = timings.getString("Asr");
        times[3] = timings.getString("Maghrib");
        times[4] = timings.getString("Isha");
        JSONObject date = data.getJSONObject("date");
        times[5] = date.getString("readable");
        return times;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
