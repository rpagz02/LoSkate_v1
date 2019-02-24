package com.example.loskate0;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;



public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(navListener);

        //This starts app with MAP FRAGMENT
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Map_Fragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId())
            {
                case R.id.navigation_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Home_Fragment()).commit();
                    break;
                case R.id.navigation_map:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Map_Fragment()).commit();
                    break;
                case R.id.navigation_dashboard:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Spots_Fragment()).commit();
                    break;
            }
            return true;
        }
    };


    // Home Page Method -> Fragment transaction from Home to Home Expanded
    public void Frag_Trans_Home(String title)
    {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ViewASpot hlf = new ViewASpot();
        Bundle b2 = new Bundle();
        b2.putString("s", title);
        hlf.setArguments(b2);
        ft.replace(R.id.fragment_container, hlf);
        ft.commit();
    }

    // Map Fragment Method -> Fragment transaction from Map to CreateASpot
    public void Frag_Trans_Info(String SpotID)
    {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        CreateSpot_Fragment cas = new CreateSpot_Fragment();
        Bundle bun2 = new Bundle();
        bun2.putString("d", SpotID);
        cas.setArguments(bun2);
        ft.replace(R.id.fragment_container, cas);
        ft.commit();
    }

    public void Frag_Trans_Map()
    {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Map_Fragment cas = new Map_Fragment();
        ft.replace(R.id.fragment_container, cas);
        ft.commit();
    }

}
