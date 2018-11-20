package com.btl.hcj.myapplication;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MyUtils {

    private static HttpsURLConnection mHttpsURLConnection;
    private static InputStream mInputStream;
    public static final String apiKey = "AIzaSyBHFonEYEGbbr0ZjsAWtEqKkpYrEDu5cH8";
    public static final String DirecUrl = "https://maps.googleapis.com/maps/api/directions/json?";
    public static final String PlaceUrl = "https://maps.googleapis.com/maps/api/place/details/json?";
    public static final String NearByUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";

    public static String getJson(URL url) {
        String json = null;
        try {
            mHttpsURLConnection = (HttpsURLConnection) url.openConnection();
            mHttpsURLConnection.connect();
            mInputStream = mHttpsURLConnection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(mInputStream, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line = "";

            while ((line = bufferedReader.readLine()) != null)
                sb.append(line);

            json = sb.toString();

            bufferedReader.close();
            mInputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }

    public static String getJson(InputStream is) {
        String json = null;
        try {
            mInputStream = is;

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(mInputStream, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line = "";

            while ((line = bufferedReader.readLine()) != null)
                sb.append(line);

            json = sb.toString();

            bufferedReader.close();
            mInputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }

    public static String getDirection(String origin, String dest, boolean usePlace) {
        StringBuilder sb = new StringBuilder();
        String json = null;
        if (usePlace) {
            sb.append(DirecUrl);
            sb.append("origin=place_id:").append(origin);
            sb.append("&destination=place_id:").append(dest);
            sb.append("&language=ko");
            sb.append("&mode=transit");
            sb.append("&key=").append(apiKey);
        } else {
            sb.append(DirecUrl);
            sb.append("origin=").append(origin);
            sb.append("&destination=").append(dest);
            sb.append("&language=ko");
            sb.append("&mode=transit");
            sb.append("&key=").append(apiKey);
        }

        try {
//            Log.i(TAG, "Request direction: " + sb.toString());
            URL url = new URL(sb.toString());
            json = MyUtils.getJson(url);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static LatLng convertStringToLatLng(String s) {
        s = s.replaceAll(" ", "");
        String[] strings = s.split(",");
        return new LatLng(Double.parseDouble(strings[0]), Double.parseDouble(strings[1]));
    }

    public static String convertLatLngToString(LatLng l) {
        return l.latitude + ", " + l.longitude;
    }
}
