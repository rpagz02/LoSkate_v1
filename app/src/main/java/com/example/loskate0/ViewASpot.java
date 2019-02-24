package com.example.loskate0;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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


public class ViewASpot extends Fragment {

    TextView textView_Title;
    TextView textView_Notes;
    ImageView imageView_Picture;
    String tempTitle, tempNotes;
    Bitmap tempImage;
    private static final String TAG = "MyActivity";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_spot, container, false);

        textView_Title = v.findViewById(R.id.View_Spot);
        textView_Notes = v.findViewById(R.id.View_Notes);
        imageView_Picture = v.findViewById(R.id.View_Image);
        Bundle b = getArguments();
        if(b != null)
        {
            String title = b.getString("s");
            QueryDatabaseForSpot(title);
            textView_Title.setText(tempTitle);
            textView_Notes.setText(tempNotes);
            if(tempImage != null)
            imageView_Picture.setImageBitmap(tempImage);
        }
        return v;
    }

    private void QueryDatabaseForSpot(final String spotTitle)
    {
        Log.d(TAG,"Pagnozzi: Got inside query with title -> " + spotTitle);
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
                            tempTitle = mI.getID();
                            tempNotes = mI.getNotes();
                        if(mI.getImage()!= null) tempImage = DecodeBitmap(mI.getImage());

                        if(tempTitle!=null) {
                            textView_Title.setText(tempTitle);
                            textView_Title.setTextColor(Color.argb(255,255,165,0));
                        } else {
                                tempTitle = "No Title Listed";
                            textView_Title.setTextColor(Color.argb(255,255,165,0));
                            textView_Title.setText(tempTitle); }

                            if(tempImage!=null) {
                                imageView_Picture.setImageBitmap(tempImage);
                            }

                        if(tempNotes!=null) {
                            textView_Notes.setText(tempNotes);
                            textView_Notes.setTextColor(Color.argb(255,255,165,0));
                        }
                        else{
                            tempNotes = "No Notes Listed";
                            textView_Notes.setTextColor(Color.argb(255,255,165,0));
                            textView_Notes.setText(tempNotes);

                        }

                            return; }
                    else {
                            tempTitle = "Nothing Found";
                            tempNotes = "Nothing Found"; }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }


    public static Bitmap DecodeBitmap(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
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
