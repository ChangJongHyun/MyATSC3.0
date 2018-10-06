package com.btl.hcj.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

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

    private String mDirecUrl = "https://maps.googleapis.com/maps/api/directions/json?";
    private String mPlaceUrl = "https://maps.googleapis.com/maps/api/place/details/json?";
    private String mNearByUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
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

    private String[] getNearBy(LatLng latLng) {
        StringBuilder sb = new StringBuilder();
        sb.append(mNearByUrl);
        sb.append("location=").append(latLng.latitude).append(",").append(latLng.longitude);
        sb.append("&language=ko");
        sb.append("&type=point_of_interest");
        sb.append("&rankby=distance");
        sb.append("&key=").append(mKey);
        String[] near = new String[5];
        try {
            Log.i(TAG, "Request search nearby: " + sb.toString());
            URL url = new URL(sb.toString());
            String json = MyUtils.getJson(url);
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            if (status.equals("OK")) {
                for (int i = 0; i < near.length; i++) {
                    near[i] = jsonObject.getJSONArray("results").getJSONObject(i).getString("place_id");

                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return near;
    }

    private void getPlace(String placeId) {
        StringBuilder sb = new StringBuilder();
        sb.append(mPlaceUrl);  // "https://maps.googleapis.com/maps/api/place/details/json?"
        sb.append("placeid=").append(placeId);
        sb.append("&language=ko");
        sb.append("&key=").append(mKey);


        try {
            Log.i(TAG, "Request place detail: " + sb.toString());
            URL url = new URL(sb.toString());
            String json = MyUtils.getJson(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    private String getDirection(String origin, String dest, boolean usePlace) {
        StringBuilder sb = new StringBuilder();
        String json = null;
        if (usePlace) {
            sb.append(mDirecUrl);
            sb.append("origin=place_id:").append(origin);
            sb.append("&destination=place_id:").append(dest);
            sb.append("&language=ko");
            sb.append("&mode=transit");
            sb.append("&key=").append(mKey);
        } else {
            sb.append(mDirecUrl);
            sb.append("origin=").append(origin);
            sb.append("&destination=").append(dest);
            sb.append("&language=ko");
            sb.append("&mode=transit");
            sb.append("&key=").append(mKey);
        }

        try {
            Log.i(TAG, "Request direction: " + sb.toString());
            URL url = new URL(sb.toString());
            json = MyUtils.getJson(url);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return json;
    }

    @Override
    protected String doInBackground(Object... objects) {

        mOrigin = (LatLng) objects[0];
        mDest = (LatLng) objects[1];
        mMap = (GoogleMap) objects[2];

        String origin_latlng = mOrigin.latitude + "," + mOrigin.longitude;
        String dest_latlng = mDest.latitude + "," + mDest.longitude;

//        String origin_json = mGeocoder.doGoogle(mOrigin);
//        String dest_json = mGeocoder.doGoogle(mDest);
        String polyline = null;
        try {
//            String origin_place_id = getPlaceId(new JSONObject(origin_json));
//            String dest_place_id = getPlaceId(new JSONObject(dest_json));
//            getDirection(origin_place_id, dest_place_id, true);
            String json1 = getDirection(origin_latlng, dest_latlng, false);
            DirectionData directionData = new DirectionData(json1);
            polyline = directionData.getOverviewPolyLine();
//            String[] near_origin = getNearBy(mOrigin);
//            String[] near_dest = getNearBy(mDest);
//            for (String origin : near_origin)
//                for (String dest : near_dest)
//                    getDirection(origin, dest, 1);


//            getPlace(origin_place_id);
//            getPlace(dest_place_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return polyline;
    }

    /*
    * UI 업데이트 부분 작성*/
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (mMap == null || s == null)
            Log.i(TAG, "Google Map is null or Polyline is null");
        else
            mMap.addPolyline(new PolylineOptions().addAll(PolyUtil.decode(s)));
    }
}
