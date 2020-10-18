package com.uk.ac.tees.w9207652.eventzz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;
import com.uk.ac.tees.w9207652.eventzz.helpers.CONST;
import com.uk.ac.tees.w9207652.eventzz.helpers.firebase;

import java.util.HashMap;

public class clientLogin extends AppCompatActivity {

    //init
    private Button gGoogleSignIn,gPhoneAuth;
    private EditText gPhoneNumber;
    private TextView gCoLogin;
    private String PHONE,CID;
    private CountryCodePicker gCCP;

    //googleSignIn
    private GoogleSignInClient gGoogleSignInClient;
    private GoogleApiClient gGoogleApiClient;
    private static final int RC_SIGN_IN = 123;

    //firebase
    private DatabaseReference gClientRef;
    private HashMap<String,String> dataMap = new HashMap<>();

    //progress dialog
    private ProgressDialog gDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_login);

        //functions
        googleSignInInstance();
        gClientRef = firebase.gDatabase.getReference().child(CONST.DB_CLIENT);

        //init
        gGoogleSignIn = (Button)findViewById(R.id.ciLogin_googleSignIn_btn);
        gPhoneAuth = (Button)findViewById(R.id.ciLogin_phoneAuth_btn);
        gCoLogin = (TextView)findViewById(R.id.ciLogin_coLogin_tv);
        gPhoneNumber = (EditText)findViewById(R.id.ciLogin_phone_et);
        gCCP = (CountryCodePicker)findViewById(R.id.ccp);
        gCCP.registerCarrierNumberEditText(gPhoneNumber);

        //progress dialog
        gDialog = new ProgressDialog(this);
        gDialog.setTitle("Please wait");
        gDialog.setMessage("While we are loading....");


        //googleSignIn
        gGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
            }
        });

        gPhoneAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PHONE = gCCP.getFormattedFullNumber();
                toOTP(PHONE);
            }
        });

        gCoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toCompanyLogin();
            }
        });
    }

    private void toCompanyLogin() {
        Intent toCompanyLogin = new Intent(clientLogin.this,companyLogin.class);
        startActivity(toCompanyLogin);
    }

    private void toOTP(String phone) {
        Intent toOTP = new Intent(clientLogin.this,clientOTP.class);
        toOTP.putExtra("Phone",phone);
        startActivity(toOTP);
    }

    private void googleSignInInstance() {
       GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
               .requestIdToken(getString(R.string.default_web_client_id))
               .requestEmail()
               .build();

       gGoogleSignInClient = GoogleSignIn.getClient(this,gso);
    }

    private void googleSignIn() {

        Intent signInIntent = gGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            }catch (ApiException e){}
        }

    }

    private void firebaseAuthWithGoogle(String idToken) {
        gDialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null);
        firebase.gAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            CID = task.getResult().getUser().getUid();
                            String email = task.getResult().getUser().getEmail();
                            String name = task.getResult().getUser().getDisplayName();
                            firebase.gUser = firebase.gAuth.getCurrentUser();
                            firebase.CUR_USER = 0;
                            firebase.CID = CID;
                            promotUser(CID,email,name);

                        }
                        else
                        {
                            gDialog.dismiss();
                            Toast.makeText(clientLogin.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void promotUser(final String cid, final String email, final String name) {


        gClientRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(cid))
                {
                    gDialog.dismiss();
                    toClientHome();
                }
                else
                {
                    createClientInstance(cid,email,name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void createClientInstance(String cid, String email, String name) {

        dataMap.put(CONST.DB_C_EMAIL,email);
        dataMap.put(CONST.DB_C_NAME,name);

        gClientRef.child(cid).setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    gDialog.dismiss();
                    toClientEditProfile();
                }
                else
                {
                    gDialog.dismiss();
                    Toast.makeText(clientLogin.this, "error at db.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void toClientEditProfile() {
        Intent toClientEditProfile = new Intent(clientLogin.this,clientEditProfile.class);
        startActivity(toClientEditProfile);
        finish();
    }

    private void toClientHome() {
        Intent toClientHome = new Intent(clientLogin.this,clientHome.class);
        startActivity(toClientHome);
        finish();
    }
}