package com.btl.hcj.myapplication.Serialize;

import java.util.Map;

class Step {
    private String html_instructions;
    private ValText distance; // value-, text-
    private ValText duration; // value-, text-
    private MyLatLng start_location; // lat, lng
    private MyLatLng end_location; // lat, lng
    private String[] maneuver;
    private MyPolyLine polyline; // points
    private Step[] steps;
    private Transit transit_details;


}
