package com.btl.hcj.myapplication.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.btl.hcj.myapplication.MyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class DbHelper extends SQLiteOpenHelper {

    private static final String TAG = DbHelper.class.getSimpleName();

    private Resources mResources;
    private static final String DATABASE_NAME = "shelter_info.db";
    private static final int DATABASE_VERSION = 1;
    private static DbHelper dbHelper = null;
    AssetManager mAssetManager;
    Context context;
    SQLiteDatabase db;


    // 1번 생성다면 다시 insert 하지 않음..!
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        mResources = context.getResources();

        db = getWritableDatabase();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_BUGS_TABLE = "CREATE TABLE " + DbContract.ShelterInfoEntry.TABLE_NAME + " (" +
                DbContract.ShelterInfoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DbContract.ShelterInfoEntry.COLUMN_DESCRIPTION + " TEXT, " +
                DbContract.ShelterInfoEntry.COLUMN_CLASS + " TEXT, " +
                DbContract.ShelterInfoEntry.COLUMN_ADDRESS_ROAD + " TEXT, " +
                DbContract.ShelterInfoEntry.COLUMN_ADDRESS_LAND + " TEXT, " +
                DbContract.ShelterInfoEntry.COLUMN_LATITUDE + " TEXT, " +
                DbContract.ShelterInfoEntry.COLUMN_LONGITUDE + " TEXT, " +
                DbContract.ShelterInfoEntry.COLUMN_SIZE + " TEXT, " +
                DbContract.ShelterInfoEntry.COLUMN_CAPACITY + " TEXT, " +
                DbContract.ShelterInfoEntry.COLUMN_AVAILABLE + " TEXT, " +
                DbContract.ShelterInfoEntry.COLUMN_TYPE + " TEXT, " +
                DbContract.ShelterInfoEntry.COLUMN_OFFICE_NUMBER + " TEXT, " +
                DbContract.ShelterInfoEntry.COLUMN_OFFICE_NAME + " TEXT, " +
                DbContract.ShelterInfoEntry.COLUMN_UPDATE_DATE + " TEXT " + " );";

        sqLiteDatabase.execSQL(SQL_CREATE_BUGS_TABLE);
        Log.d(TAG, "Database Create Successfully!");

        try {
            readDataToDb(sqLiteDatabase);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private String getStringToJson(JSONObject object, String str) {
        String i = "";
        try {
            i = object.getString(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return i;
    }

    private void readDataToDb(SQLiteDatabase db) throws IOException, JSONException {
        // shelter info --> SI
        mAssetManager = mResources.getAssets();
        InputStream is = mAssetManager.open("전국민방위대피시설표준데이터.json");

        String jsonDataString = MyUtils.getJson(is);
        JSONObject infoItemJsonArray = new JSONObject(jsonDataString);
        JSONArray records = infoItemJsonArray.getJSONArray("records");
        for (int i = 0; i < records.length(); i++) {
            String[] shelterInfo = new String[13];
            JSONObject info = records.getJSONObject(i);

            shelterInfo[0] = getStringToJson(info, DbContract.ShelterInfoEntry.COLUMN_DESCRIPTION);
            shelterInfo[1] = getStringToJson(info, DbContract.ShelterInfoEntry.COLUMN_CLASS);
            shelterInfo[2] = getStringToJson(info, DbContract.ShelterInfoEntry.COLUMN_ADDRESS_ROAD);
            shelterInfo[3] = getStringToJson(info, DbContract.ShelterInfoEntry.COLUMN_ADDRESS_LAND);
            shelterInfo[4] = getStringToJson(info, DbContract.ShelterInfoEntry.COLUMN_LATITUDE);
            shelterInfo[5] = getStringToJson(info, DbContract.ShelterInfoEntry.COLUMN_LONGITUDE);
            shelterInfo[6] = getStringToJson(info, DbContract.ShelterInfoEntry.COLUMN_SIZE);
            shelterInfo[7] = getStringToJson(info, DbContract.ShelterInfoEntry.COLUMN_CAPACITY);
            shelterInfo[8] = getStringToJson(info, DbContract.ShelterInfoEntry.COLUMN_AVAILABLE);
            shelterInfo[9] = getStringToJson(info, DbContract.ShelterInfoEntry.COLUMN_TYPE);
            shelterInfo[10] = getStringToJson(info, DbContract.ShelterInfoEntry.COLUMN_OFFICE_NUMBER);
            shelterInfo[11] = getStringToJson(info, DbContract.ShelterInfoEntry.COLUMN_OFFICE_NAME);
            shelterInfo[12] = getStringToJson(info, DbContract.ShelterInfoEntry.COLUMN_UPDATE_DATE);

            ContentValues siValues = new ContentValues();
            siValues.put(DbContract.ShelterInfoEntry.COLUMN_DESCRIPTION, shelterInfo[0]);
            siValues.put(DbContract.ShelterInfoEntry.COLUMN_CLASS, shelterInfo[1]);
            siValues.put(DbContract.ShelterInfoEntry.COLUMN_ADDRESS_ROAD, shelterInfo[2]);
            siValues.put(DbContract.ShelterInfoEntry.COLUMN_ADDRESS_LAND, shelterInfo[3]);
            siValues.put(DbContract.ShelterInfoEntry.COLUMN_LATITUDE, shelterInfo[4]);
            siValues.put(DbContract.ShelterInfoEntry.COLUMN_LONGITUDE, shelterInfo[5]);
            siValues.put(DbContract.ShelterInfoEntry.COLUMN_SIZE, shelterInfo[6]);
            siValues.put(DbContract.ShelterInfoEntry.COLUMN_CAPACITY, shelterInfo[7]);
            siValues.put(DbContract.ShelterInfoEntry.COLUMN_AVAILABLE, shelterInfo[8]);
            siValues.put(DbContract.ShelterInfoEntry.COLUMN_TYPE, shelterInfo[9]);
            siValues.put(DbContract.ShelterInfoEntry.COLUMN_OFFICE_NUMBER, shelterInfo[10]);
            siValues.put(DbContract.ShelterInfoEntry.COLUMN_OFFICE_NAME, shelterInfo[11]);
            siValues.put(DbContract.ShelterInfoEntry.COLUMN_UPDATE_DATE, shelterInfo[12]);

            db.insert(DbContract.ShelterInfoEntry.TABLE_NAME, null, siValues);

            Log.d(TAG, "Inserted Successfully " + siValues);
        }
    }


}
