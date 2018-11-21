package com.btl.hcj.myapplication.data.ATSC;

import com.google.android.gms.maps.model.LatLng;

public class Shelter {
    public String name;
    public String description;
    public Position position;

    public LatLng getLatLng() {
        return new LatLng(position.lat, position.lng);
    }
}
