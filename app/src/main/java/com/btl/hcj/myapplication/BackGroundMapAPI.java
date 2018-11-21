package com.btl.hcj.myapplication;

import android.content.Context;
import android.content.res.AssetManager;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.btl.hcj.myapplication.BottomSheet.RouteAdapter;
import com.btl.hcj.myapplication.data.Direction.Direction;
import com.btl.hcj.myapplication.data.ATSC.ATSCData;
import com.btl.hcj.myapplication.data.Direction.Route;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.android.PolyUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class BackGroundMapAPI extends AsyncTask<Object, Object, Object[]> {

    private static final String TAG = "BackGroundMapAPI";

    private String mDirecUrl = "https://maps.googleapis.com/maps/api/directions/json?";
    private String mKey = MyUtils.apiKey;
    private Context mContext;
    private LatLng mDest;
    public ArrayList<Route> items;

    private GoogleMap map;

    public BackGroundMapAPI(Context context) {
        this.mContext = context;
    }


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


    /*
     * status code
     * "OK"
     * "ZERO_RESULTS"
     * "OVER_QUERY_LIMIT"
     * "REQUEST_DENIED"
     * "INVALID_REQUEST"
     * "UNKNOWN_ERROR"
     * */
    private Direction getDirection(String origin, String dest, boolean useAlternative) {
        StringBuilder sb = new StringBuilder();
        Direction dd = null;

        // TODO status code에 따라 동작하도록
        if (useAlternative) {
            sb.append(mDirecUrl);
            sb.append("origin=").append(origin);
            sb.append("&destination=").append(dest);
            sb.append("&language=ko");
            sb.append("&mode=transit");
            sb.append("&alternatives=true");
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

        Object[] result = new Object[objects.length];

        int code = (int) objects[2];

        if (code == MapsActivity.REQUEST_DIRECTION) {

            LatLng origin = (LatLng) objects[0];

            LatLng[] table = getPathTable();
            Direction results = findShortDistanceDuration(table, origin);

            result[0] = results;
            result[1] = objects[1]; // google map
            result[2] = origin;
            result[3] = objects[3]; // behavior
            result[4] = objects[4]; // recyclerview

        }
        return result;
    }

    @Override
    protected void onPostExecute(Object[] s) {
        super.onPostExecute(s);

        Direction atsc = (Direction) s[0];
        GoogleMap gmap = (GoogleMap) s[1];
        LatLng origin = (LatLng) s[2];
        RecyclerView recyclerView = (RecyclerView) s[3];
        RouteAdapter routeAdapter = (RouteAdapter) s[4];

        map = gmap;
        map.clear();

        if (atsc != null) {
            String[] polylines = atsc.getAllPolyline();
            Polyline p = map.addPolyline(new PolylineOptions().addAll(PolyUtil.decode(polylines[0])));

            map.addMarker(new MarkerOptions().position(origin).title("Origin"));
            map.addMarker(new MarkerOptions().position(mDest).title("Destination"));

            LatLngBounds.Builder b = new LatLngBounds.Builder();
            b.include(origin);
            b.include(mDest);
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(b.build(), 150));
            items = new ArrayList<>(Arrays.asList(atsc.routes));
            sendItem(items, map);
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

    public LatLng[] getPathTable() {
        AssetManager am = mContext.getAssets();
        LatLng[] table = null;
        try {

            String json = MyUtils.getJson(am.open("path.json"));
            Gson gson = new GsonBuilder().serializeNulls().create();

            ATSCData d = gson.fromJson(json, ATSCData.class);
            table = d.getPathTable();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return table;
    }

    private Direction findShortDistanceDuration(LatLng[] table, LatLng origin) {

        List<Direction> durationList = new LinkedList<>();

        String o = origin.latitude + "," + origin.longitude; // origin
        for (int i = 0; i < table.length; i++) {
            String d = table[i].latitude + "," + table[i].longitude; // dest
            Direction dd = getDirection(o, d, false);
            durationList.add(dd);
        }

        // TODO direction이 없을 경우 에러처리

        Direction min = Collections.min(durationList, (d1, d2) -> (int) (d1.getDuration() - d2.getDuration()));
        mDest = table[durationList.indexOf(min)];
        String dest = mDest.latitude + "," + mDest.longitude;
        return getDirection(o, dest, true);
    }

    public interface ItemListener {
        void getItem(List<Route> item, GoogleMap googleMap);
    }

    public void sendItem(ArrayList<Route> item, GoogleMap googleMap) {
        MapsActivity.getItem(item, googleMap);
    }
}
