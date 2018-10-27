package com.example.sankalp.mappy;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;

import static android.provider.UserDictionary.Words.APP_ID;

public class floatingAct extends AppCompatActivity {

    TextView tvlocation;
    Button locationButton;
    GoogleApiClient googleApiClient;
    Location lastLocation;
    PlaceAutocompleteFragment places;

    double lat;
    double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floating);

        Intent intent = getIntent();
        lat = intent.getDoubleExtra("lat" , 3);
        lng = intent.getDoubleExtra("lng" , 3);

        tvlocation = findViewById(R.id.location);
        locationButton = findViewById(R.id.locationButton);

        places= (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        places.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                List<Address> addressList = null;

                if (!TextUtils.isEmpty(place.getName()))
                {
                    Geocoder geocoder = new Geocoder(floatingAct.this);

                    try
                    {
                        addressList = geocoder.getFromLocationName(String.valueOf(place.getName()), 1);

                        if (addressList != null)
                        {
                            for (int i=0; i<addressList.size(); i++)
                            {
                                Address userAddress = addressList.get(i);
                                lat = userAddress.getLatitude();
                                lng = userAddress.getLongitude();
                            }
                        }
                        else
                        {
                            Log.e("ERROR", "Location not found...");
                        }
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Log.e("ERROR", "please write any location name...");
                }
            }

            @Override
            public void onError(Status status) {

            }
        });
    }

    public void locationClicked(View view)
    {
         Toast.makeText(this , lat + "  " + lng , Toast.LENGTH_SHORT).show();

        tvlocation.setText(lat + " , " + lng);

        Intent intent = new Intent(this , ChooseOption.class);
        intent.putExtra("Latitude" , lat);
        intent.putExtra("longitude" , lng);

        startActivity(intent);

        }


    public void searchClicked(View view){

        // Enter code to put lat and lon to firebase


        Intent intent = new Intent(this , ChooseOption.class);
        intent.putExtra("Latitude" , lat);
        intent.putExtra("longitude" , lng);

        startActivity(intent);




    }

}