package com.btl.hcj.myapplication.data.ATSC;

import com.google.android.gms.maps.model.LatLng;

public class ATSCData {
    public Shelter[] shelter;
    public Warning[] warnings;

    public LatLng[] getPathTable() {
        LatLng[] result = new LatLng[shelter.length];

        for(int i = 0; i <shelter.length; i++)
            result[i] = shelter[i].getLatLng();

        return result;
    }

    public Shelter[] getShelter() {
        return shelter;
    }
}
