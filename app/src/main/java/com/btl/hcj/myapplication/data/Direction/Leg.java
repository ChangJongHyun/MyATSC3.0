package com.btl.hcj.myapplication.data.Direction;

public class Leg {
    public Step[] steps;
    public ValText distance; // value-, text-
    public ValText duration; // value-, text-
    public ValText duration_in_traffic; // value-, text-
    public ValTextTime arrival_time; // value-, text-, time_zone-
    public ValTextTime departure_time; // text, value, time_zone
    public MyLatLng start_loaction; // lat lng
    public MyLatLng end_location;  // lat lng
    public String start_address;
    public String end_address;

}
