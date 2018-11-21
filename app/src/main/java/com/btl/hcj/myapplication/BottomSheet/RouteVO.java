package com.btl.hcj.myapplication.BottomSheet;

// 현재 사용 x 그냥 data의 route를 이용중
public class RouteVO {
//    Direction direction;
    String name;
    String duration;
    int image;

    public RouteVO(String name, String duration) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
