package com.btl.hcj.myapplication;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.btl.hcj.myapplication.Serialize.Direction;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.android.PolyUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class BackGroundMapAPI extends AsyncTask<Object, Object, Object[]>{

    private static final String TAG = "BackGroundMapAPI";

    private String mDirecUrl = "https://maps.googleapis.com/maps/api/directions/json?";
    private String mPlaceUrl = "https://maps.googleapis.com/maps/api/place/details/json?";
    private String mNearByUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    private String mKey = MyUtils.apiKey;
    private GoogleMap mMap;
    private LatLng mOrigin;
    private LatLng mDest;
    private Context mContext;
    private InputStream mInputStream;
    private HttpsURLConnection mHttpsURLConnection;

    public BackGroundMapAPI(Context context) {
        this.mContext = context;
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

            Gson gson = new GsonBuilder().serializeNulls().create();
            Direction dd = gson.fromJson(json, Direction.class);

        } catch (MalformedURLException e) {
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

    private Direction getDirection(String origin, String dest, boolean usePlace) {
        StringBuilder sb = new StringBuilder();
        Direction dd = null;
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
            String json = MyUtils.getJson(url);

            Gson gson = new GsonBuilder().serializeNulls().create();
            dd = gson.fromJson(json, Direction.class);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return dd;
    }

    @Override
    protected Object[] doInBackground(Object... objects) {

        Object[] result = new Object[2];

        int code = (int) objects[3];

//        String origin_latlng = mOrigin.latitude + "," + mOrigin.longitude;
//        String dest_latlng = mDest.latitude + "," + mDest.longitude;
        String poly = null;

        if (code == MapsActivity.REQUEST_DIRECTION) {
                mOrigin = (LatLng) objects[0];
                LatLng[] table = (LatLng[]) objects[1];
                mMap = (GoogleMap) objects[2];

                Object[] results = findShortDistanceDuration(table, mOrigin);
//                mDirection = (Direction) results[0];
//                poly = mDirectionData.getOverviewPolyLine();

//                result[0] = poly;
                //TODO reusult[0]에 polyline 보내주기
                result[1] = results[1]; // path table -->
        }
        return result; // googleMap
    }

    @Override
    protected void onPostExecute(Object[] s) {
        super.onPostExecute(s);
        if (mMap == null || s[0] == null) {
            Log.i(TAG, "Google Map is null or Polyline is null");
        }
        else {
            LatLng dest = (LatLng) s[1];
            mMap.addPolyline(new PolylineOptions().addAll(PolyUtil.decode((String) s[0])));
            mMap.addMarker(new MarkerOptions().position(dest).title("Destination"));
            LatLngBounds.Builder b = new LatLngBounds.Builder();
            b.include(mOrigin);
            b.include(dest);
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(b.build(), 150));
        }
    }

    private int findShortDistanceIndex(LatLng[] table, LatLng origin) {
        Long t1 = System.currentTimeMillis();
        float[] result = new float[1];
        ArrayList<Float> length = new ArrayList<>();

        for (int i = 0; i < table.length; i++) {
            Location.distanceBetween(table[i].latitude, table[i].longitude, origin.latitude, origin.latitude, result);
            length.add(result[0]);
        }
        float min = Math.min(Math.min(length.get(0), length.get(1)), length.get(2));

        Long t2 = System.currentTimeMillis();
        Log.i(TAG, "method runtime (Line distance): " + (t2 - t1));

        return length.indexOf(min);
    }

    private Object[] findShortDistanceDuration(LatLng[] table, LatLng origin) {
        Long t1 = System.currentTimeMillis();
        ArrayList<Double> durations = new ArrayList<>();
        ArrayList<Direction> data = new ArrayList<>();
        String o = origin.latitude + "," + origin.longitude; // origin
        for (int i = 0; i < table.length; i++) {
            String d = table[i].latitude + "," + table[i].longitude; // dest
            Direction dd = getDirection(o, d, false);
            data.add(dd);

            // TODO Direction class에서 duration 가지고오기
//            durations.add(Double.parseDouble(dd.getDuration()));

        }
        double min = Math.min(Math.min(durations.get(0), durations.get(1)), durations.get(2));
        int i = durations.indexOf(min);
        Object[] l = {data.get(i), table[i]};
        return l;
    }
}
