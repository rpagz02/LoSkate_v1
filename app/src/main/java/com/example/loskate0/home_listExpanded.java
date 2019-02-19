package com.example.loskate0;


import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class home_listExpanded extends Fragment {

    TextView textView_Title;
    Button button_Address;
    String spotAddress;
    private static final String TAG = "MyActivity";
    MarkerInfo spotInformation = new MarkerInfo();


    public home_listExpanded()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home_list_expanded, container, false);
        textView_Title = v.findViewById(R.id.spotTitle);
        textView_Title.setTextColor(Color.BLUE);

        button_Address = v.findViewById(R.id.button);
        button_Address.setTextColor(Color.BLACK);
        button_Address.setText(""); // -> make the button text blank until it populates

        Bundle b = getArguments();
        if(b != null)
        {
            String title = b.getString("s");

            textView_Title.setText(title);
            QueryDatabaseForSpot(title);
        }
        return v;
    }

    private void QueryDatabaseForSpot(final String spotTitle)
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
                    if(mI.getID().equals(spotTitle))
                        {
                        Log.d(TAG,"we found a matching ID - PAGNOZZI");
                        Log.d(TAG,mI.getID() + " --- " + spotTitle + "  Pagnozzi");

                        spotInformation.setLatitude(mI.getLatitude());
                        spotInformation.setLongitude(mI.getLongitude());
                            try {
                                SetSpotAddress(spotInformation.getLatitude(), spotInformation.getLongitude());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return;
                        }
                    else
                        {
                        spotAddress = "No Address to retrieve";
                        }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }

        });
        return;
    }

    private void SetSpotAddress(String lat, String lon) throws IOException {
        spotAddress = GetAddressFromLatLng(Double.parseDouble(lat),Double.parseDouble(lon));
        button_Address.setText(spotAddress);
        Log.d(TAG,"Got to SetSpotAddress - Pagnozzi" + lat + "  " + spotAddress);
    }

    private String GetAddressFromLatLng(double Lat, double Long) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this.getContext(), Locale.getDefault());
        addresses = geocoder.getFromLocation(Lat, Long, 1);
        String address = addresses.get(0).getAddressLine(0);

        return address;
    }
}
