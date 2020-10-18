package com.uk.ac.tees.w9207652.eventzz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.uk.ac.tees.w9207652.eventzz.fragments.*;
import com.uk.ac.tees.w9207652.eventzz.helpers.firebase;

public class clientHome extends AppCompatActivity {

    //init
    private Toolbar gToolbar;
    private TextView gExtraBtn;
    private BottomNavigationView gBottomNavView;
    private FrameLayout gHomeFrame;
    private final FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home);

        gExtraBtn = (TextView) findViewById(R.id.home_profileChange_btn);
        gToolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(gToolbar);

        gExtraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toEditProfile();
            }
        });

        final Fragment clHome = new com.uk.ac.tees.w9207652.eventzz.fragments.clientHome();
        final Fragment clProfile = new clientProfile();
        final Fragment registerEvents = new registeredEvents();

        gBottomNavView = (BottomNavigationView)findViewById(R.id.home_bottomNavView);
        gHomeFrame = (FrameLayout)findViewById(R.id.home_frameLayout);
        gBottomNavView.setSelectedItemId(R.id.bnv_home);
        fm.beginTransaction().replace(R.id.home_frameLayout,clHome).commit();

        gBottomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fg = null;
                switch (item.getItemId())
                {
                    case R.id.bnv_home:
                    {
                        gExtraBtn.setVisibility(View.GONE);
                        gToolbar.setTitle("Hub Tech");
                        fg = clHome;
                    }
                    break;
                    case R.id.bnv_event:
                    {
                        gExtraBtn.setVisibility(View.GONE);
                        if (firebase.CUR_USER == 0)
                            gToolbar.setTitle("Registered Events");
                        else
                            gToolbar.setTitle("Your Hosted Events");
                        fg = registerEvents;
                    }
                    break;
                    case R.id.bnv_profile:
                    {
                        gExtraBtn.setVisibility(View.VISIBLE);
                        gToolbar.setTitle("Profile");
                        fg = clProfile;
                    }
                    break;

                }
                fm.beginTransaction().replace(R.id.home_frameLayout,fg).commit();
                return true;
            }
        });




    }


    private void toEditProfile() {
        Intent toEditCompanyProfile = new Intent(clientHome.this,clientEditProfile.class);
        startActivity(toEditCompanyProfile);
    }
}