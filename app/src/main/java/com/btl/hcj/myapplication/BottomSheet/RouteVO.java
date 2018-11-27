package com.btl.hcj.myapplication.BottomSheet;

import com.btl.hcj.myapplication.data.ATSC.Shelter;
import com.btl.hcj.myapplication.data.Direction.Direction;
import com.google.android.gms.maps.model.LatLng;

// 현재 사용 x 그냥 data의 route를 이용중
public class RouteVO {
    Shelter shelter;
    Direction direction;
    int count;

    public RouteVO(Shelter shelter, Direction direction) {
        this.shelter = shelter;
        this.direction = direction;
        count = 0;
    }

    public double getFirstDuraion() {
        return direction.routes[0].getTotalDuration();
    }

    public String getStringDuration() {
        return direction.routes[0].getStringDuration();
    }

    public String getOverviewPolyline() {
        return this.direction.getOverivewPolyline();
    }

    public LatLng getDestination() {
        return new LatLng(shelter.position.lat, shelter.position.lng);
    }

    public String getStringDestiation() {
        return this.shelter.name;
    }

    public String getDistance() {
        return this.direction.routes[0].getStringDistance();
    }

    public String getNextPolyline() {
        String p = this.direction.routes[count].overview_polyline.points;
        return p;
    }
}
