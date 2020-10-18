package com.uk.ac.tees.w9207652.eventzz.modelsNadapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.uk.ac.tees.w9207652.eventzz.R;
import com.uk.ac.tees.w9207652.eventzz.eventDisplay;

import java.util.List;

public class sliderAdapter extends SliderViewAdapter<sliderAdapter.slider_VH> {

    private Context context;
    private List<eventModel> eventModelList;

    public sliderAdapter(Context context, List<eventModel> eventModelList) {
        this.context = context;
        this.eventModelList = eventModelList;
    }

    @Override
    public slider_VH onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_slide_layout,parent,false);
        return new slider_VH(view);
    }

    @Override
    public void onBindViewHolder(slider_VH viewHolder, int position) {
        final eventModel curEvent = eventModelList.get(position);

        viewHolder.D_NAME.setText(curEvent.getEName());
        viewHolder.D_PRICE.setText(curEvent.getEPrice());
        Glide.with(context).load(curEvent.getEPicUrl()).fitCenter().into(viewHolder.D_PIC);

        viewHolder.D_CLICK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toEventDisplay = new Intent(context, eventDisplay.class);
                toEventDisplay.putExtra("EID",curEvent.getEID());
                context.startActivity(toEventDisplay);
            }
        });

    }

    @Override
    public int getCount() {
        if (eventModelList.size() > 5)
            return 5;
        else
            return eventModelList.size();
    }

    public class slider_VH extends SliderViewAdapter.ViewHolder {

        private TextView D_NAME,D_PRICE;
        private ImageView D_PIC;
        private RelativeLayout D_CLICK;

        public slider_VH(View itemView) {
            super(itemView);

            D_NAME = (TextView)itemView.findViewById(R.id.singleSlideLayout_name_txt);
            D_PRICE = (TextView)itemView.findViewById(R.id.singleSlideLayout_price_txt);
            D_PIC = (ImageView)itemView.findViewById(R.id.singleSlideLayout_image);
            D_CLICK = (RelativeLayout)itemView.findViewById(R.id.slide_click_layout);
        }
    }
}
