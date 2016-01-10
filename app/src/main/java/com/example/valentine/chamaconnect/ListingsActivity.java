package com.example.valentine.chamaconnect;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.valentine.chamaconnect.model.Property;

import java.util.ArrayList;

public class ListingsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Property> properties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Chama");
        toolbar.setLogo(R.drawable.ic_action_appbar_icon);
        setSupportActionBar(toolbar);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.counties_array, R.layout.spinner_layout);
// Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);

        spinner.setAdapter(spinnerAdapter);

        recyclerView = (RecyclerView) findViewById(R.id.properties);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);

        initializeData();
        PropertyAdapter adapter = new PropertyAdapter(properties);
        
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    private void initializeData(){
        ArrayList<Property> properties = new ArrayList<Property>();
        properties.add(new Property("Karen", "KAREN BROOKS 23 ACRES DEVELOPMENT PLOT", R.drawable.prop_1));
        properties.add(new Property("Westlands", "WARAI RD 4 BED VILLA", R.drawable.prop_2));
        properties.add(new Property("Ridgeways", "ALMASI 4 BED PLUS VILLA", R.drawable.prop_2));
        this.properties = properties;
    }
}
