package com.example.loskate0;

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

import java.util.ArrayList;


public class Home_Fragment extends Fragment
{

    // Variables Declarations for the list and string array
   ListView lv;
   ArrayList<String> al;
   ArrayAdapter<String> aa;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_home,container, false);
        // initliaze the declared variables
        lv = (ListView)v.findViewById(R.id.listview1);
        al = new ArrayList<String>();
        aa = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, al);
        lv.setAdapter(aa);
        al.add("Item 1");
        al.add("Item 2");
        al.add("Item 3");
        al.add("Item 4");
        al.add("Item 5");
        al.add("Item 6");
        al.add("Item 7");
        al.add("Item 8");
        al.add("Item 9");
        al.add("Item 10");

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = al.get(position);
                MainActivity mnl = (MainActivity)getActivity();
                mnl.fl(s);
            }
        });


        return v;
    }





}

