package com.example.loskate0;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.location.LocationListener;


import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    SupportMapFragment supportMapFragment;
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentUserLocationMarker;
    private static final int Request_User_Location_Code = 99;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportMapFragment = new SupportMapFragment();

        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(navListener);

        //This starts our MAP fragment from the start of our app
        getSupportFragmentManager().beginTransaction().replace(R.id.map_container, supportMapFragment).commit();
        //This starts our DASHBOARD fragment from the start of our app (in case the map one doesn't work atm)
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Dashboard_Fragment()).commit();


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            checkUserLocationPermission();


        supportMapFragment.getMapAsync(this);
        ///////////////////////////////////////////////////////////////////////////

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (supportMapFragment.isAdded())
                getSupportFragmentManager().beginTransaction().hide(supportMapFragment).commit();

            switch (item.getItemId()) {

                case R.id.navigation_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Home_Fragment()).commit();
                    break;
                case R.id.navigation_map: {
                    //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Map_Fragment()).commit();
                    if (!supportMapFragment.isAdded())
                        getSupportFragmentManager().beginTransaction().add(R.id.map_container, supportMapFragment).commit();
                    else
                        getSupportFragmentManager().beginTransaction().show(supportMapFragment).commit();
                    break;
                }
                case R.id.navigation_dashboard:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Dashboard_Fragment()).commit();
                    break;
            }


            return true;
        }
    };


    @Override
    public void onMapReady(GoogleMap googleMap) {
//        LatLng marker = new LatLng(28.604940,-81.286910);
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 13));
//        googleMap.addMarker(new MarkerOptions().position(marker).title("Richs House"));

        mMap = googleMap;
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

    }


    protected synchronized void buildGoogleApiClient()
    {
        googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location)
    {
    lastLocation = location;
    if(currentUserLocationMarker != null)
    {
        currentUserLocationMarker.remove();
    }
    LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
    MarkerOptions markerOptions = new MarkerOptions();
    markerOptions.position(latLng);
    markerOptions.title("User Current Position");
    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

    currentUserLocationMarker = mMap.addMarker(markerOptions);

    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    mMap.animateCamera(CameraUpdateFactory.zoomBy(14));

    if(googleApiClient != null)
    {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
    locationRequest = new LocationRequest();
    locationRequest.setInterval(1100);
    locationRequest.setFastestInterval(1100);
    locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    {
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }
    }

    @Override
    public void onConnectionSuspended(int i)
    {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {

    }


    public boolean checkUserLocationPermission()
    {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
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
        switch(requestCode)
        {
            case Request_User_Location_Code:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        if (googleApiClient == null)
                            buildGoogleApiClient();
                    }
                    mMap.setMyLocationEnabled(true);
                }
                else
                {
                    Toast.makeText(this,"Permission Denied...", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }



}
