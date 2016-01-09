package com.example.valentine.chamaconnect;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by valentine on 1/6/16.
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();




//        // Add your initialization code here
//        Parse.initialize(this, "usSvkOLma7JHM0Nc8cAAwH5nCI0mULIBpA8UUQhm", "mLSWLCCzBR9NdRd361CumMDaL6rhkoWBwOJrfdkE");
//
//        ParseUser.enableAutomaticUser();
//        ParseACL defaultACL = new ParseACL();
//
//        // If you would like all objects to be private by default, remove this
//        // line.
//        defaultACL.setPublicReadAccess(true);
//
//        ParseACL.setDefaultACL(defaultACL, true);

// [Optional] Power your app with Local Datastore. For more info, go to
// https://parse.com/docs/android/guide#local-datastore
        Parse.enableLocalDatastore(this);

        Parse.initialize(this);
    }

}
