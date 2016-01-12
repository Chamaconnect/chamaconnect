package com.example.valentine.chamaconnect.helper;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.valentine.chamaconnect.R;
import com.example.valentine.chamaconnect.model.Property;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;

/**
 * Created by cliff on 24/12/15.
 */
public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder>{

    private List<Property> properties;
    CustomItemClickListener listener;
    static ImageLoader imageLoader;

    public PropertyAdapter(Activity activity, List<Property> properties) {
        this.properties = properties;
        this.imageLoader = MySingleton.getInstance(activity.getApplicationContext()).getImageLoader();

    }

    public static class PropertyViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView propertyLocation;
        TextView propertyDescription;
        NetworkImageView propertyPhoto;

        PropertyViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.card_view);
            propertyLocation = (TextView)itemView.findViewById(R.id.title);
            propertyDescription = (TextView)itemView.findViewById(R.id.desc);

            if (imageLoader == null)
                imageLoader = MySingleton.getInstance(itemView.getContext()).getImageLoader();

            propertyPhoto = (NetworkImageView) itemView.findViewById(R.id.thumbnail);
        }
    }

    @Override
    public PropertyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.property_card, viewGroup, false);
        final PropertyViewHolder pvh = new PropertyViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, pvh.getAdapterPosition());
            }
        });
        return pvh;
    }

    @Override
    public void onBindViewHolder(PropertyViewHolder propertyViewHolder, int i) {
        propertyViewHolder.propertyLocation.setText(properties.get(i).getLocation());
        propertyViewHolder.propertyDescription.setText(properties.get(i).getDescription());
        propertyViewHolder.propertyPhoto.setImageUrl(properties.get(i).getPhotoId(), imageLoader);
    }

    public int getItemCount() {
        return properties.size();
    }

    public void setListener(CustomItemClickListener listener){
        this.listener = listener;
    }

}