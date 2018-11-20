package com.btl.hcj.myapplication.Serialize;

import java.util.Map;

public class Direction {
    public String status;
    public GeocodeWayPoint[] geocoded_waypoints;
    public Route[] routes;
    public String[] available_travel_modes;

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

    public String[] getAllPolyline() {
        String[] result = new String[routes.length];
        for(int i = 0; i < routes.length; i++)
            result[i] = routes[i].overview_polyline.points;
        return result;
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
