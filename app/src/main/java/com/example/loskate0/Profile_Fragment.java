package com.example.loskate0;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;


public class Profile_Fragment extends Fragment {

    private TextView text;
    FirebaseAuth mAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_, container, false);

        mAuth = FirebaseAuth.getInstance();
        text = v.findViewById(R.id.user);
        text.setText(mAuth.getCurrentUser().getEmail());


        return v;
    }



}
