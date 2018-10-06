package com.btl.hcj.myapplication.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.Serializable;
import java.util.HashSet;

public class GeocodeWayPoint implements Serializable{

    private String geocoderStatus;
    private String placeId;
    private HashSet<String> types;
    private String partialMatch;

    GeocodeWayPoint(String jsonObject) {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonElement rootObj = parser.parse(jsonObject);
    }
}
