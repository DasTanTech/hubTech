package com.uk.ac.tees.w9207652.eventzz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.uk.ac.tees.w9207652.eventzz.helpers.CONST;
import com.uk.ac.tees.w9207652.eventzz.helpers.firebase;

public class companyEventDisplay extends AppCompatActivity {

    private TextView gEName,gEDate,gEMonth;
    private TextInputLayout gESponsor,gEPrice,gEDesc,gEVenue;
    private ImageView gEPic;

    private DatabaseReference gEventRef;
    private String EID;

    //progress dialog
    private ProgressDialog gDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_event_display);
        EID = getIntent().getStringExtra("EID");

        gEName = (TextView)findViewById(R.id.coEventDisplay_eventName);
        gEDate = (TextView)findViewById(R.id.coEventDisplay_eventDate);
        gEMonth = (TextView)findViewById(R.id.coEventDisplay_eventDay);
        gESponsor = (TextInputLayout)findViewById(R.id.coEventDisplay_eventSponsor);
        gEPrice = (TextInputLayout)findViewById(R.id.coEventDisplay_eventRegFee);
        gEDesc = (TextInputLayout)findViewById(R.id.coEventDisplay_eventDesc);
        gEVenue = (TextInputLayout)findViewById(R.id.coEventDisplay_eventAddress);
        gEPic = (ImageView)findViewById(R.id.coEventDisplay_eventImageView);

        //progress dialog
        gDialog = new ProgressDialog(this);
        gDialog.setTitle("Please wait");
        gDialog.setMessage("While we are loading....");

        loadEventData(EID);

    }

    private void loadEventData(String eid) {
        gDialog.show();
        gEventRef = firebase.gDatabase.getReference().child(CONST.DB_EVENTS).child(eid);
        gEventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    gEName.setText(String.valueOf(snapshot.child(CONST.DB_E_NAME).getValue()));
                    gESponsor.getEditText().setText(String.valueOf(snapshot.child(CONST.DB_E_SPONSORS).getValue()));
                    gEPrice.getEditText().setText("£ "+String.valueOf(snapshot.child(CONST.DB_E_PRICE).getValue())+" /per person.");
                    gEDesc.getEditText().setText(String.valueOf(snapshot.child(CONST.DB_E_DESC).getValue()));
                    gEVenue.getEditText().setText(String.valueOf(snapshot.child(CONST.DB_E_VENUE).getValue()));

                    String date = String.valueOf(snapshot.child(CONST.DB_E_DATE).getValue());
                    String[] dates = date.split("/");

                    gEDate.setText(dates[0]);
                    gEMonth.setText(getMonth(dates[1]));

                    Glide.with(companyEventDisplay.this).load(String.valueOf(snapshot.child(CONST.DB_E_PICURL).getValue()))
                            .into(gEPic);
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