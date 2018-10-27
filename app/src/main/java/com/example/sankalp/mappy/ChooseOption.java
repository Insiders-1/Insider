package com.example.sankalp.mappy;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChooseOption extends AppCompatActivity {

    double lat;
    double lng;
    Disaster disaster = new Disaster();
    TextView tvdisaster;
    String dis;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("mappy-4bd7e");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_option);

        tvdisaster = (TextView) findViewById(R.id.tvdisaster);

        Intent intent = getIntent();
        lat = intent.getDoubleExtra("Latitude" , 3);
        lng = intent.getDoubleExtra("longitude" , 3);


    }

    public void buttonClicked(View view){

        dis = tvdisaster.getText().toString();
        disaster.setLng(lng);
        disaster.setLat(lat);
        disaster.setDisaster(dis);
        disaster.setUid("fe22rfdsdasdfd");

        myRef.child("user3").setValue(disaster)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e("success" , "SUCCESS");
                        // ...
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("failure" , "FAILURE");
                        // ...
                    }
                });

       // Toast.makeText(this , lat + "  " + lng + "  " + dis , Toast.LENGTH_SHORT).show();
    }
}
