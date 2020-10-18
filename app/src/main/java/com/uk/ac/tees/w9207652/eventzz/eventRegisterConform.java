package com.uk.ac.tees.w9207652.eventzz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.uk.ac.tees.w9207652.eventzz.helpers.CONST;
import com.uk.ac.tees.w9207652.eventzz.helpers.firebase;

public class eventRegisterConform extends AppCompatActivity {

    private TextView gDate,gEName,gBID,gUEmail;
    private TextInputLayout gSponsor,gVenue;
    private String RID;

    private DatabaseReference gRegisterRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_register_conform);
        RID = getIntent().getStringExtra("RID");


        gDate = (TextView)findViewById(R.id.registerConform_date);
        gEName = (TextView)findViewById(R.id.registerConform_name);
        gBID = (TextView)findViewById(R.id.registerConform_bookingId);
        gUEmail = (TextView)findViewById(R.id.registerConform_userEmail);
        gSponsor = (TextInputLayout)findViewById(R.id.registerConform_sponsors);
        gVenue = (TextInputLayout)findViewById(R.id.registerConform_eventAddress);

        loadRegisterData();



    }

    private void loadRegisterData() {

        gRegisterRef = firebase.gDatabase.getReference().child(CONST.DB_CLIENT).child(firebase.CID).child(CONST.DB_REGISTERS).child(RID);
        gRegisterRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gEName.setText(String.valueOf(snapshot.child(CONST.DB_R_ENAME).getValue()));
                gBID.setText(String.valueOf(snapshot.child(CONST.DB_R_RID).getValue()));
                gUEmail.setText(String.valueOf(snapshot.child(CONST.DB_R_UEMAIL).getValue()));
                gDate.setText(String.valueOf(snapshot.child(CONST.DB_R_EDATE).getValue()));

                gSponsor.getEditText().setText(String.valueOf(snapshot.child(CONST.DB_R_ESPONSOR).getValue()));
                gVenue.getEditText().setText(String.valueOf(snapshot.child(CONST.DB_R_EVENUE).getValue()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}