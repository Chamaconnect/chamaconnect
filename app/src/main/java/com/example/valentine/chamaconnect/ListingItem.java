package com.example.valentine.chamaconnect;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.valentine.chamaconnect.helper.MySingleton;
import com.example.valentine.chamaconnect.model.Property;

public class ListingItem extends AppCompatActivity {
    public static final String TAG_SEL_PROPERTY = "selectedProperty";

    private TextView location;
    private TextView description;
    private AppBarLayout appbar;
    private Property property;
    private NetworkImageView header;
    ImageLoader imageLoader = MySingleton.getInstance(this).getImageLoader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        location = (TextView) findViewById(R.id.location);
        description = (TextView) findViewById(R.id.description);
        header = (NetworkImageView) findViewById(R.id.header_photo);

        Intent i = getIntent();
        property = (Property) i.getSerializableExtra(TAG_SEL_PROPERTY);

        location.setText(property.getLocation());
        description.setText(property.getDescription());

        if (imageLoader == null)
            imageLoader = MySingleton.getInstance(this).getImageLoader();

        header.setImageUrl(property.getPhotoId(),imageLoader);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cogwheel, menu);

        return true;
    }
}
