package com.uk.ac.tees.w9207652.eventzz.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.uk.ac.tees.w9207652.eventzz.R;
import com.uk.ac.tees.w9207652.eventzz.helpers.CONST;
import com.uk.ac.tees.w9207652.eventzz.helpers.firebase;


public class companyProfile extends Fragment {

    private TextView gName,gEmail,gPhone,gAddress;
    private ImageView gImageView;
    private Button gLogout;

    private DatabaseReference gProfileRef;

    //progress dialog
    private ProgressDialog gDialog;

    public companyProfile() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_company_profile, container, false);

        gName = (TextView)view.findViewById(R.id.companyProfileFrag_name);
        gEmail = (TextView)view.findViewById(R.id.companyProfileFrag_email);
        gPhone = (TextView)view.findViewById(R.id.companyProfileFrag_phone);
        gAddress = (TextView)view.findViewById(R.id.companyProfileFrag_address);
        gImageView = (ImageView)view.findViewById(R.id.companyProfileFrag_propic);
        gLogout = (Button)view.findViewById(R.id.companyProfileFrag_logout_btn);

        //progress dialog
        gDialog = new ProgressDialog(getContext());
        gDialog.setTitle("Please wait");
        gDialog.setMessage("While we are loading....");


        gLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebase.logoutUser(getActivity());
            }
        });



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        gDialog.show();
        gProfileRef = firebase.gDatabase.getReference().child(CONST.DB_COMPANY).child(firebase.COID);
        gProfileRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    gName.setText(String.valueOf(snapshot.child(CONST.DB_CO_NAME).getValue()));
                    gEmail.setText(String.valueOf(snapshot.child(CONST.DB_CO_EMAIL).getValue()));
                    gPhone.setText(String.valueOf(snapshot.child(CONST.DB_CO_PHONE).getValue()));
                    gAddress.setText(String.valueOf(snapshot.child(CONST.DB_CO_ADDRESS).getValue()));
                    Glide.with(getActivity()).load(String.valueOf(snapshot.child(CONST.DB_CO_WALLURL).getValue())).into(gImageView);
                    gDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}