package com.uk.ac.tees.w9207652.eventzz.helpers;

import android.app.Activity;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.uk.ac.tees.w9207652.eventzz.R;
import com.uk.ac.tees.w9207652.eventzz.clientLogin;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class firebase {

    static GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(String.valueOf(R.string.default_web_client_id))
            .requestEmail()
            .build();

    public static final FirebaseAuth gAuth = FirebaseAuth.getInstance();
    public static final FirebaseStorage gStorage = FirebaseStorage.getInstance();
    public static final FirebaseDatabase gDatabase = FirebaseDatabase.getInstance();
    public static FirebaseUser gUser = null;
    public static int CUR_USER;//0-client,1-company
    public static String CID = null,COID = null;

    public static void logoutUser(Activity activity) {

        firebase.gUser = null;
        GoogleSignInClient signInClient = GoogleSignIn.getClient(activity, gso);
        if (signInClient != null) {
            signInClient.signOut();
        }
        firebase.gAuth.signOut();
        Intent toLogin = new Intent(activity, clientLogin.class);
        activity.startActivity(toLogin);
        activity.finish();
    }



}
