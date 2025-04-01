package com.example.pdm20_intentservice;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class RSSPullService extends IntentService {
    public RSSPullService() {
        super("RSSPullService");
    }

    // Defines a custom Intent action
    public static final String BROADCAST_ACTION = "com.example.pdm20_intentservice.BROADCAST";

    // Defines the key for the status "extra" in an Intent
    public static final String TAG_STATUS = "com.example.pdm20_intentservice.STATUS";

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        String dataString = workIntent.getDataString();


        // Do work here, based on the contents of dataString
        //>>
        dataString = "[" + dataString + "]";
        //<<

        /*
         * Creates a new Intent containing a Uri object
         * BROADCAST_ACTION is a custom Intent action
         */
        Intent localIntent = new Intent(BROADCAST_ACTION);

        // Puts the status into the Intent
        String status = "OK";
        localIntent.putExtra(TAG_STATUS, status);
        localIntent.putExtra("data", dataString);

        Log.d("RSS", "Entro");

        // Broadcasts the Intent to receivers in this app.
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }
}


