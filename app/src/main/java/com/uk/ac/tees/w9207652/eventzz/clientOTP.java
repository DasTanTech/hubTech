package com.uk.ac.tees.w9207652.eventzz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.uk.ac.tees.w9207652.eventzz.helpers.CONST;
import com.uk.ac.tees.w9207652.eventzz.helpers.firebase;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class clientOTP extends AppCompatActivity {

    //init
    private Pinview gPinView;
    private Button gVerify;
    private TextView gTimer;
    private String FB_PHONE,CID,VERIFICATION_CODE,OTP;

    //time variables
    private long timeLeftInMS = 60000;
    private long timeInterval = 1000;
    private static final String timeElapsed = "Time elapsed";

    //phone auth
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks gCallbacks;
    private PhoneAuthCredential gCredential;

    //firebase
    private DatabaseReference gClientRef;
    private HashMap<String,String> dataMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_o_t_p);
        FB_PHONE = getIntent().getStringExtra("Phone");
        gClientRef = firebase.gDatabase.getReference().child(CONST.DB_CLIENT);

        gPinView = (Pinview)findViewById(R.id.clOTP_pin);
        gVerify = (Button)findViewById(R.id.clOTP_verify_btn);
        gTimer = (TextView)findViewById(R.id.clOTP_timer);




        gVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OTP = gPinView.getValue();
                gCredential = PhoneAuthProvider.getCredential(VERIFICATION_CODE,OTP);
                signInWithPhoneAuthCredential(gCredential);
            }
        });


        gCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                VERIFICATION_CODE = s;
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                FB_PHONE,
                60,
                TimeUnit.SECONDS,
                this,
                gCallbacks
        );
        startTimer();

    }

    //timer methods
    public void startTimer(){
        //countdown timer
        new CountDownTimer(timeLeftInMS, timeInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMS = millisUntilFinished;
                updateTimer();
            }
            @Override
            public void onFinish() {
                gTimer.setText(timeElapsed);
                gTimer.setVisibility(View.VISIBLE);
            }
        }.start();
    }
    public void updateTimer(){
        int seconds = (int) timeLeftInMS % 60000 / 1000;

        String timeLeftText;
        timeLeftText = "00:";
        if (seconds < 10 ) timeLeftText += "0";
        timeLeftText += seconds;

        gTimer.setText(timeLeftText);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){

        firebase.gAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            CID = task.getResult().getUser().getUid();
                            Toast.makeText(clientOTP.this, CID, Toast.LENGTH_SHORT).show();
                            promotUser(CID,FB_PHONE);
                        }
                        else
                        {
                            Toast.makeText(clientOTP.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                });

    }

    private void promotUser(final String cid, final String fb_phone) {
        gClientRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(cid))
                {
                    firebase.gUser = firebase.gAuth.getCurrentUser();
                    firebase.CUR_USER = 0;
                    firebase.CID = cid;
                    toClientHome();
                }
                else
                {
                    createClientInstance(cid,fb_phone);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void createClientInstance(final String cid, String fb_phone) {
        dataMap.put(CONST.DB_C_PHONE,fb_phone);


        gClientRef.child(cid).setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    firebase.gUser = firebase.gAuth.getCurrentUser();
                    firebase.CUR_USER = 0;
                    firebase.CID = cid;
                    toEditClient();
                }
                else
                {
                    Toast.makeText(clientOTP.this, "error at db.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void toEditClient() {
        Intent toEditClient = new Intent(clientOTP.this,clientEditProfile.class);
        startActivity(toEditClient);
        finish();
    }

    private void toClientHome() {

        Intent toClientHome = new Intent(clientOTP.this,clientHome.class);
        startActivity(toClientHome);
        finish();

    }
}