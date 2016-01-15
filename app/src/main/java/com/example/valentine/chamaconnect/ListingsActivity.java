package com.example.valentine.chamaconnect;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.valentine.chamaconnect.helper.CustomItemClickListener;
import com.example.valentine.chamaconnect.helper.MySingleton;
import com.example.valentine.chamaconnect.helper.PropertyAdapter;
import com.example.valentine.chamaconnect.model.Property;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListingsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Property> properties;
    private static final String TAG = "DROGO";
    PropertyAdapter adapter;
    private String URL_FEED = "http://colleowino.github.io/hauz/img/api/feed.json";

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Chama");
        toolbar.setLogo(R.drawable.logo);
        setSupportActionBar(toolbar);

        properties = new ArrayList<Property>(); // I hate the null values

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.counties_array, R.layout.spinner_layout);
// Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);

        spinner.setAdapter(spinnerAdapter);

        recyclerView = (RecyclerView) findViewById(R.id.properties);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);

        adapter = new PropertyAdapter(this, properties);

        recyclerView.setAdapter(adapter);

        /**
         * Making volley's json object request to fetch list of photos of an
         * album
         * */
        JsonObjectRequest jsonReq = new JsonObjectRequest(Method.GET,
                URL_FEED, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "----------- creating the jsonrequest ------- ");

                VolleyLog.e(TAG, "Response: " + response.toString());
                if (response != null) {
                    parseJsonFeed(response);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "----------- creating the jsonrequest ------- ");
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                // unable to fetch wallpapers
                // either google username is wrong or
                // devices doesn't have internet connection
                Toast.makeText(getApplicationContext(), "Feeds not loading",
                        Toast.LENGTH_LONG).show();
            }
        });

        Log.e(TAG, "----------- request ready ------- " + jsonReq.toString());

        MySingleton.getInstance(this).addToRequestQueue(jsonReq);

        Log.e(TAG, "----------- got instance and run it ------- ");


        // Define click listener for the ViewHolder's View.
        adapter.setListener(new CustomItemClickListener() {

            @Override
            public void onItemClick(View v, int position) {
                int clickedItem = position;
                Log.e("DROGO", "Element " + clickedItem + " clicked.");

                Intent propertyIntent = new Intent(v.getContext(), PropertyActivity.class);

                // passing the selected property
                Property property = properties.get(clickedItem);
                propertyIntent.putExtra(PropertyActivity.TAG_SEL_PROPERTY, property);

                v.getContext().startActivity(propertyIntent);

            }

        });

    }

    /**
     * Parsing json reponse and passing the data to feed view list adapter
     * */
    private void parseJsonFeed(JSONObject response) {
        Log.e(TAG, "----------- parsing json ------- ");
        try {
            JSONArray feedArray = response.getJSONArray("nairobi");

            Log.e(TAG, "----------- feedArray obtained ------- ");

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                Property prop = new Property();
                prop.setDescription(feedObj.getString("description"));
                prop.setLocation(feedObj.getString("location"));


                // Image might be null sometimes
                String image = feedObj.isNull("image") ? null : feedObj
                        .getString("image");

                prop.setPhotoId(image);

                properties.add(prop);

            }

            // notify data changes to list adapater
            adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

}
