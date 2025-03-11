package dev.pedroayon.pdm20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.app.Activity;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
    TextView tvResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResponse = findViewById(R.id.tvResponse);

        // The filter's action is the same as in the Worker
        IntentFilter mStatusIntentFilter = new IntentFilter(RSSPullWorker.BROADCAST_ACTION);

        // Register the receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(
                new ResponseReceiver(),
                mStatusIntentFilter);
    }

    public void onClick(View v){
        // Prepare input data (if needed)
        Data inputData = new Data.Builder()
                .putString("input_data", "http")
                .build();

        // Create a OneTimeWorkRequest for your Worker
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(RSSPullWorker.class)
                .setInputData(inputData)
                .build();

        // Enqueue the work request
        WorkManager.getInstance(this).enqueue(workRequest);
    }

    // Broadcast receiver for receiving status updates from the Worker
    private class ResponseReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Retrieve and display data from the broadcast
            String data = intent.getStringExtra("data");
            tvResponse.setText("Dato = " + data);
        }
    }
}
