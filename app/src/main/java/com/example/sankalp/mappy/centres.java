package com.example.sankalp.mappy;

public class centres {

    String hospital;
    String reliefCentre;
    String food;

    public centres(String hospital, String reliefCentre, String food) {
        this.hospital = hospital;
        this.reliefCentre = reliefCentre;
        this.food = food;
    }

    public centres() {
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getReliefCentre() {
        return reliefCentre;
    }

    public void setReliefCentre(String reliefCentre) {
        this.reliefCentre = reliefCentre;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }
}
