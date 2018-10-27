package com.example.sankalp.mappy;

public class helpCentre {

    private String uid;
    centres helpCenter;
    double lat;
    double lng;

    public helpCentre(){

    }

    public helpCentre(String uid , centres helpCenter , double lat , double lng){
        this.uid = uid;
        this.helpCenter = helpCenter;
        this.lat = lat;
        this.lng= lng;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public centres getHelpCenter() {
        return helpCenter;
    }

    public void setHelpCenter(centres helpCenter) {
        this.helpCenter = helpCenter;
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
