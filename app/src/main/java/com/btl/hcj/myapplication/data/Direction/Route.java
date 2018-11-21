package com.btl.hcj.myapplication.data.Direction;

public class Route {
    public Bound bounds;
    public String copyrights;
    public Leg[] legs;
    public MyPolyLine overview_polyline;  // point --> encoded String
    public String summary;
    public String[] warnings;
    public int[] waypoint_order;

    public double getTotalDuration() {
        double duration = 0;
        for(int i = 0; i < legs.length; i++) {
            duration += legs[i].duration.value;
        }
        return duration;
    }
}
