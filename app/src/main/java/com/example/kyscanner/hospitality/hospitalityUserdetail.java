package com.example.kyscanner.hospitality;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.kyscanner.model.UserModel;
import com.example.kyscanner.model.hospitalityModel;

import org.json.JSONArray;
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

public class hospitalityUserdetail {
    private static final String BASE_URL ="https://apiv2.kashiyatra.org/api/user/getUser/";
    private static final OkHttpClient CLIENT = new OkHttpClient();
    private static final List<UserModel> copyExistingData = new ArrayList<>();
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static boolean isDataFetched = false;


    public static Future<List<hospitalityModel>> fetchHospitalityUserDetail(String KyId){
        Log.e("KYID", "KYID is :" + KyId);
        return executorService.submit(() -> {
            List<hospitalityModel> hospitalityuserDetail = new ArrayList<>();
            String encodedKyId = URLEncoder.encode(KyId, StandardCharsets.UTF_8.toString());
            Request request = new Request.Builder().url(BASE_URL + KyId).build();

            try (Response response = CLIENT.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseData = response.body().string();
                    Log.d("API Response", responseData); // Log the response
                    if (responseData.isEmpty()) {
                        Log.e("FetchUserDetail", "API returned an empty response");
                        return hospitalityuserDetail; // Return empty list
                    }
                    JSONObject jsonObject = new JSONObject(responseData);
                    String ky_id = jsonObject.optString("ky_id", "");

                    if (ky_id.equals(KyId)) {
                        String name = jsonObject.optString("name", "Null");
                        String college = jsonObject.optString("college", "Null");
                        String mobileNo = jsonObject.optString("mobile_number", "Null");
                        String gmail = jsonObject.optString("gmail", "Null");
                        JSONArray purchasedBenefits = jsonObject.getJSONArray("purchased_benefits");

                        // Check for specific benefits
                        boolean hasFood = false;
                        boolean hasAccomodation = false;
                        boolean hasTShirt = false;
                        for (int i = 0; i < purchasedBenefits.length(); i++) {
                            String benefit = purchasedBenefits.getString(i);
                            if (benefit.equalsIgnoreCase("Food")) {
                                hasFood = true;
                            }
                            if (benefit.equalsIgnoreCase("Accomodation")) {
                                hasAccomodation = true;
                            }
                            if (benefit.equalsIgnoreCase("T-Shirt")) {
                                hasTShirt = true;
                            }
                        }
                        hospitalityuserDetail.add(new hospitalityModel(name, KyId, college, mobileNo, gmail, hasFood, hasAccomodation, hasTShirt));
                    }
                } else {

                    Log.e("FetchUserDetail", "API request failed: " + response.code());
                }
            } catch (IOException | JSONException e) {
                Log.e("FetchUserDetail", "Error fetching user details", e);
            }
            return hospitalityuserDetail;
        });
    }
    public static void shutdown() {
        executorService.shutdown();
    }
}
