package com.btl.hcj.myapplication.Serialize;

import java.util.Map;

class Leg {
    private Step[] steps;
    private ValText distance; // value-, text-
    private ValText duration; // value-, text-
    private ValText duration_in_traffic; // value-, text-
    private ValTextTime arrival_time; // value-, text-, time_zone-
    private ValTextTime departure_time; // text, value, time_zone
    private MyLatLng start_loaction; // lat lng
    private MyLatLng end_location;  // lat lng
    private String start_address;
    private String end_address;

}
