package com.uk.ac.tees.w9207652.eventzz.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.uk.ac.tees.w9207652.eventzz.R;
import com.uk.ac.tees.w9207652.eventzz.*;
import com.uk.ac.tees.w9207652.eventzz.helpers.CONST;
import com.uk.ac.tees.w9207652.eventzz.helpers.firebase;
import com.uk.ac.tees.w9207652.eventzz.modelsNadapters.coHomeAdapter;
import com.uk.ac.tees.w9207652.eventzz.modelsNadapters.eventModel;

import java.util.ArrayList;
import java.util.List;


public class companyHome extends Fragment {

    private Button gNewEvent;


    //RCV
    private RecyclerView gRCV;
    private LinearLayoutManager gLLM;
    private coHomeAdapter adapter;
    private List<eventModel> eventModelList = new ArrayList<>();

    //firebase
    private DatabaseReference gEventsRef;

    //progress dialog
    private ProgressDialog gDialog;

    public companyHome() { }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company_home, container, false);

        //progress dialog
        gDialog = new ProgressDialog(getContext());
        gDialog.setTitle("Please wait");
        gDialog.setMessage("While we are loading....");


        gNewEvent = (Button)view.findViewById(R.id.companyHomeFragment_new_btn);
        gNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toNewEvent();
            }
        });

        gRCV = (RecyclerView)view.findViewById(R.id.companyHomeFrag_RCV);
        gLLM = new LinearLayoutManager(getActivity());
        gLLM.setOrientation(LinearLayoutManager.VERTICAL);
        gRCV.setLayoutManager(gLLM);
        gRCV.setHasFixedSize(true);



        adapter = new coHomeAdapter(eventModelList,getContext());
        gRCV.setAdapter(adapter);


        gEventsRef = firebase.gDatabase.getReference().child(CONST.DB_EVENTS);

        gEventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    eventModelList.clear();
                    for (DataSnapshot event : snapshot.getChildren())
                    {
                        if (String.valueOf(event.child(CONST.DB_E_COID).getValue()).equals(firebase.COID))
                        {
                            eventModelList.add(new eventModel(
                                    String.valueOf(event.getKey()),
                                    String.valueOf(event.child(CONST.DB_E_NAME).getValue()),
                                    String.valueOf(event.child(CONST.DB_E_SPONSORS).getValue()),
                                    String.valueOf(event.child(CONST.DB_E_DESC).getValue()),
                                    String.valueOf(event.child(CONST.DB_E_PRICE).getValue()),
                                    String.valueOf(event.child(CONST.DB_E_VENUE).getValue()),
                                    String.valueOf(event.child(CONST.DB_E_DATE).getValue()),
                                    String.valueOf(event.child(CONST.DB_E_PICURL).getValue()),
                                    String.valueOf(event.child(CONST.DB_E_COID).getValue())
                            ));
                        }
                    }
                    adapter.notifyDataSetChanged();
                    gDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






        return view;
    }

    private void toNewEvent() {
        Intent toNewEvent = new Intent(getActivity(),companyAddEvent.class);
        startActivity(toNewEvent);
    }

    @Override
    public void onStart() {
        super.onStart();
        gDialog.show();
    }
}