package com.uk.ac.tees.w9207652.eventzz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.uk.ac.tees.w9207652.eventzz.helpers.CONST;
import com.uk.ac.tees.w9207652.eventzz.helpers.firebase;

public class MainActivity extends AppCompatActivity {

    private static final long TIME_DELAY = 2000;
    private Handler gHandler;

    private DatabaseReference gCheckRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gHandler = new Handler();
        firebase.gUser = firebase.gAuth.getCurrentUser();
        if (firebase.gUser == null)
        {
            gHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toClientLogin();
                }
            },TIME_DELAY);
        }
        else
        {
            checkUser(firebase.gUser.getUid());
        }


    }

    private void checkUser(final String ID) {
        gCheckRef = firebase.gDatabase.getReference().child(CONST.DB_CLIENT);
        gCheckRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(ID))
                {
                    firebase.CUR_USER = 0;
                    firebase.CID = ID;
                    gHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toClientHome();
                        }
                    },TIME_DELAY);
                }
                else
                {
                    firebase.CUR_USER = 1;
                    firebase.COID = ID;
                    gHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toCompanyHome();
                        }
                    },TIME_DELAY);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void toCompanyHome() {
        Intent toCompanyHome = new Intent(MainActivity.this,companyHome.class);
        startActivity(toCompanyHome);
        finish();
    }

    private void toClientHome() {
        Intent toClientHome = new Intent(MainActivity.this,clientHome.class);
        startActivity(toClientHome);
        finish();
    }

    private void toClientLogin() {
        Intent toClientLogin = new Intent(MainActivity.this,clientLogin.class);
        startActivity(toClientLogin);
        finish();
    }
}