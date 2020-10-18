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
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.uk.ac.tees.w9207652.eventzz.fragments.clientProfile;
import com.uk.ac.tees.w9207652.eventzz.fragments.companyProfile;
import com.uk.ac.tees.w9207652.eventzz.fragments.registeredEvents;
import com.uk.ac.tees.w9207652.eventzz.helpers.firebase;

public class companyHome extends AppCompatActivity {

    //init
    private Toolbar gToolbar;
    private TextView gExtraBtn;
    private BottomNavigationView gBottomNavView;
    private final FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_home);

        gExtraBtn = (TextView) findViewById(R.id.coHome_profileChange_btn);
        gToolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.coHome_toolbar);
        setSupportActionBar(gToolbar);

        gExtraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toEditComapnyProfile();
            }
        });

        final Fragment coHome = new com.uk.ac.tees.w9207652.eventzz.fragments.companyHome();
        final Fragment coProfile = new companyProfile();
        final Fragment registerEvents = new registeredEvents();

        gBottomNavView = (BottomNavigationView)findViewById(R.id.coHome_bottomNavView);
        gBottomNavView.setSelectedItemId(R.id.bnv_home);
        fm.beginTransaction().replace(R.id.coHome_frameLayout,coHome).commit();

        gBottomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fg = null;
                switch (item.getItemId())
                {
                    case R.id.bnv_home:
                    {
                        gExtraBtn.setVisibility(View.GONE);
                        gToolbar.setTitle("Tech Hub");
                        fg = coHome;
                    }
                    break;
                    case R.id.bnv_event:
                    {
                        gExtraBtn.setVisibility(View.GONE);
                        if (firebase.CUR_USER == 0)
                            gToolbar.setTitle("Registered Events");
                        else
                            gToolbar.setTitle("Registered Users");
                        fg = registerEvents;
                    }
                    break;
                    case R.id.bnv_profile:
                    {
                        gExtraBtn.setVisibility(View.VISIBLE);
                        gToolbar.setTitle("Profile");
                        fg = coProfile;
                    }
                    break;

                }
                fm.beginTransaction().replace(R.id.coHome_frameLayout,fg).commit();
                return true;
            }
        });
    }

    private void toEditComapnyProfile() {
        Intent toEditCompanyProfile = new Intent(companyHome.this,companyEditProfile.class);
        startActivity(toEditCompanyProfile);
    }
}