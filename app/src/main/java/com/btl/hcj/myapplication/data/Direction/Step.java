package com.btl.hcj.myapplication.data.Direction;

class Step {
    public String html_instructions;
    public ValText distance; // value-, text-
    public ValText duration; // value-, text-
    public MyLatLng start_location; // lat, lng
    public MyLatLng end_location; // lat, lng
    public String[] maneuver;
    public MyPolyLine polyline; // points
    public Step[] steps;
    public Transit transit_details;


}
