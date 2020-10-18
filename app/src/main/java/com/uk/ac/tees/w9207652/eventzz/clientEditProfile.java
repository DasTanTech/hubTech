package com.uk.ac.tees.w9207652.eventzz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.uk.ac.tees.w9207652.eventzz.helpers.CONST;
import com.uk.ac.tees.w9207652.eventzz.helpers.firebase;

import java.util.HashMap;

public class clientEditProfile extends AppCompatActivity {

    //init
    private TextInputLayout gName,gEmail,gPhone;
    private ImageView gProPic;
    private Button gUpdate;
    private String NAME,EMAIL,PHONE,PROPICURL;
    private static final int GALLERY_PICK = 1;
    private Uri IMAGE_URL = null;
    private int check = 0;

    //firebase
    private DatabaseReference gClientRef;
    private HashMap<String,Object> dataMap = new HashMap<>();
    private StorageReference gProPicRef;
    private UploadTask uploadTask;

    //progress dialog
    private ProgressDialog gDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_edit_profile);

        gClientRef = firebase.gDatabase.getReference().child(CONST.DB_CLIENT);

        gName = (TextInputLayout) findViewById(R.id.ciEditProfile_name_et);
        gEmail = (TextInputLayout)findViewById(R.id.ciEditProfile_email_et);
        gPhone = (TextInputLayout)findViewById(R.id.ciEditProfile_phone_et);
        gProPic = (ImageView)findViewById(R.id.ciEditProfile_proPic_IV);
        gUpdate = (Button)findViewById(R.id.ciEditProfile_update_btn);

        //progress dialog
        gDialog = new ProgressDialog(this);
        gDialog.setTitle("Please wait");
        gDialog.setMessage("While we are loading....");

        loadClientData();

        gProPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGalleryIntent();
            }
        });

        gUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NAME = gName.getEditText().getText().toString();
                EMAIL = gEmail.getEditText().getText().toString();
                PHONE = gPhone.getEditText().getText().toString();
                if (NAME.isEmpty() || EMAIL.isEmpty() || PHONE.isEmpty())
                {
                    Toast.makeText(clientEditProfile.this, "Fill all the details.", Toast.LENGTH_SHORT).show();
                }
                else if (IMAGE_URL == null)
                {
                    if (PROPICURL == null)
                        Toast.makeText(clientEditProfile.this, "Select any Image.", Toast.LENGTH_SHORT).show();
                    else {
                        gDialog.show();
                        updateData(NAME, EMAIL, PHONE, PROPICURL);
                    }
                }
                else
                {
                    storeImage(IMAGE_URL);
                }
            }
        });


    }

    private void storeImage(Uri image_url) {
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
                    PROPICURL = String.valueOf(task.getResult());
                    updateData(NAME,EMAIL,PHONE,PROPICURL);
                }
            }
        });
    }

    private void updateData(String name, String email, String phone, String PROPICURL) {

        dataMap.put(CONST.DB_C_NAME,name);
        dataMap.put(CONST.DB_C_EMAIL,email);
        dataMap.put(CONST.DB_C_PHONE,phone);
        dataMap.put(CONST.DB_C_PROPICURL,PROPICURL);

        gClientRef.child(firebase.CID).updateChildren(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    gDialog.dismiss();
                    toClientHome();
                }
                else
                {
                    gDialog.dismiss();
                    Toast.makeText(clientEditProfile.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void toClientHome() {
        Intent toClientHome = new Intent(clientEditProfile.this,clientHome.class);
        toClientHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(toClientHome);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    private void openGalleryIntent() {
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
            gProPic.setImageURI(IMAGE_URL);
        }
    }

    private void loadClientData(){
        gDialog.show();
        gClientRef.child(firebase.CID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(CONST.DB_C_PROPICURL))
                {
                    gName.getEditText().setText(String.valueOf(snapshot.child(CONST.DB_C_NAME).getValue()));
                    gEmail.getEditText().setText(String.valueOf(snapshot.child(CONST.DB_C_EMAIL).getValue()));
                    gPhone.getEditText().setText(String.valueOf(snapshot.child(CONST.DB_C_PHONE).getValue()));
                    PROPICURL = String.valueOf(snapshot.child(CONST.DB_C_PROPICURL).getValue());
                    Glide.with(clientEditProfile.this).load(String.valueOf(snapshot.child(CONST.DB_C_PROPICURL).getValue())).into(gProPic);

                }
                gDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}