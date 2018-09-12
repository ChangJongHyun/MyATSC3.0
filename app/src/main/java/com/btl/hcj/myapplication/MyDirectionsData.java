package com.btl.hcj.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MyDirectionsData extends AsyncTask<Object, Object, String>{

    private static final String TAG = "MyDirectionsData";

    private String mUrl = "https://maps.googleapis.com/maps/api/directions/json?";
    private String mKey = MyUtils.apiKey;
    private GoogleMap mMap;
    private MyGeocoder mGeocoder;
    private LatLng mOrigin;
    private LatLng mDest;

    private Context mContext;
    private InputStream mInputStream;
    private HttpsURLConnection mHttpsURLConnection;

    public MyDirectionsData(Context context) {
        this.mContext = context;
        mGeocoder = new MyGeocoder(mContext);
    }

    /*
    * status code
    * "OK"
    * "ZERO_RESULTS"
    * "OVER_QUERY_LIMIT"
    * "REQUEST_DENIED"
    * "INVALID_REQUEST"
    * "UNKNOWN_ERROR"
    * */

    /**
     * latlng --> address,
     * @param jsonObject responsed json object
     * @return place id of address
     */
    private String getPlaceId(JSONObject jsonObject) {
        String place_id = null;
        try {
            String statusCode = jsonObject.getString("status");
            if (statusCode.equals("OK")) {
                place_id = jsonObject.getJSONArray("results").getJSONObject(0).getString("place_id");
                Log.i(TAG, "GET place_id: " + place_id);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return place_id;
    }

    private void route(String origin, String dest, int place) {
        StringBuilder sb = new StringBuilder();
        if (place == 1) {
            sb.append(mUrl);
            sb.append("origin=place_id:").append(origin);
            sb.append("&destination=place_id:").append(dest);
            sb.append("&key=").append(mKey);
        } else {
            sb.append(mUrl);
            sb.append("origin=").append(origin);
            sb.append("&destination=").append(dest);
            sb.append("&key=").append(mKey);
        }

        try {
            Log.i(TAG, "Request direction: " + sb.toString());
            URL url = new URL(sb.toString());
            String json = MyUtils.getJson(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(Object... objects) {

        mOrigin = (LatLng) objects[0];
        mDest = (LatLng) objects[1];
        mMap = (GoogleMap) objects[2];

        String origin_latlng = mOrigin.latitude + "," + mOrigin.longitude;
        String dest_latlng = mDest.latitude + "," + mDest.longitude;

        String origin_json = mGeocoder.doGoogle(mOrigin);
        String dest_json = mGeocoder.doGoogle(mDest);
        try {
            String origin_place_id = getPlaceId(new JSONObject(origin_json));
            String dest_place_id = getPlaceId(new JSONObject(dest_json));
            route(origin_place_id, dest_place_id, 1);
            route(origin_latlng, dest_latlng, 2);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // TODO routing algorithm

        return null;
    }
}
