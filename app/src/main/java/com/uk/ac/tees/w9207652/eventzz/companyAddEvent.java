package com.uk.ac.tees.w9207652.eventzz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.uk.ac.tees.w9207652.eventzz.helpers.CONST;
import com.uk.ac.tees.w9207652.eventzz.helpers.firebase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class companyAddEvent extends AppCompatActivity {

    private TextInputLayout gEName,gEPrice,gEDesc,gEVenue,gESponsors;
    private TextView gEDate;
    private ImageView gEImage;
    private Button gHostEvent;
    private String ENAME,EDESC,ESPONSORS,EPRICE,EVENUE,EDATE,EPICURL = null,EID,E_COID,COID;

    private DatePickerDialog.OnDateSetListener dateSetListener;
    private static final int GALLERY_PICK = 1;
    private Uri IMAGE_URL = null;

    private DatabaseReference gEventRef;
    private StorageReference gProPicRef;
    private UploadTask uploadTask;

    private HashMap<String,Object> eventData = new HashMap<>();

    //progress dialog
    private ProgressDialog gDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_add_event);

        COID = firebase.COID;

        gEName = (TextInputLayout)findViewById(R.id.coAddEventDisplay_eventName);
        gESponsors = (TextInputLayout)findViewById(R.id.coAddEventDisplay_eventSponsor);
        gEPrice = (TextInputLayout)findViewById(R.id.coAddEventDisplay_eventPrice);
        gEDate = (TextView)findViewById(R.id.coAddEventDisplay_eventDate);
        gEDesc = (TextInputLayout)findViewById(R.id.coAddEventDisplay_eventDesc);
        gEVenue = (TextInputLayout)findViewById(R.id.coAddEventDisplay_eventAddress);
        gEImage = (ImageView)findViewById(R.id.coAddEventDisplay_eventImageView);
        gHostEvent = (Button)findViewById(R.id.coAddEventDisplay_register_btn);

        //progress dialog
        gDialog = new ProgressDialog(this);
        gDialog.setTitle("Please wait");
        gDialog.setMessage("While we are loading....");


        gEDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        companyAddEvent.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener,
                        year,month,day
                );
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                gEDate.setText(dayOfMonth+"/"+month+"/"+year);
            }
        };

        gEImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        gHostEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ENAME = gEName.getEditText().getText().toString();
                EDESC = gEDesc.getEditText().getText().toString();
                ESPONSORS = gESponsors.getEditText().getText().toString();
                EPRICE = gEPrice.getEditText().getText().toString();
                EVENUE = gEVenue.getEditText().getText().toString();
                EDATE = gEDate.getText().toString();
                E_COID = COID;

                if (TextUtils.isEmpty(ENAME) || TextUtils.isEmpty(EDESC) || TextUtils.isEmpty(ESPONSORS) || TextUtils.isEmpty(EPRICE) || TextUtils.isEmpty(EVENUE)
                || EDATE.equals("Click Here To Select Date"))
                {
                    Toast.makeText(companyAddEvent.this, "Please Fill All The Details.", Toast.LENGTH_SHORT).show();
                }
                else if (IMAGE_URL.equals(null))
                {
                    Toast.makeText(companyAddEvent.this, "Upload a image for event.", Toast.LENGTH_SHORT).show();
                }else
                {
                    storePPimage(IMAGE_URL);
                }

            }
        });



    }

    private void openGallery() {
        Intent GalleryIntent = new Intent();
        GalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        GalleryIntent.setType("image/*");
        startActivityForResult(GalleryIntent, GALLERY_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK && data != null) {
            IMAGE_URL = data.getData();
            gEImage.setImageURI(IMAGE_URL);
        }
    }

    private void storePPimage(Uri image_url) {
        gDialog.show();
        gProPicRef = firebase.gStorage.getReference().child("US_S" + image_url.getLastPathSegment() + ".jpg");
        uploadTask = gProPicRef.putFile(image_url);
        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return gProPicRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    EPICURL = String.valueOf(task.getResult());
                    hostEvent(ENAME,EDESC,EPRICE,ESPONSORS,EVENUE,EDATE,E_COID,EPICURL);
                }
            }
        });
    }

    private void hostEvent(String ename, String edesc, String eprice, String esponsors, String evenue, String edate, String e_coid, String epicurl) {

        eventData.put(CONST.DB_E_NAME,ename);
        eventData.put(CONST.DB_E_DESC,edesc);
        eventData.put(CONST.DB_E_SPONSORS,esponsors);
        eventData.put(CONST.DB_E_PRICE,eprice);
        eventData.put(CONST.DB_E_VENUE,evenue);
        eventData.put(CONST.DB_E_DATE,edate);
        eventData.put(CONST.DB_E_COID,e_coid);
        eventData.put(CONST.DB_E_PICURL,epicurl);

        gEventRef = firebase.gDatabase.getReference().child(CONST.DB_EVENTS);
        EID = gEventRef.push().getKey();
        gEventRef.child(EID).setValue(eventData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    gDialog.dismiss();
                    toCompanyHome();
                }
                else
                {
                    gDialog.dismiss();
                    Toast.makeText(companyAddEvent.this, "Error.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void toCompanyHome() {

        Intent toCompanyHome = new Intent(companyAddEvent.this,companyHome.class);
        startActivity(toCompanyHome);
        finish();

    }
}