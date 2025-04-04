package com.example.kyscanner.security;

import static com.example.kyscanner.security.UserDatabaseHelper.COLUMN_EVENT1;
import static com.example.kyscanner.security.UserDatabaseHelper.COLUMN_EVENT2;
import static com.example.kyscanner.security.UserDatabaseHelper.COLUMN_EVENT3;

import android.content.Context;
import android.util.Log;

import com.example.kyscanner.model.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GloballyDBClass {
    private static final String BASE_URL = "https://apiv2.kashiyatra.org/api/security/retrieve";
    private static final OkHttpClient CLIENT = new OkHttpClient();
    private static final List<UserModel> copyExistingData=new ArrayList<>();
    private static Context context;
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static boolean isDataFetched=false;
    public static void fetchAndSaveUserDataGlobally() {
        executorService.execute(() -> {
            Request request = new Request.Builder().url(BASE_URL).build();
            try (Response response = CLIENT.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseData = response.body().string();
                    Calendar calendar = Calendar.getInstance();
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    int month = calendar.get(Calendar.MONTH) + 1; // January is 0
                    int year = calendar.get(Calendar.YEAR);

                    String eventColumn = null;
                    boolean isEligible = false;


                    if (year == 2025 && month == 3) { // March 2025
                        switch (day) {
                            case 7:
                                eventColumn = COLUMN_EVENT1;
                                break;
                            case 8:
                                eventColumn = COLUMN_EVENT2;
                                break;
                            case 9:
                                eventColumn = COLUMN_EVENT3;
                                break;
                        }
                    }
                    try {
                        // Convert responseData into JSONArray
                        JSONArray jsonArray = new JSONArray(responseData);
                        UserDatabaseHelper dbhelper=UserDatabaseHelper.getInstance(context);

                        // Loop through the array and fetch values
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String kyId = jsonArray.getString(i);  // Extract each string
                            Log.e("KYIds are :","The KYIDS are :" + kyId);
                            dbhelper.updateEventEligibility(kyId,eventColumn,false);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                } else {
                    Log.e("FetchUserDetail", "API request failed: " + response.code());
                }
            } catch (IOException e) {
                Log.e("FetchUserDetail", "Error fetching user details", e);
            }
        });
    }

    public static List<UserModel> getuserList() {
        synchronized (copyExistingData) {
            return new ArrayList<>(copyExistingData); // Return a copy to avoid concurrency issues
        }
    }
}
