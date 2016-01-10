package com.example.valentine.chamaconnect;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.valentine.chamaconnect.model.Property;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cliff on 24/12/15.
 */
public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder>{

    private List<Property> properties;

    public PropertyAdapter(List<Property> properties) {
        this.properties = properties;
        if (properties != null) {
            Log.e("DROGO", " We have lift off");
        } else {
            Log.e("DROGO", "what the hell is going on");
        }
    }

    public static class PropertyViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView propertyLocation;
        TextView propertyDescription;
        ImageView propertyPhoto;

        PropertyViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.card_view);
            propertyLocation = (TextView)itemView.findViewById(R.id.title);
            propertyDescription = (TextView)itemView.findViewById(R.id.desc);
            propertyPhoto = (ImageView)itemView.findViewById(R.id.thumbnail);

            // Define click listener for the ViewHolder's View.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedItem = getAdapterPosition();
                    Log.d("DROGO", "Element " + clickedItem + " clicked.");

                    Intent propertyIntent = new Intent(v.getContext(), PropertyActivity.class);

                    propertyIntent.putExtra("PROPERTY_ID",clickedItem );

                    v.getContext().startActivity(propertyIntent);
                }
            });
        }
    }

    @Override
    public PropertyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.property_card, viewGroup, false);
        PropertyViewHolder pvh = new PropertyViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PropertyViewHolder propertyViewHolder, int i) {
        propertyViewHolder.propertyLocation.setText(properties.get(i).getLocation());
        propertyViewHolder.propertyDescription.setText(properties.get(i).getDescription());
        propertyViewHolder.propertyPhoto.setImageResource(properties.get(i).getPhotoId());
    }

    public int getItemCount() {
        return properties.size();
    }

}