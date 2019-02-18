package com.example.loskate0;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;



public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(navListener);

        //This starts app with MAP FRAGMENT
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Map_Fragment()).commit();
        // This starts app with DASHBOARD FRAGMENT
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Dashboard_Fragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId())
            {
                case R.id.navigation_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Home_Fragment()).commit();
                    break;
                case R.id.navigation_map:
                    {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Map_Fragment()).commit();
                    break;
                    }
                case R.id.navigation_dashboard:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Dashboard_Fragment()).commit();
                    break;
            }
            return true;
        }
    };


    // Home Page Method
    public void fl(String s)
    {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        home_listExpanded hlf = new home_listExpanded();
        Bundle b2 = new Bundle();
        b2.putString("s", s);
        hlf.setArguments(b2);
        ft.replace(R.id.fragment_container, hlf);
        ft.commit();
    }
}
