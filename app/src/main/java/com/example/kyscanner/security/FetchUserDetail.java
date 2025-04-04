package com.example.kyscanner.security;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import com.example.kyscanner.model.UserModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FetchUserDetail {
    private static final String BASE_URL = "https://apiv2.kashiyatra.org/api/user/getUsersApp";
    private static final OkHttpClient CLIENT = new OkHttpClient();
    private static final List<UserModel> copyExistingData=new ArrayList<>();
    private static Context context;
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static boolean isDataFetched=false;
    public static void fetchAndSaveUserData() {
        executorService.execute(() -> {
            Request request = new Request.Builder().url(BASE_URL).build();
            try (Response response = CLIENT.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseData = response.body().string();
                    JSONArray jsonArray = new JSONArray(responseData);
                    List<UserModel> usersData = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name = jsonObject.optString("name", "Unknown");
                        String ky_id = jsonObject.optString("ky_id", "");
                        String gender=jsonObject.optString("gender");
                        String Gmail = jsonObject.optString("gmail", "");
                        JSONArray purchasedBenefits = jsonObject.getJSONArray("purchased_benefits");
                        boolean event1 = false;
                        boolean event2 = false;
                        boolean event3 = false;
                        for (int j = 0; j < purchasedBenefits.length(); j++) {
                            String benefit = purchasedBenefits.getString(j);
                            if (benefit.equalsIgnoreCase("Event_1")) {
                                event1=true;
                            }
                            if (benefit.equalsIgnoreCase("Event_2")) {
                                event2=true;
                            }
                            if (benefit.equalsIgnoreCase("Event_3")) {
                                event3=true;
                            }
                        }

                        if (!ky_id.isEmpty()) { // Ensure valid ky_id before adding
                            usersData.add(new UserModel(ky_id,gender,name, Gmail,event1,event2,event3));
                        }
                    }
                    // Synchronize and update data
                    synchronized (copyExistingData) {
                        copyExistingData.clear();  // Avoid duplicates by clearing old data
                        copyExistingData.addAll(usersData);
                        isDataFetched = true;
                    }
                    Log.e("FetchUserDetail", "Data fetched successfully. Total users: " + usersData.size());

                } else {
                    Log.e("FetchUserDetail", "API request failed: " + response.code());
                }
            } catch (IOException | JSONException e) {
                Log.e("FetchUserDetail", "Error fetching user details", e);
            }
        });
    }


    public static List<UserModel> getuserList() {
        synchronized (copyExistingData) {
            return new ArrayList<>(copyExistingData); // Return a copy to avoid concurrency issues
        }
    }
    public static void UpdateDB(Context context,List<UserModel> copyexistingData){
        Log.e("ExistingData","CopyExistingData" + copyexistingData.size());
        new Thread(() -> {
            UserDatabaseHelper db = UserDatabaseHelper.getInstance(context);
            for (UserModel user : copyexistingData) {
                try {
                    String Ky_id = user.getKyId();

                    if (db.doesUserExist(Ky_id)) {
                        UserModel existingUser = db.getUserByKyId(Ky_id);
                        if (existingUser != null &&
                                (existingUser.isEvent1() != user.isEvent1() ||
                                        existingUser.isEvent2() != user.isEvent2() ||
                                        existingUser.isEvent3() != user.isEvent3())) {
                            Log.e("Updated KYID", "the Updated KYID is :" + existingUser.getKyId());
                            db.insertOrUpdateUser(Ky_id,user.getGender(), user.getName(), user.getGmail(),
                                    user.isEvent1(), user.isEvent2(), user.isEvent3());
                            Log.e("Sync", "Updated user: " + Ky_id);
                        }
                    } else {
                        db.insertOrUpdateUser(Ky_id,user.getGender(), user.getName(), user.getGmail(),
                                user.isEvent1(), user.isEvent2(), user.isEvent3());
                        Log.e("Sync", "New Added user: " + Ky_id);
                    }
                } catch (Exception e) {
                    Log.e("Sync", "Error processing user: " + (user != null ? user.getKyId() : "null"), e);
                }
            }
        }).start();
    }

    // Bulk Update Method for Server Sync
    public static boolean isDataReady() {
        return isDataFetched;
    }

}


