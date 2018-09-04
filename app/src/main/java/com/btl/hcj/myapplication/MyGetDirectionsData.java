package com.btl.hcj.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MyGetDirectionsData extends AsyncTask<Object, String, String> {

    GoogleMap mMap;
    String url;
    LatLng startLatLng, endLatLng;

    HttpsURLConnection httpURLConnection = null;
    String data = "";
    InputStream inputStream = null;
    Context mContext;

    public MyGetDirectionsData(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected String doInBackground(Object... objects) {

        mMap = (GoogleMap) objects[0];
        url = (String) objects[1];
        startLatLng = (LatLng) objects[2];
        endLatLng = (LatLng) objects[3];

        try {
            URL myurl = new URL(url);
            httpURLConnection = (HttpsURLConnection) myurl.openConnection();
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            bufferedReader.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("routes").getJSONObject(0)
                    .getJSONArray("legs").getJSONObject(0).getJSONArray("steps");

            int count = jsonArray.length();
            String[] polyline_array = new String[count];

            JSONObject jsonObject2;

            for (int i = 0; i < count; i++) {
                jsonObject2 = jsonArray.getJSONObject(i);
                String polygone = jsonObject2.getJSONObject("polyline").getString("points");

                polyline_array[i] = polygone;
            }

            int count2 = polyline_array.length;

            for (int i = 0; i < count2; i++) {
                PolylineOptions options2 = new PolylineOptions();
                options2.color(Color.RED).width(10).addAll(PolyUtil.decode(polyline_array[0]));

                mMap.addPolyline(options2);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
