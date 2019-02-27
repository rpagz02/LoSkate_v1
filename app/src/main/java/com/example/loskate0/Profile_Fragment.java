package com.example.loskate0;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.List;


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
private DatabaseReference ref;
private List<MarkerInfo> mSpots;
private RecyclerView recyclerView;
private RecyclerViewAdapter mAdapter;

//////////////////////////////////////////////////////////
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_, container, false);

        recyclerView = v.findViewById(R.id.recycleView);
        mSpots = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mAdapter = new RecyclerViewAdapter(mSpots);
        recyclerView.setAdapter(mAdapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference("Users");
        // Above this line is all RV stuff ^

        // Below this line is all LV stuff
        mAuth = FirebaseAuth.getInstance();
        text = v.findViewById(R.id.user);
        text.setText(mAuth.getCurrentUser().getEmail());

        PopulateRecyclerFromDataBase();


        return v;
    }

    private void PopulateRecyclerFromDataBase()
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
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren())
                {
                    MarkerInfo mI = singleSnapshot.getValue(MarkerInfo.class);
                    //
                    mSpots.add(mI);
                    recyclerView.scrollToPosition(mSpots.size() - 1);
                    mAdapter.notifyItemInserted(mSpots.size() - 1);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

}
