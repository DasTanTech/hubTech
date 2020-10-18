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
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.uk.ac.tees.w9207652.eventzz.R;
import com.uk.ac.tees.w9207652.eventzz.eventsList;
import com.uk.ac.tees.w9207652.eventzz.helpers.CONST;
import com.uk.ac.tees.w9207652.eventzz.helpers.firebase;
import com.uk.ac.tees.w9207652.eventzz.modelsNadapters.coHomeAdapter;
import com.uk.ac.tees.w9207652.eventzz.modelsNadapters.eventModel;
import com.uk.ac.tees.w9207652.eventzz.modelsNadapters.sliderAdapter;

import java.util.ArrayList;
import java.util.List;


public class clientHome extends Fragment {

    private TextView gShowAll;

    private SliderView gSliderView;
    private sliderAdapter sliderAdapter;
    private List<eventModel> eventModelListRaw = new ArrayList<>();
    private List<eventModel> eventModelList = new ArrayList<>();

    //RCV
    private RecyclerView gRCV;
    private LinearLayoutManager gLLM;
    private coHomeAdapter adapter;

    private DatabaseReference gEventRef;

    //progress dialog
    private ProgressDialog gDialog;

    public clientHome() { }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_home, container, false);

        //progress dialog
        gDialog = new ProgressDialog(getContext());
        gDialog.setTitle("Please wait");
        gDialog.setMessage("While we are loading....");



        gShowAll = (TextView)view.findViewById(R.id.clientHomeFrag_showAll_btn);

        gShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toEventsList();
            }
        });

        gSliderView = (SliderView)view.findViewById(R.id.clientHomeFrag_imageSlider);
        gSliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        gSliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        gSliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        gSliderView.setScrollTimeInSec(4);
        gSliderView.startAutoCycle();

        sliderAdapter = new sliderAdapter(getContext(),eventModelList);
        gSliderView.setSliderAdapter(sliderAdapter);

        gRCV = (RecyclerView)view.findViewById(R.id.clientHomeFrag_RCV);
        gLLM = new LinearLayoutManager(getActivity());
        gLLM.setOrientation(LinearLayoutManager.VERTICAL);
        gRCV.setLayoutManager(gLLM);
        gRCV.setHasFixedSize(true);



        adapter = new coHomeAdapter(eventModelList,getContext());
        gRCV.setAdapter(adapter);


        gDialog.show();
        gEventRef = firebase.gDatabase.getReference().child(CONST.DB_EVENTS);
        gEventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren())
                {
                    eventModelListRaw.clear();
                    eventModelList.clear();
                    for (DataSnapshot event : snapshot.getChildren())
                    {
                        eventModelListRaw.add(new eventModel(
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
                    adapter.notifyDataSetChanged();

                    int len = eventModelListRaw.size();
                    for (int i = len-1;i>=0;i--)
                    {
                        eventModelList.add(eventModelListRaw.get(i));
                    }
                    sliderAdapter.notifyDataSetChanged();
                    gDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private void toEventsList() {
        Intent toEventsList = new Intent(getActivity(), eventsList.class);
        startActivity(toEventsList);
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}