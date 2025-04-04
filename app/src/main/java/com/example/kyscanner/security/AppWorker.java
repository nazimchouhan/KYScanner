package com.example.kyscanner.security;

import android.app.Application;
import android.util.Log;
import androidx.work.OneTimeWorkRequest;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import java.util.concurrent.TimeUnit;

public class AppWorker extends Application{
    private static final String TAG = "AppWorker"; // Log tag


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "Application started: Scheduling WorkManager tasks");

        // Define constraints (e.g., require network connectivity)
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        FetchUserDetail.fetchAndSaveUserData();

        // Create a periodic work request for UpdateKyIdWorker (minimum interval: 15 minutes)
        WorkRequest workRequest = new OneTimeWorkRequest.Builder(UpdateKyIdworker.class)
                .setConstraints(constraints)
                .addTag("UpdateKyIdWorker")
                .build();
        WorkManager.getInstance(getApplicationContext()).enqueue(workRequest);
        Log.e(TAG, "WorkManager task enqueued: UpdateKyIdWorker");
    }
}
