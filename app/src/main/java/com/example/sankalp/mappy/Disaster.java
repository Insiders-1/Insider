package com.example.sankalp.mappy;

public class Disaster {

    private String uid;
    String disaster;
    double lat;
    double lng;

    public Disaster(){

    }

    public Disaster(String uid , String disaster , double lat , double lng){
        this.uid = uid;
        this.disaster = disaster;
        this.lat = lat;
        this.lng= lng;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDisaster() {
        return disaster;
    }

    public void setDisaster(String disaster) {
        this.disaster = disaster;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
