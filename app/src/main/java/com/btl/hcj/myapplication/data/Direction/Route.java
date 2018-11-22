package com.btl.hcj.myapplication.data.Direction;

public class Route {
    public Bound bounds;
    public String copyrights;
    public Leg[] legs;
    public MyPolyLine overview_polyline;  // point --> encoded String
    public String summary;
    public String[] warnings;
    public int[] waypoint_order;


    // Waypoint를 사용하지 않으면 legs 1개만 들어옴
    public double getTotalDuration() {
        return legs[0].duration.value;
    }

    public String getStringDuration() {
        return legs[0].duration.text;
    }

    public String getStringArrival() {
        return legs[0].arrival_time.text;
    }

    public String getStringDeparture() {
        return legs[0].departure_time.text;
    }

    public String getStringDistance() {
        return legs[0].distance.text;
    }

    public Step[] getSteps() {
        return legs[0].steps;
    }
}
