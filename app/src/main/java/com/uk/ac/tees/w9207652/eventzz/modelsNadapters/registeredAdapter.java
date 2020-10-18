package com.uk.ac.tees.w9207652.eventzz.modelsNadapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.uk.ac.tees.w9207652.eventzz.R;
import com.uk.ac.tees.w9207652.eventzz.companyEventDisplay;
import com.uk.ac.tees.w9207652.eventzz.eventDisplay;
import com.uk.ac.tees.w9207652.eventzz.helpers.firebase;

import java.util.List;

public class registeredAdapter extends RecyclerView.Adapter<registeredAdapter.registered_VH> {
    private List<registeredModel> registeredModelList;
    private Context context;

    public registeredAdapter(List<registeredModel> registeredModelList, Context context) {
        this.registeredModelList = registeredModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public registered_VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_event_item,parent,false);
        return new registered_VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull registered_VH holder, int position) {
        final registeredModel curEvent = registeredModelList.get(position);
        holder.eName.getEditText().setText(curEvent.getEName());
        holder.eSponsors.getEditText().setText(curEvent.getESponsors());
        holder.eVenue.getEditText().setText(curEvent.getEVenue());
        String date = curEvent.getEDate();
        String[] dates = date.split("/");
        holder.eDate.setText(dates[0]);
        holder.eMonth.setText(getMonth(dates[1]));
        final String EID = curEvent.getEID();

        holder.clickLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firebase.CUR_USER == 0)
                {
                    Intent toDisplay = new Intent(context, eventDisplay.class);
                    toDisplay.putExtra("EID",EID);
                    context.startActivity(toDisplay);
                }else
                {
                    Intent toDisplay = new Intent(context, companyEventDisplay.class);
                    toDisplay.putExtra("EID",EID);
                    context.startActivity(toDisplay);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return registeredModelList.size();
    }

    public class registered_VH extends RecyclerView.ViewHolder{
        private TextInputLayout eName,eSponsors,eVenue;
        private TextView eDate,eMonth;
        private RelativeLayout clickLayout;
        public registered_VH(@NonNull View itemView) {
            super(itemView);
            eName = (TextInputLayout)itemView.findViewById(R.id.singleEventItem_eventName);
            eSponsors = (TextInputLayout)itemView.findViewById(R.id.singleEventItem_eventSponsor);
            eVenue = (TextInputLayout)itemView.findViewById(R.id.singleEventItem_eventAddress);
            eDate = (TextView)itemView.findViewById(R.id.singleEventItem_eventDate);
            eMonth = (TextView)itemView.findViewById(R.id.singleEventItem_eventDay);
            clickLayout = (RelativeLayout)itemView.findViewById(R.id.click_Layout);
        }
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
