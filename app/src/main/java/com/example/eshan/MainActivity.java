package com.example.eshan;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.os.Bundle;
import android.view.MenuItem;
//import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                selectedFragment =new HomeFragment();

                System.out.println(item.getItemId());
//                Fragment selectedFragment = null;
                if (item.getItemId() == R.id.navigation_home) {
                    selectedFragment = new HomeFragment();
                } else if (item.getItemId() == R.id.navigation_dashboard) {
                    selectedFragment = new DashboardFragment();
                } else if (item.getItemId() == R.id.navigation_notifications) {
                    selectedFragment = new NotificationsFragment();
                } else if (item.getItemId() == R.id.navigation_profile) {
                    selectedFragment = new ProfileFragment();
                } else {
                    throw new IllegalArgumentException("Unexpected value: " + item.getItemId());
                }
//                switch (item.getItemId()) {
//                    case 1000005 :
//                        selectedFragment = new HomeFragment();
//                        break;
//                    case 1000001:
//                        selectedFragment = new DashboardFragment();
//                        break;
//                    case 1000002:
//                        selectedFragment = new NotificationsFragment();
//                        break;
//                    case 1000004:
//                        selectedFragment = new ProfileFragment();
//                        break;
//                }
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, selectedFragment).commit();
                return true;
            }

        });

        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }
}