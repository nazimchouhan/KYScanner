package com.example.kyscanner.security;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.example.kyscanner.model.UserModel;

import java.util.List;
public class UpdateKyIdworker extends Worker {
    Context context;

    public UpdateKyIdworker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        // Call the updateKyIdSet method
        Log.e("UpdateKyIdworker", "Worker has started execution");
        Log.e("Worker started", "Updating KY IDs and fetching user data");

        try {

            while (!FetchUserDetail.isDataReady()) { // Retry max 5 times
                //Log.e("UpdateKYidWorker", "UserModel is Empty, waiting for data... Retry: ");
                return Result.retry();
            }
            List<UserModel> userModels = FetchUserDetail.getuserList();
            if (userModels.isEmpty()) {
                //Log.e("UpdateKYidWorker", "UserModel is Empty, waiting for data...");
                return Result.retry(); // Worker will retry later
            }
            //Log.e("UserData","UserData Fetched and Saved Successfully");

            updateDatabaseFromList(userModels,context);
            //Log.e("userData","Userdata is Updated in Database");
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyListPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("UserList", userModels.size());
            editor.apply();
            //Log.e("UpdateKyIdWorker", "KY ID Set size saved in SharedPreferences: " + userModels.size());

            // Send a broadcast to notify MainActivity about data change
            Intent intent = new Intent("User_List_UPDATED");
            intent.putExtra("UserList", userModels.size());
            intent.setPackage(getApplicationContext().getPackageName()); // Target your app only

            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            return Result.success();

        } catch (Exception e) {
            Log.e("UpdateKyIdWorker", "Worker failed due to: " + e.getMessage(), e);
            return Result.failure();
        }
    }

    public static void updateDatabaseFromList(List<UserModel> userList, Context context) {
        UserDatabaseHelper db = UserDatabaseHelper.getInstance(context);
        for (UserModel user : userList) {
            try {
                String Ky_id = user.getKyId();

                if (db.doesUserExist(Ky_id)) {
                    UserModel existingUser = db.getUserByKyId(Ky_id);
                    if (existingUser != null &&
                            (existingUser.isEvent1() != user.isEvent1() ||
                                    existingUser.isEvent2() != user.isEvent2() ||
                                    existingUser.isEvent3() != user.isEvent3())) {
                        Log.e("Updated KYID", "the Updated KYID is :" + existingUser.getKyId());
                        db.insertOrUpdateUser(Ky_id, user.getGender(), user.getName(), user.getGmail(),
                                user.isEvent1(), user.isEvent2(), user.isEvent3());
                        Log.e("Sync", "Updated user: " + Ky_id);
                    }
                } else {
                    db.insertOrUpdateUser(Ky_id, user.getGender(), user.getName(), user.getGmail(),
                            user.isEvent1(), user.isEvent2(), user.isEvent3());
                    Log.e("Sync", "New Added user: " + Ky_id);
                }
            } catch (Exception e) {
                Log.e("Sync", "Error processing user: " + (user != null ? user.getKyId() : "null"), e);
            }

        }
    }
}