package dev.pedroayon.pdm20;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import android.util.Log;

public class RSSPullWorker extends Worker {

    public RSSPullWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Get input data (for example, a URL string)
        String dataString = getInputData().getString("input_data");
        if (dataString == null) {
            dataString = "";
        }

        // Process the data (similar to your IntentService logic)
        dataString = "[" + dataString + "]";
        Log.d("RSS", "Worker executing: " + dataString);

        // Create and send a local broadcast with the result
        Intent localIntent = new Intent(RSSPullWorker.BROADCAST_ACTION);
        String status = "OK";
        localIntent.putExtra(RSSPullWorker.TAG_STATUS, status);
        localIntent.putExtra("data", dataString);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(localIntent);

        return Result.success();
    }

    // Constants for broadcast
    public static final String BROADCAST_ACTION = "com.example.pdm20_intentservice.BROADCAST";
    public static final String TAG_STATUS = "com.example.pdm20_intentservice.STATUS";
}
