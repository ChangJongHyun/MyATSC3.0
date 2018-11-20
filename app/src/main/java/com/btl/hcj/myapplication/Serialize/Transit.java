package com.btl.hcj.myapplication.Serialize;

import java.util.Map;

class Transit {
    private StopInfo arrival_stop; // name, location
    private StopInfo departure_stop; // name, location
    private ValTextTime arrival_time; // text, value, time_zone
    private ValTextTime departure_time; // text, value, time_zone
    private String headsign;
    private double headway;
    private int num_stops;
    private Line line;
    private String url;
    private String icon; // icon url
    private String text_color;
    private Vehicle vehicle;

}
