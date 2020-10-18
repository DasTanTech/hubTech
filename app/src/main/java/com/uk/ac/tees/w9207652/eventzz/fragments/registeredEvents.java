package com.uk.ac.tees.w9207652.eventzz.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.uk.ac.tees.w9207652.eventzz.R;
import com.uk.ac.tees.w9207652.eventzz.helpers.CONST;
import com.uk.ac.tees.w9207652.eventzz.helpers.firebase;
import com.uk.ac.tees.w9207652.eventzz.modelsNadapters.coHomeAdapter;
import com.uk.ac.tees.w9207652.eventzz.modelsNadapters.eventModel;
import com.uk.ac.tees.w9207652.eventzz.modelsNadapters.registeredAdapter;
import com.uk.ac.tees.w9207652.eventzz.modelsNadapters.registeredModel;

import java.util.ArrayList;
import java.util.List;


public class registeredEvents extends Fragment {


    private List<registeredModel> eventModelList = new ArrayList<>();
    private TextView gTitle;

    //RCV
    private RecyclerView gRCV;
    private LinearLayoutManager gLLM;
    private registeredAdapter adapter;

    private DatabaseReference gRegisteredRef;

    //progress dialog
    private ProgressDialog gDialog;


    public registeredEvents() { }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registered_events, container, false);

        gTitle = (TextView)view.findViewById(R.id.registeredEvents_title);

        gRCV = (RecyclerView)view.findViewById(R.id.registeredEvents_RCV);
        gLLM = new LinearLayoutManager(getActivity());
        gLLM.setOrientation(LinearLayoutManager.VERTICAL);
        gRCV.setLayoutManager(gLLM);
        gRCV.setHasFixedSize(true);

        //progress dialog
        gDialog = new ProgressDialog(getContext());
        gDialog.setTitle("Please wait");
        gDialog.setMessage("While we are loading....");
        gDialog.show();

        if (firebase.CUR_USER == 0)
        {
            gTitle.setText("All Events");
            adapter = new registeredAdapter(eventModelList,getContext());
            gRCV.setAdapter(adapter);
            gRegisteredRef = firebase.gDatabase.getReference().child(CONST.DB_CLIENT).child(firebase.CID).child(CONST.DB_REGISTERS);
            gRegisteredRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChildren())
                    {
                        eventModelList.clear();
                        for (DataSnapshot event : snapshot.getChildren())
                        {
                            eventModelList.add(new registeredModel(
                                    String.valueOf(event.child(CONST.DB_R_EID).getValue()),
                                    String.valueOf(event.child(CONST.DB_R_ENAME).getValue()),
                                    String.valueOf(event.child(CONST.DB_R_ESPONSOR).getValue()),
                                    String.valueOf(event.child(CONST.DB_R_EVENUE).getValue()),
                                    String.valueOf(event.child(CONST.DB_R_EDATE).getValue())
                            ));
                        }
                        adapter.notifyDataSetChanged();

                    }
                    gDialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else if(firebase.CUR_USER == 1)
        {
            gDialog.dismiss();
            gTitle.setText("Registered Users");
        }



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}