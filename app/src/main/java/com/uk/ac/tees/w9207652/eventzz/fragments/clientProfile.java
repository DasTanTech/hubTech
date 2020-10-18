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


public class clientProfile extends Fragment {
    private TextView gName,gEmail,gPhone;
    private ImageView gImageView;
    private Button gLogout;

    private DatabaseReference gProfileRef;

    //progress dialog
    private ProgressDialog gDialog;

    public clientProfile() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_profile, container, false);

        gName = (TextView)view.findViewById(R.id.clientProfileFrag_name);
        gEmail = (TextView)view.findViewById(R.id.clientProfileFrag_email);
        gPhone = (TextView)view.findViewById(R.id.clientProfileFrag_phone);
        gImageView = (ImageView)view.findViewById(R.id.clientProfileFrag_propic);

        //progress dialog
        gDialog = new ProgressDialog(getContext());
        gDialog.setTitle("Please wait");
        gDialog.setMessage("While we are loading....");

        gLogout = (Button)view.findViewById(R.id.clientProfileFrag_logout_btn);

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
        gProfileRef = firebase.gDatabase.getReference().child(CONST.DB_CLIENT).child(firebase.CID);
        gProfileRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    gName.setText(String.valueOf(snapshot.child(CONST.DB_C_NAME).getValue()));
                    gEmail.setText(String.valueOf(snapshot.child(CONST.DB_C_EMAIL).getValue()));
                    gPhone.setText(String.valueOf("Ph : "+snapshot.child(CONST.DB_C_PHONE).getValue()));
                    Glide.with(getActivity()).load(String.valueOf(snapshot.child(CONST.DB_C_PROPICURL).getValue())).into(gImageView);
                    gDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}