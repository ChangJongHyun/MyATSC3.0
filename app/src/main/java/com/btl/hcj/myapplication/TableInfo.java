package com.btl.hcj.myapplication;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.ArraySet;
import android.util.Log;

import com.btl.hcj.myapplication.data.DbContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TableInfo {
    private Context mContext;
    private AssetManager mAssetManager;
    private String mJson;
    JSONObject jsonObject;

    // 무적권 도로명 주소
    private Set<String> addr1;  // 1번주소 서울특별시
    private Set<String> addr2;  // 2번주소 무슨구
    private Set<String> addr3;  // 3번주소 무슨무슨~

    private static final String TAG = TableInfo.class.getSimpleName();

    public TableInfo(Context context) {
        this.mContext = context;
        mAssetManager = context.getResources().getAssets();

        addr1 = new HashSet<String>();
        addr2 = new HashSet<String>();
        addr3 = new HashSet<String>();

        readJSON();
    }

    private void readJSON() {
        InputStream is = null;
        try {
            is = mAssetManager.open("전국민방위대피시설표준데이터.json");
            mJson = MyUtils.getJson(is);
            jsonObject = new JSONObject(mJson);
            JSONArray records = jsonObject.getJSONArray("records");
            for (int i = 0; i < records.length(); i++) {
                JSONObject info = records.getJSONObject(i);
                extractAddr(info);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void extractAddr(JSONObject json) {
        String address = "";
        try {
            String address_road = json.getString(DbContract.ShelterInfoEntry.COLUMN_ADDRESS_ROAD);
            address = address_road;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (address.equals("")) {
            try {
                String address_land = json.getString(DbContract.ShelterInfoEntry.COLUMN_ADDRESS_LAND);
                address = address_land;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String[] addr = address.split(" ");

        addr1.add(addr[0]);
        addr2.add(addr[1]);
    }

    // 시 군 구로 나누기.. 붙어있는경우....짤라주거나... 못찾는경우 찾아주기..
    public void log() {
        Log.i(TAG, "ADDR1: " + Arrays.toString(addr1.toArray()));
        Log.i(TAG, "ADDR2: " + Arrays.toString(addr2.toArray()));
    }
}
