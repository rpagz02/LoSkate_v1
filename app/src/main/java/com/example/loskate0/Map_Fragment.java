package com.example.loskate0;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;

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
    private DatabaseReference mDatabase;
    private String ID;
    private static final String TAG = "MyActivity"; // logging to check the values at element positions in DB
    //
    String snippetInfo; // this snippet info is for filling out the marker in dropmarker
    String lat, lon; // these variables are passed to spot info when drop marker is called



    // Check our Permissions in this method
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        GetLocationPermission();
    }

    // Method called every time the map is ready
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        Toast.makeText(this.getContext(), "Drop A Spot!", Toast.LENGTH_SHORT).show();
        gMap = googleMap;
        gMap.setMapType(MAP_TYPE_HYBRID); // Set the map to sat mode
        if (mLocationPermissionGranted)
        {
            GetDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                return;
            gMap.setMyLocationEnabled(true);
            gMap.getUiSettings().setCompassEnabled(true);
            gMap.getUiSettings().setMapToolbarEnabled(true);
           // Read from the database, and plot the spots here
            GetDatabaseContent(gMap);

            // On Marker Click Stuff -> Should open the information window fragment
            gMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                public void onInfoWindowClick(Marker marker) {
                    Log.d(TAG,marker.getTitle() + "  Pagnozzi"); // successfully gets the spot title
                    // Open a new fragment with this specific spots Spot information
                    MainActivity mainActivity = (MainActivity)getActivity();
                    mainActivity.Frag_Trans_Home(marker.getTitle());
                }
            });


        }
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

    // Initailize the Map Fragment
    private void InitMap()
    {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment.getMapAsync(this);
    }

    // Gets Location Permissions
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

    // Checks Permissions
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

    // Uses permisisons to get Location
    private void GetDeviceLocation()
    {
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

    // Moves the map camera to a LatLng and respective Zoom
    private void MoveMapCamera(LatLng latLng, float zoom)
    {
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    // Drops a Marker and Stores Markers Location in Database
    private void DropMarker()  {

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getContext());
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        final Task location = mFusedLocationProviderClient.getLastLocation();
        location.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful())
                {
                    Location currentLocation = (Location) task.getResult();
                    gMap.addMarker(new MarkerOptions().position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())).title("Current Spot")).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    lat = Double.toString(currentLocation.getLatitude());
                    lon = Double.toString(currentLocation.getLongitude());
                    // Store the location in the database here
                    StoreLocationInDatabase(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
                    // Once we dropped a marker and stored it in the DB, we open our CreateASpot fragment
                    MainActivity mainActivity = (MainActivity)getActivity();
                    mainActivity.Frag_Trans_Info(GetSpotID()); // -> as we open the fragment, we pass the marker ID as an arguement
                } else
                    Toast.makeText(Map_Fragment.this.getContext(), "unable to get location", Toast.LENGTH_SHORT).show();
            }
        });

        // Open a new fragment that allows the user to enter information about the spot

    }

    // Method called when marker is dropped.
    private void StoreLocationInDatabase(LatLng coords)
    {
            mDatabase = FirebaseDatabase.getInstance().getReference();
            ID = RandomStringGenerator();
            MarkerInfo spot = new MarkerInfo(Double.toString(coords.latitude), Double.toString(coords.longitude), ID, null, null);
            mDatabase.child("spots").child(ID).setValue(spot);
        Log.d(TAG,"Pagnozzi: setting ID - " + this.ID);
    }

    // Reads the Spot locations from the database and plots their respective locations
    private void GetDatabaseContent(final GoogleMap map)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query1 = reference.child("spots").orderByKey();

        query1.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren())
                {
                    MarkerInfo mI = singleSnapshot.getValue(MarkerInfo.class);
                    LatLng spotPos = new LatLng(Double.parseDouble(mI.getLatitude()),Double.parseDouble(mI.getLongitude()));
                    Log.d(TAG,"onDataChange: Query Method 1) found spot: " + mI.toString());

                    try {
                        snippetInfo = GetAddressFromLatLng(Double.parseDouble(mI.getLatitude()), Double.parseDouble(mI.getLongitude()));
                        snippetInfo = mI.getNotes();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    MarkerOptions marker = new MarkerOptions()
                            .position(spotPos)
                            .title(mI.getID())
                            .snippet(snippetInfo);
                    map.addMarker(marker).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    // Creates a random string to be used as ID
    private String RandomStringGenerator()
    {
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

    // Takes a latitude and longitude and returns a physical address
    private String GetAddressFromLatLng(double Lat, double Long) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this.getContext(), Locale.getDefault());
        addresses = geocoder.getFromLocation(Lat, Long, 1);
        String address = addresses.get(0).getAddressLine(0);

        return address;
    }

    private String GetSpotID()
    {
        Log.d(TAG,"Pagnozzi: returning ID - " + this.ID);
        return this.ID;
    }

}
