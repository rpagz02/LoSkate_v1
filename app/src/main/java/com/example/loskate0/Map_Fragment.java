package com.example.loskate0;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import java.util.Random;

public class Map_Fragment extends Fragment implements OnMapReadyCallback
{

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                   Variables                                                //
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private  GoogleMap gMap;
    private boolean mLocationPermissionGranted = false;
    private static final int mLocationCode = 1234;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // *TESTING*  Database Variables                                                              //
//    private FirebaseDatabase database;
//    private DatabaseReference myRef;
//    private DatabaseReference mDatabase;
    private String ID;




    // Check our Permissions in this method
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        GetLocationPermission();
        Toast.makeText(this.getContext(), "Drop A Spot!", Toast.LENGTH_SHORT).show();
    }


    // Method called every time the map is ready
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        gMap = googleMap;
        if (mLocationPermissionGranted)
        {
            GetDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                return;
            gMap.setMyLocationEnabled(true);
            gMap.getUiSettings().setCompassEnabled(true);
            gMap.getUiSettings().setMapToolbarEnabled(true);

            /*  --------------------------------------------------------------------------------------  */
            // TESTING RETREIVING DATABASE OBJ LOCATIONS HERE
            /*  --------------------------------------------------------------------------------------  */

//            FirebaseDatabase.getInstance().getReference().child(RandomStringGenerator()).addListenerForSingleValueEvent(new ValueEventListener()
//
//            {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot)
//                {
//                    for (DataSnapshot ds : dataSnapshot.getChildren())
//                    {
//                        //LatLng temp = new LatLng(28.587640,-81.286320);
//                        //gMap.addMarker(new MarkerOptions().position(spot.getCoords()).title("Saved Spot"));
//                        MarkerInfo spot = ds.getValue(MarkerInfo.class);
//                        double lat = spot.getCoords().latitude;
//                        double lon = spot.getCoords().longitude;
//                        LatLng markerLocation = new LatLng(lat,lon);
//                        gMap.addMarker(new MarkerOptions().position(markerLocation).title("Saved Spot"));
//                        System.out.println(spot);
//                    }
//                }
//                @Override
//                public void onCancelled(DatabaseError databaseError)
//                {
//                }
//            });
        }
        // Testing to see
    }


    // Inflate the fragment here
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        // Handle Button Stuff Here
        Button markerButton = (Button) rootView.findViewById(R.id.MapMarkerButton);
        markerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Drop a marker
                DropMarker();
            }
        });
        // Handle Map Stuff here
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment.getMapAsync(this);
        return rootView;
    }

    private void InitMap()
    {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment.getMapAsync(this);
    }

    private void GetLocationPermission()
    {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
            } else {
                ActivityCompat.requestPermissions(this.getActivity(), permissions, mLocationCode);
            }
        } else {
            ActivityCompat.requestPermissions(this.getActivity(), permissions, mLocationCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        mLocationPermissionGranted = false;

        switch (requestCode) {
            case mLocationCode: {
                if (grantResults.length > 0)
                {
                    // Loop through our permissions to see if they are granted
                    for (int i = 0; i < grantResults.length; i++)
                    {
                        // if 1 permission returns false, we return
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
                        {
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    // if all permissions are returned as granted, we set to true
                    mLocationPermissionGranted = true;
                    // And initialize our map
                    InitMap();
                }
                break;
            }
        }
    }

    // LOCATION METHODS //

    private void GetDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getContext());
        try {
            if (mLocationPermissionGranted) {
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Location currentLocation = (Location) task.getResult();

                            MoveMapCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 18f);

                        } else {
                            Toast.makeText(Map_Fragment.this.getContext(), "unable to get location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
        }
    }

    private void MoveMapCamera(LatLng latLng, float zoom) {
        //Toast.makeText(Map_Fragment.this.getContext(), "Peepin Your Location", Toast.LENGTH_SHORT).show();
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void DropMarker() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getContext());
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        final Task location = mFusedLocationProviderClient.getLastLocation();
        location.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful())
                {
                    Location currentLocation = (Location) task.getResult();
                    gMap.addMarker(new MarkerOptions().position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())).title("Current Spot"));
                    // Store the location in the database here
                    //StoreLocationInDatabase(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
                } else
                    Toast.makeText(Map_Fragment.this.getContext(), "unable to get location", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // This method is called everytime a marker is dropped.
    // We will store the marker location in the database
//    private void StoreLocationInDatabase(LatLng coords)
//    {
//            mDatabase = FirebaseDatabase.getInstance().getReference();
//            MarkerInfo spot = new MarkerInfo(coords);
//            ID = RandomStringGenerator();
//            mDatabase.child(ID).setValue(spot);
//    }

    // This method is called when the map is ready
    // It loops through the database and maps the markers accordingly
//    private void DisplaySavedMarkers(final GoogleMap _gmap)
//    {
//        FirebaseDatabase.getInstance().getReference("loskate0-copy").addValueEventListener(new ValueEventListener()
//        {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot)
//                    {
//                        for (DataSnapshot ds : dataSnapshot.getChildren())
//                        {
//                            MarkerInfo spot = ds.getValue(MarkerInfo.class);
//                            LatLng temp = new LatLng(28.609240,-81.287090);
//                            //gMap.addMarker(new MarkerOptions().position(spot.getCoords()).title("Saved Spot"));
//                            _gmap.addMarker(new MarkerOptions().position(temp).title("Saved Spot"));
//
//                            System.out.println(spot);
//                        }
//                    }
//                    @Override
//                    public void onCancelled(DatabaseError databaseError)
//                    {
//                    }
//        });
//    }

    private String RandomStringGenerator() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 8) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
    // DATABASE METHODS

}
