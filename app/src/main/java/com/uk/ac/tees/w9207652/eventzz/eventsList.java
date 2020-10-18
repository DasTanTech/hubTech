package com.uk.ac.tees.w9207652.eventzz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.uk.ac.tees.w9207652.eventzz.helpers.CONST;
import com.uk.ac.tees.w9207652.eventzz.helpers.firebase;
import com.uk.ac.tees.w9207652.eventzz.modelsNadapters.coHomeAdapter;
import com.uk.ac.tees.w9207652.eventzz.modelsNadapters.eventModel;

import java.util.ArrayList;
import java.util.List;

public class eventsList extends AppCompatActivity {

    private List<eventModel> eventModelListRaw = new ArrayList<>();
    private List<eventModel> eventModelList = new ArrayList<>();

    //RCV
    private RecyclerView gRCV;
    private LinearLayoutManager gLLM;
    private coHomeAdapter adapter;

    private DatabaseReference gEventRef;

    //progress dialog
    private ProgressDialog gDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);

        gRCV = (RecyclerView)findViewById(R.id.eventsList_RCV);
        gLLM = new LinearLayoutManager(this);
        gLLM.setOrientation(LinearLayoutManager.VERTICAL);
        gRCV.setLayoutManager(gLLM);
        gRCV.setHasFixedSize(true);

        //progress dialog
        gDialog = new ProgressDialog(this);
        gDialog.setTitle("Please wait");
        gDialog.setMessage("While we are loading....");

        adapter = new coHomeAdapter(eventModelList,this);
        gRCV.setAdapter(adapter);

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


                    int len = eventModelListRaw.size();
                    for (int i = len-1;i>=0;i--)
                    {
                        eventModelList.add(eventModelListRaw.get(i));
                    }
                    adapter.notifyDataSetChanged();
                    gDialog.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        gDialog.show();
    }
}