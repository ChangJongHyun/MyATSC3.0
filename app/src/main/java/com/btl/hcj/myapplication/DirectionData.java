package com.btl.hcj.myapplication;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class DirectionData {
    private JSONObject mDirInfo;
    private String mStatus;
    private ArrayList<ArrayList> mRoute;
    private static final String TAG = "DirectionData";

    DirectionData(String json) {
        try {
            HashSet<LatLng> info = new HashSet<>();
            mDirInfo = new JSONObject(json);
            mStatus = (String) mDirInfo.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getOverviewPolyLine() throws JSONException {
        String overview = null;
        if (mStatus.equals("OK")) {
            overview = mDirInfo.getJSONArray("routes").getJSONObject(0).getJSONObject("overview_polyline").getString("points");
            Log.i(TAG, overview);
        }
        return overview;
    }

    class Route {
        ArrayList<LatLng> bounds;
        ArrayList<Leg> Legs;
        String overviewPolyline;
    }

    class Leg {
        ArrayList<String> arrivalTime;
        ArrayList<String> departureTime;
        ArrayList<String> distance;
        ArrayList<String> duration;
        ArrayList<String> endAddress;
        ArrayList<LatLng> endLocation;
    }

    class Step implements Serializable{

    }
}
