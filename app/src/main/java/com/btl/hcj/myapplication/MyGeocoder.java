package com.btl.hcj.myapplication;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

/*
* AsyncTask -> Thread + Handler
* <a,b,c> --> doInBackground(), onProgressUpdate(), onPostExecute()
* */
public class MyGeocoder extends AsyncTask<Object, String, String> {

    private static final String TAG = "MyGeocoder";
    public static final int ANDROID_GEOCODER = 1;
    public static final int GOOGLE_GEOCODER = 2;

    private int mCoder;
    private Geocoder mGeocoder;
    private GoogleMap mMap;
    private String mUrl = "https://maps.googleapis.com/maps/api/geocode/json?";
    private String mKey = MyUtils.apiKey;;
    private LatLng mTarget;
    private MyUtils mJsonUtils;
    private HttpsURLConnection mHttpURLConnection = null;
    InputStream mInputStream = null;
    private Context mContext;


    // https://maps.googleapis.com/maps/api/geocode/json?address=high+st+hasting&components=country:KR&key=YOUR_API_KEY

    public MyGeocoder(Context context) {
        this.mContext = context;
        mGeocoder = new Geocoder(mContext, Locale.KOREA);
        mJsonUtils = new MyUtils();
        Log.i(TAG, "GET API KEY: " + mKey);
    }

    public String doAndroid(LatLng target) {
        List<Address> items = null;
        String data = null;
        try {
            items = mGeocoder.getFromLocation(target.latitude, target.longitude, 3);
            StringBuilder sb = new StringBuilder();
            if (items != null) {
                for (Address item : items) {
                    sb.append(item.toString()).append(" ");
                }
                data = sb.toString();
                Log.i(TAG, "android geocoder: " + sb.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public String doGoogle(LatLng target) {
        String data = null;

        StringBuilder strURL = new StringBuilder();
        strURL.append(mUrl).append("latlng=").append(target.latitude).append(",").append(target.longitude);
        strURL.append("&language=ko");
        strURL.append("&location_type=ROOFTOP");
        strURL.append("&key=").append(mKey);

        Log.i(TAG, "request to google: " + strURL.toString());

        try {
            data = MyUtils.getJson(new URL(strURL.toString()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "Do Google: " + data);
        return data;
    }

    @Override
    protected String doInBackground(Object... objects){
        // object[0] --> android or google
        // object[1] --> target LatLng
        String data = null;
        if ((int) objects[0] == MyGeocoder.ANDROID_GEOCODER)
            data = doAndroid((LatLng) objects[1]);
        else if ((int) objects[0] == MyGeocoder.GOOGLE_GEOCODER)
            data = doGoogle((LatLng) objects[1]);
        return data;
    }

    /*
    * doInBackGround가 끝나고 실행
    * s --> Google: JSON, Android: List<Address>
    * */
    @Override
    protected void onPostExecute(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONObject results = jsonObject.getJSONArray("results").getJSONObject(0);
            String placeId = results.getString("place_id");
            Log.i(TAG, "placeId: " + placeId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
