package com.example.loskate0;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class home_listExpanded extends Fragment {

    TextView textView;

    public home_listExpanded() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home_list_expanded, container, false);
        textView = v.findViewById(R.id.listText);
        Bundle b = getArguments();
        if(b != null)
        {
            String s = b.getString("s");
            textView.setText(s);
        }
        return v;
    }

}
