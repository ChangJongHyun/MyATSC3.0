package com.btl.hcj.myapplication.Serialize;

import java.util.Map;

public class Direction {
    private String status;
    private GeocodeWayPoint[] geocoded_waypoints;
    private Route[] routes;
    private String[] available_travel_modes;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public GeocodeWayPoint[] getGeocoded_waypoints() {
        return geocoded_waypoints;
    }

    public void setGeocoded_waypoints(GeocodeWayPoint[] geocoded_waypoints) {
        this.geocoded_waypoints = geocoded_waypoints;
    }

    public Route[] getRoutes() {
        return routes;
    }

    public void setRoutes(Route[] routes) {
        this.routes = routes;
    }

    public String[] getAvailable_travel_modes() {
        return available_travel_modes;
    }

    public void setAvailable_travel_modes(String[] available_travel_modes) {
        this.available_travel_modes = available_travel_modes;
    }
}
