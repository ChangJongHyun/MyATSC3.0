package com.btl.hcj.myapplication.data.Direction;

class Transit {
    public StopInfo arrival_stop; // name, location
    public StopInfo departure_stop; // name, location
    public ValTextTime arrival_time; // text, value, time_zone
    public ValTextTime departure_time; // text, value, time_zone
    public String headsign;
    public double headway;
    public int num_stops;
    public Line line;
    public String url;
    public String icon; // icon url
    public String text_color;
    public Vehicle vehicle;

}
