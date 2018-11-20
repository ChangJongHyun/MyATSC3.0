package com.btl.hcj.myapplication.data;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.btl.hcj.myapplication.MyUtils;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MyShelterPath {
    private ArrayList<LatLng> mPaths;
    private InputStream is;
    private JSONObject mJsonObject;
    private AssetManager am;
    private static final String TAG = "MyShelterPath";

    public MyShelterPath(Context context) {
        mPaths = new ArrayList<>();
        am = context.getAssets();
        readAsset();
        updateJSON();
    }

    private void readJSON(JSONObject object) {
        try {
            JSONArray array = object.getJSONArray("shelter");
            Log.i(TAG, array.length() + "shelter length");
            for (int i = 0; i < array.length(); i++)
                mPaths.add(MyUtils.convertStringToLatLng(array.getJSONObject(i).getString("position")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addPath(String s) {
        mPaths.add(MyUtils.convertStringToLatLng(s));
    }

    public LatLng getPath(int index) {
        return mPaths.get(index);
    }

    public LatLng[] getAllPath() {
        LatLng[] a = new LatLng[mPaths.size()];
        return mPaths.toArray(a);
    }

    private void readAsset() {
        try {
            this.is = am.open("path.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateJSON() {
        try {
            readAsset();
            readJSON(new JSONObject(MyUtils.getJson(is)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
