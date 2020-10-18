package com.uk.ac.tees.w9207652.eventzz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.uk.ac.tees.w9207652.eventzz.helpers.CONST;
import com.uk.ac.tees.w9207652.eventzz.helpers.firebase;

import java.util.HashMap;

public class eventDisplay extends AppCompatActivity {

    private TextView gEName,gEDate,gEMonth;
    private TextInputLayout gESponsor,gEPrice,gEDesc,gEVenue;
    private ImageView gEPic;
    private Button gRegister;

    private String RID,COID,CID,UNAME,UEMAIL,ENAME,ESPONSOR,EVENUE,EDATE;
    private int check = 0;
    private HashMap<String, String> registrationMap = new HashMap<>();

    private DatabaseReference gEventRef,gRegistrationRef,gUserRef,gRC,gRE,gRCO;
    private String EID;

    //progress dialog
    private ProgressDialog gDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_display);
        EID = getIntent().getStringExtra("EID");

        gEName = (TextView)findViewById(R.id.eventDisplay_eventName);
        gEDate = (TextView)findViewById(R.id.eventDisplay_eventDate);
        gEMonth = (TextView)findViewById(R.id.eventDisplay_eventDay);
        gESponsor = (TextInputLayout)findViewById(R.id.eventDisplay_eventSponsor);
        gEPrice = (TextInputLayout)findViewById(R.id.eventDisplay_eventRegFee);
        gEDesc = (TextInputLayout)findViewById(R.id.evenbDisplay_eventDesc);
        gEVenue = (TextInputLayout)findViewById(R.id.eventDisplay_eventAddress);
        gEPic = (ImageView)findViewById(R.id.eventDisplay_eventImageView);
        gRegister = (Button)findViewById(R.id.eventDisplay_register_btn);

        //progress dialog
        gDialog = new ProgressDialog(this);
        gDialog.setTitle("Please wait");
        gDialog.setMessage("While we are loading....");

        loadEventData(EID);

        gRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check == 0)
                    startRegistration();
                else
                    toRegisterConform(RID);
            }
        });
    }

    private void startRegistration() {
        gDialog.show();
        gRegistrationRef = firebase.gDatabase.getReference().child(CONST.DB_REGISTERS);
        RID = gRegistrationRef.push().getKey();
        registrationMap.put(CONST.DB_R_RID,RID);
        registrationMap.put(CONST.DB_R_CID,firebase.CID);
        registrationMap.put(CONST.DB_R_COID,COID);
        registrationMap.put(CONST.DB_R_EID,EID);
        registrationMap.put(CONST.DB_R_ENAME,ENAME);
        registrationMap.put(CONST.DB_R_ESPONSOR,ESPONSOR);
        registrationMap.put(CONST.DB_R_EDATE,EDATE);
        registrationMap.put(CONST.DB_R_EVENUE,EVENUE);

        getUserDetails();

    }

    private void updateRegistration() {
        gRC = firebase.gDatabase.getReference().child(CONST.DB_CLIENT).child(firebase.CID).child(CONST.DB_REGISTERS).child(RID);
        gRC.setValue(registrationMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    gRE = firebase.gDatabase.getReference().child(CONST.DB_EVENTS).child(EID).child(CONST.DB_REGISTERS).child(RID);
                    gRE.setValue(registrationMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                gRCO = firebase.gDatabase.getReference().child(CONST.DB_COMPANY).child(COID).child(CONST.DB_REGISTERS).child(RID);
                                gRCO.setValue(registrationMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            gDialog.dismiss();
                                            toRegisterConform(RID);
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }

    private void toRegisterConform(String RID) {
        Intent toRegisterConform = new Intent(eventDisplay.this,eventRegisterConform.class);
        toRegisterConform.putExtra("RID",RID);
        startActivity(toRegisterConform);
        finish();
    }

    private void getUserDetails() {

        gUserRef = firebase.gDatabase.getReference().child(CONST.DB_CLIENT).child(firebase.CID);
        gUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                registrationMap.put(CONST.DB_R_UNAME,String.valueOf(snapshot.child(CONST.DB_C_NAME).getValue()));
                registrationMap.put(CONST.DB_R_UEMAIL,String.valueOf(snapshot.child(CONST.DB_C_EMAIL).getValue()));
                updateRegistration();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadEventData(String eid) {
        gDialog.show();
        gEventRef = firebase.gDatabase.getReference().child(CONST.DB_EVENTS).child(eid);
        gEventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    COID = String.valueOf(snapshot.child(CONST.DB_E_COID).getValue());
                    ENAME = String.valueOf(snapshot.child(CONST.DB_E_NAME).getValue());
                    gEName.setText(String.valueOf(snapshot.child(CONST.DB_E_NAME).getValue()));
                    ESPONSOR = String.valueOf(snapshot.child(CONST.DB_E_SPONSORS).getValue());
                    gESponsor.getEditText().setText(String.valueOf(snapshot.child(CONST.DB_E_SPONSORS).getValue()));
                    gEPrice.getEditText().setText("£ "+String.valueOf(snapshot.child(CONST.DB_E_PRICE).getValue())+" /per person.");
                    gEDesc.getEditText().setText(String.valueOf(snapshot.child(CONST.DB_E_DESC).getValue()));
                    EVENUE = String.valueOf(snapshot.child(CONST.DB_E_VENUE).getValue());
                    gEVenue.getEditText().setText(String.valueOf(snapshot.child(CONST.DB_E_VENUE).getValue()));

                    EDATE = String.valueOf(snapshot.child(CONST.DB_E_DATE).getValue());
                    String[] dates = EDATE.split("/");

                    gEDate.setText(dates[0]);
                    gEMonth.setText(getMonth(dates[1]));

                    Glide.with(eventDisplay.this).load(String.valueOf(snapshot.child(CONST.DB_E_PICURL).getValue()))
                            .into(gEPic);

                    if (snapshot.child(CONST.DB_REGISTERS).hasChildren()) {
                        for (DataSnapshot registers : snapshot.child(CONST.DB_REGISTERS).getChildren()) {
                            String CCID = String.valueOf(registers.child(CONST.DB_R_CID).getValue());
                            if (CCID.equals(firebase.CID)) {
                                gRegister.setText("Show Registration");
                                RID = registers.getKey();
                                check = 1;
                            }else
                            {
                                gRegister.setText("Register");
                                check = 0;
                            }
                        }
                    }

                    gDialog.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private String getMonth(String date) {
        String mon = null;

        switch (date)
        {
            case "1" :  mon = "Jan";
                break;
            case "2" :  mon = "Feb";
                break;
            case "3" :  mon = "Mar";
                break;
            case "4" :  mon = "Apr";
                break;
            case "5" :  mon = "May";
                break;
            case "6" :  mon = "Jun";
                break;
            case "7" :  mon = "Jul";
                break;
            case "8" :  mon = "Aug";
                break;
            case "9" :  mon = "Sep";
                break;
            case "10" :  mon = "Oct";
                break;
            case "11" :  mon = "Nov";
                break;
            case "12" :  mon = "Dec";
                break;
        }
        return mon;
    }
}