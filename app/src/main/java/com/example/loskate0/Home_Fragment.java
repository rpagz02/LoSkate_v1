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

import org.w3c.dom.Text;

import java.util.ArrayList;


public class Home_Fragment extends Fragment
{

   ListView lv;
   ArrayList<String> al;
   ArrayAdapter<String> aa;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_home,container, false);
        lv = v.findViewById(R.id.listview1);
        al = new ArrayList<>();
        aa = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, al)
        {
            @Nullable
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView listItem = (TextView)super.getView(position,convertView,parent);
                listItem.setTextColor(Color.WHITE);
                return listItem;
            }
        };

        lv.setAdapter(aa);
        al.add("Spot 1");
        al.add("Spot 2");
        al.add("Spot 3");
        al.add("Spot 4");
        al.add("Spot 5");
        al.add("Spot 6");
        al.add("Spot 7");
        al.add("Spot 8");
        al.add("Spot 9");
        al.add("Spot 10");

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

