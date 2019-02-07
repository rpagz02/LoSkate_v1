package com.example.loskate0;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map_Fragment extends Fragment implements OnMapReadyCallback {

    /* Variables */
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    GoogleMap gMap;
    private boolean mLocationPermissionGranted = false;
    private static final int mLocationCode = 1234;
    /* Variables */


    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
//     LatLng coords = new LatLng(11, 105);
//     MarkerOptions markerOptions = new MarkerOptions();
//     markerOptions.position(coords).title("Temp Location");
//     gMap.addMarker(markerOptions);
//     gMap.moveCamera(CameraUpdateFactory.newLatLng(coords));

        Toast.makeText(this.getContext(),"Drop A Spot!", Toast.LENGTH_SHORT).show();
    }


    // Check our Permissions in this method
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GetLocationPermission();
    }

    // Inflate the fragment here
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment.getMapAsync(this);
        return rootView;
    }

    // Create the supportMapFragment and initialize the map
    private void InitMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment.getMapAsync(this);
    }

    // Check Permissions from the user to access location (Fine & Course)
    private void GetLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
       if(ContextCompat.checkSelfPermission(this.getContext(),FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        if(ContextCompat.checkSelfPermission(this.getContext(),COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        }else{
               ActivityCompat.requestPermissions(this.getActivity(),permissions,mLocationCode);
           }
       }else{
           ActivityCompat.requestPermissions(this.getActivity(),permissions,mLocationCode);
       }
    }
    // Method that checks what to do when permissions are either granted or denied
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;

        switch(requestCode){
            case mLocationCode:
            {
                if(grantResults.length > 0 )
                {
                    for(int i = 0; i < grantResults.length; i++ ) // Loop through our permissions to see if they are granted
                    {
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED) // if 1 permission returns false, we return
                        {
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionGranted = true; // if all permissions are returned as granted, we set to true and initialize the map
                    //initialize our map
                    InitMap();
                }
               break;
            }
        }
    }
}
