package com.example.loskate0;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;



import java.util.ArrayList;


public class Home_Fragment extends Fragment {
//////////////////////////////////////////////////////////
//                  VARIABLES                           //
//////////////////////////////////////////////////////////
   ListView lv;
   ArrayList<String> al;
   ArrayAdapter<String> aa;
   private static final String TAG = "MyActivity";
//////////////////////////////////////////////////////////

    // On Create we populate the list
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_home,container, false);
        lv = v.findViewById(R.id.listview1);
        al = new ArrayList<>();

        PopulateListFromDataBase();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = al.get(position);
                MainActivity mnl = (MainActivity)getActivity();
                mnl.fl(s);
            }});
        return v;
    }

    // Populate the list from the database here
    private void PopulateListFromDataBase()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query1 = reference.child("spots").orderByKey();

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
                    al.add(listItem);
                }
                aa = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, al)
                {
                    @Nullable
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent)
                    {
                        TextView listItem = (TextView)super.getView(position,convertView,parent);
                        listItem.setTextColor(Color.WHITE);
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

