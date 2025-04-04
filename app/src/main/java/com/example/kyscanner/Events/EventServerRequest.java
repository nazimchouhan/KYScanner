package com.example.kyscanner.Events;

import android.util.Log;

import com.example.kyscanner.model.EventUserModel;
import com.example.kyscanner.model.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EventServerRequest {
    private static final String BASE_URL ="https://apiv2.kashiyatra.org/api/user/getUser/";
    private static final OkHttpClient CLIENT = new OkHttpClient();
    private static final List<UserModel> copyExistingData = new ArrayList<>();
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static boolean isDataFetched = false;
    public static Future<List<EventUserModel>> fetchEventUserDetail(String KyId){
        Log.e("KYID", "KYID is :" + KyId);
        return executorService.submit(() -> {
            List<EventUserModel> EventUserDetail = new ArrayList<>();
            String encodedKyId = URLEncoder.encode(KyId, StandardCharsets.UTF_8.toString());
            Request request = new Request.Builder().url(BASE_URL + KyId).build();

            try (Response response = CLIENT.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseData = response.body().string();
                    Log.d("API Response", responseData); // Log the response
                    if (responseData.isEmpty()) {
                        Log.e("FetchUserDetail", "API returned an empty response");
                        return EventUserDetail; // Return empty list
                    }
                    JSONObject jsonObject = new JSONObject(responseData);
                    String ky_id = jsonObject.optString("ky_id", "");

                    if (ky_id.equals(KyId)) {
                        String name = jsonObject.optString("name", "Null");
                        String college = jsonObject.optString("college", "Null");
                        String mobileNo = jsonObject.optString("mobile_number", "Null");

                        EventUserDetail.add(new EventUserModel(ky_id, name, mobileNo, college));
                    }
                } else {

                    Log.e("FetchUserDetail", "API request failed: " + response.code());
                }
            } catch (IOException | JSONException e) {
                Log.e("FetchUserDetail", "Error fetching user details", e);
            }
            return EventUserDetail;
        });
    }
    public static void shutdown() {
        executorService.shutdown();
    }
}
