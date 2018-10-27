package com.example.sankalp.mappy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener




{

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    String date;
    String name;
    String status;
    String result;
    int x = 0;
    ArrayList<String > stringCountry = new ArrayList<String>();
    ArrayList<String > stringType = new ArrayList<String>();

    private Marker currentUserLocationMarker;
    private static final int Request_User_Location_Code = 99;
    private int ProximityRadius = 10000;
    private double latitide, longitude;
    PlaceAutocompleteFragment places;

    public class DownloadTask extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {

                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {

                Log.e("yoo", s);

                JSONObject jsonObject = new JSONObject(s);

                String disasters = jsonObject.getString("data");

                Log.e("disasters", disasters);

                JSONArray arr = new JSONArray(disasters);

                for (int i=0; i < arr.length(); i++) {
                    JSONObject jsonPart = arr.getJSONObject(i);
                    String fields = jsonPart.getString("fields");
                    Log.e("field " + i , fields);
                    JSONObject jsonFields = new JSONObject(fields);
                    date = jsonFields.getString("date");
                    name = jsonFields.getString("name");
                    status = jsonFields.getString("status");

                       Log.e("date" , date);
                       Log.e("name", name);
                       Log.e("status" , status);

                    String type = jsonFields.getString("type");

                    JSONArray types = new JSONArray(type);

                    String country = jsonFields.getString("country");

                    JSONArray countries = new JSONArray(country);

                    for(int j=0 ; j<types.length(); ++j){
                        JSONObject jsonType = types.getJSONObject(j);
                        stringType.add(jsonType.getString("name"));
                         Log.e("Type "+j , stringType.get(x++));


                        JSONObject jsonCountry = countries.getJSONObject(j);
                        String countryString = jsonCountry.getString("name");
                        stringCountry.add(countryString);
                        if(countryString == "India"){

                            Log.e("INDIA" , "Indian Disaster");
                            //code for notification

                        }
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)

        {
            checkUserLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        DownloadTask task = new DownloadTask();
        try {
            result = task.execute("https://api.reliefweb.int/v1/disasters?appname=rwint-user-01&profile=list&preset=latest").get();
            //Log.i("yooo" , result);

        }catch(Exception e){
            e.printStackTrace();
        }

        places= (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        places.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                List<Address> addressList = null;
                MarkerOptions userMarkerOptions = new MarkerOptions();

                if (!TextUtils.isEmpty(place.getName()))
                {
                    Geocoder geocoder = new Geocoder(MapsActivity.this);

                    try
                    {
                        addressList = geocoder.getFromLocationName(String.valueOf(place.getName()), 6);

                        if (addressList != null)
                        {
                            for (int i=0; i<addressList.size(); i++)
                            {
                                Address userAddress = addressList.get(i);
                                LatLng latLng = new LatLng(userAddress.getLatitude(), userAddress.getLongitude());

                                userMarkerOptions.position(latLng);
                                userMarkerOptions.title(String.valueOf(place.getName()));
                                userMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                                mMap.addMarker(userMarkerOptions);
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
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

                Toast.makeText(getApplicationContext(),status.toString() + "yupiiieeee",Toast.LENGTH_SHORT).show();

            }
        });

      //  FragmentManager manager = this.getSupportFragmentManager();
       // manager.beginTransaction()
       //         .hide(manager.findFragmentById(R.id.place_autocomplete_fragment))
       //         .commit();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    public void onClick(View v)
    {
        String hospital = "hospital", school = "school", restaurant = "restaurant";
        Object transferData[] = new Object[2];
        GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();


        switch (v.getId())
        {
            case R.id.search_address:
                EditText addressField = (EditText) findViewById(R.id.location_search);
                String address = addressField.getText().toString();

                List<Address> addressList = null;
                MarkerOptions userMarkerOptions = new MarkerOptions();

                if (!TextUtils.isEmpty(address))
                {
                    Geocoder geocoder = new Geocoder(this);

                    try
                    {
                        addressList = geocoder.getFromLocationName(address, 6);

                        if (addressList != null)
                        {
                            for (int i=0; i<addressList.size(); i++)
                            {
                                Address userAddress = addressList.get(i);
                                LatLng latLng = new LatLng(userAddress.getLatitude(), userAddress.getLongitude());

                                userMarkerOptions.position(latLng);
                                userMarkerOptions.title(address);
                                userMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                                mMap.addMarker(userMarkerOptions);
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                            }
                        }
                        else
                        {
                            Toast.makeText(this, "Location not found...", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(this, "please write any location name...", Toast.LENGTH_SHORT).show();
                }
                break;


            case R.id.hospitals_nearby:
                mMap.clear();
                String url = getUrl(latitide, longitude, hospital);
                transferData[0] = mMap;
                transferData[1] = url;

                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Searching for Nearby Hospitals...", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Showing Nearby Hospitals...", Toast.LENGTH_SHORT).show();
                break;


            case R.id.schools_nearby:
                mMap.clear();
                url = getUrl(latitide, longitude, school);
                transferData[0] = mMap;
                transferData[1] = url;

                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Searching for Nearby Schools...", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Showing Nearby Schools...", Toast.LENGTH_SHORT).show();
                break;


            case R.id.restaurant_nearby:
                mMap.clear();
                url = getUrl(latitide, longitude, restaurant);
                transferData[0] = mMap;
                transferData[1] = url;

                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Searching for Nearby Restaurants...", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Showing Nearby Restaurants...", Toast.LENGTH_SHORT).show();
                break;




        }
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)

        {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);


        }
    }




    public boolean checkUserLocationPermission()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
            }
            else
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);

            }
            return false;

        }
        else
        {
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case Request_User_Location_Code:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        if (googleApiClient == null)
                        {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }

                else
                {
                    Toast.makeText(this, "Permission Denied...", Toast.LENGTH_SHORT).show();
                }
                return;

        }

    }

    protected synchronized void buildGoogleApiClient()
     {
       googleApiClient = new GoogleApiClient.Builder(this)
               .addConnectionCallbacks(this)
               .addOnConnectionFailedListener(this)
               .addApi(LocationServices.API)
               .build();

       googleApiClient.connect();


     }


     @Override
     public void onLocationChanged(Location location) {

        lastLocation = location;
        latitide = location.getLatitude();
        longitude = location.getLongitude();
        if (currentUserLocationMarker != null )
         {
             currentUserLocationMarker.remove();
         }

         LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Your Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        currentUserLocationMarker = mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(16));


        if (googleApiClient != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }


     }



     @Override
     public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

        }




     }

     public void FABonClick(View view){

         Intent intent = new Intent(this , floatingAct.class);
         intent.putExtra("lat" , lastLocation.getLatitude());
         Toast.makeText(this , String.valueOf(lastLocation.getLatitude()) , Toast.LENGTH_SHORT).show();;
         intent.putExtra("lng" , lastLocation.getLongitude());
         startActivity(intent);

     }

     @Override
     public void onConnectionSuspended(int i) {

     }

     @Override
     public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

     }

    private String getUrl(double latitide, double longitude, String nearbyPlace)
    {
        StringBuilder googleURL = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googleURL.append("location=" + latitide + "," + longitude);
        googleURL.append("&radius=" + ProximityRadius);
        googleURL.append("&type=" + nearbyPlace);
        googleURL.append("&sensor=true");
        googleURL.append("&key=" + "AIzaSyDtIWXQDUA1ufc_Vff3qbz522DnZ26Nk9w");

        Log.d("GoogleMapsActivity", "url = " + googleURL.toString());

        return googleURL.toString();
    }


 }
