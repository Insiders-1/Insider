package com.example.sankalp.mappy;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class disaster_reading {

    private String uid;
    String disaster;
    double lat;
    double lng;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference myRef = firebaseDatabase.getReference();



    public disaster_reading(String uid , String disaster , double lat , double lng){
        this.uid = uid;
        this.disaster = disaster;
        this.lat = lat;
        this.lng= lng;
    }

    public disaster_reading() {
    }

    Disaster disasters = new Disaster();

}
