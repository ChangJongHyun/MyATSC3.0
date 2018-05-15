package com.btl.hcj.myapplication;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

// 나중에 ATSCData로 통합하자!
public class MyItem implements ClusterItem {

    private LatLng mPosition;

    public MyItem(LatLng latLng) {
        mPosition = latLng;
    }

    public MyItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    @Override
    public LatLng getPosition() {
        return this.mPosition;
    }
}
