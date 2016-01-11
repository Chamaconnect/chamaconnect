package com.example.valentine.chamaconnect;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.valentine.chamaconnect.model.Property;

public class PropertyActivity extends AppCompatActivity {
    public static final String TAG_SEL_PROPERTY = "selectedProperty";

    private TextView location;
    private TextView description;
    private AppBarLayout appbar;
    private Property property;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        location = (TextView) findViewById(R.id.location);
        description = (TextView) findViewById(R.id.description);
        appbar = (AppBarLayout) findViewById(R.id.app_bar);

        Intent i = getIntent();
        property = (Property) i.getSerializableExtra(TAG_SEL_PROPERTY);

        location.setText(property.getLocation());
        description.setText(property.getDescription());

    }
}
