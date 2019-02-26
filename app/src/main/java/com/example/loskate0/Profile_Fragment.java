package com.example.loskate0;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Profile_Fragment extends Fragment {

//////////////////////////////////////////////////////////
//                  VARIABLES                           //
//////////////////////////////////////////////////////////
    ListView lv;
    ArrayList<String> al;
    ArrayAdapter<String> aa;
    private static final String TAG = "MyActivity";
    private TextView text;
    FirebaseAuth mAuth;
//////////////////////////////////////////////////////////
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_, container, false);

        mAuth = FirebaseAuth.getInstance();
        text = v.findViewById(R.id.user);
        text.setText(mAuth.getCurrentUser().getEmail());
        //

        lv = v.findViewById(R.id.listview2);
        al = new ArrayList<>();
        PopulateListFromDataBase();



        return v;
    }


    private void PopulateListFromDataBase()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        Log.d(TAG,"Pagnozzi- ID = " + mAuth.getCurrentUser().getUid());
        String ID = mAuth.getCurrentUser().getUid();
        Query query1 = reference.child(ID).orderByKey();

        query1.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                al.clear();
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren())
                {
                    MarkerInfo mI = singleSnapshot.getValue(MarkerInfo.class);
                    String listItem = mI.getID();
                    Log.d(TAG,"Pagnozzi- ID = " + mI.getID());
                    al.add(listItem);
                }
                aa = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, al)
                {
                    @Nullable
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent)
                    {
                        TextView listItem = (TextView)super.getView(position,convertView,parent);
                        listItem.setTextColor(Color.BLACK);
                        return listItem;
                    }
                };
                lv.setAdapter(aa);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }


}
