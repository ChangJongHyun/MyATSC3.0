package com.btl.hcj.myapplication.Serialize;

class Line {
    private String name;
    private String short_name;
    private String color;
    private TransitAgency[] agencies;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public TransitAgency[] getAgencies() {
        return agencies;
    }

    public void setAgencies(TransitAgency[] agencies) {
        this.agencies = agencies;
    }
}
