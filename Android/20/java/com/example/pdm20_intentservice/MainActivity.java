package com.example.pdm20_intentservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

//FILTERS: https://developer.android.com/guide/components/intents-filters
public class MainActivity extends Activity {
    TextView tvResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResponse = (TextView)findViewById(R.id.tvResponse);
        
        // The filter's action is BROADCAST_ACTION
        IntentFilter mStatusIntentFilter = new IntentFilter(RSSPullService.BROADCAST_ACTION);
    
        // Adds a data filter for the HTTP scheme
        //mStatusIntentFilter.addDataScheme("http");

        // Instantiates a new DownloadStateReceiver
        ResponseReceiver mResponseReceiver = new ResponseReceiver();

        // Registers the DownloadStateReceiver and its intent filters
        LocalBroadcastManager.getInstance(this).registerReceiver(
                                mResponseReceiver,
                                mStatusIntentFilter);
        
    }

    public void onClick(View v){
        Intent mServiceIntent = new Intent(this, RSSPullService.class);

        mServiceIntent.setData(Uri.parse("http"));

        startService(mServiceIntent);
    }

    // Broadcast receiver for receiving status updates from the IntentService
    private class ResponseReceiver extends BroadcastReceiver
    {
        // Prevents instantiation
        ResponseReceiver() {
        }

        // Called when the BroadcastReceiver gets an Intent it's registered to receive
        @Override
        public void onReceive(Context context, Intent intent) {
            //...
            /*
             * Handle Intents here.
             */
            //...

            String data = intent.getExtras().getString("data");
            tvResponse.setText("Dato = [" + data + "]");
        }
    }

}