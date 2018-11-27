package com.btl.hcj.myapplication.data.Direction;

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

    public String[] getAllPolyline() {
        String[] result = new String[routes.length];
        for(int i = 0; i < routes.length; i++)
            result[i] = routes[i].overview_polyline.points;
        return result;
    }

    public String getOverivewPolyline() {
        return this.routes[0].overview_polyline.points;
    }

    public double getDuration() {
        double result = 0;
        for (int i = 0; i < routes.length; i++)
            result += routes[i].getTotalDuration();
        return result;
    }

}
