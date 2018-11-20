package com.btl.hcj.myapplication.Serialize;

import java.util.Map;

public class Route {
    public Bound bounds;
    public String copyrights;
    public Leg[] legs;
    public MyPolyLine overview_polyline;  // point --> encoded String
    public String summary;
    public String[] warnings;
    public int[] waypoint_order;

}
