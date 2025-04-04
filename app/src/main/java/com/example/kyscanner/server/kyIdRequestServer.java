package com.example.kyscanner.server;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.CalendarContract;
import android.util.Log;

import com.example.kyscanner.security.GloballyDBClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
public class kyIdRequestServer {
    private static kyIdRequestServer instance;
    private final OkHttpClient client;
    private static SharedPreferences sharedPreferences;
    private final Context context;
    private final List<String> verifiedKYIDs; // Stores verified KYIDs

    public kyIdRequestServer(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null!"); // Prevent errors
        }
        this.context = context.getApplicationContext(); // Prevent memory leaks
        client = new OkHttpClient();
        verifiedKYIDs = new ArrayList<>();
        sharedPreferences = this.context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
    }

    public static synchronized kyIdRequestServer getInstance(Context context) {
        if (instance == null) {
            instance = new kyIdRequestServer(context);
        }
        return instance;
    }

    public void sendKYIDBatch(List<String> kyidList) {

        new Thread(() -> {
            Log.d("kyIdRequestServer", "Sending batch: " + kyidList.toString());
            String authToken = sharedPreferences.getString("AuthToken", "");

            try {
                JSONObject jsonObject = new JSONObject(authToken);
                String accessToken = jsonObject.getString("access");

                JSONObject requestBodyJson = new JSONObject();
                requestBodyJson.put("ids", new JSONArray(kyidList));

                RequestBody requestBody = RequestBody.create(
                        requestBodyJson.toString(), MediaType.get("application/json; charset=utf-8")
                );

                Request request = new Request.Builder()
                        .url("https://apiv2.kashiyatra.org/api/security/update_access")
                        .header("Authorization", "Bearer " + accessToken)
                        .post(requestBody)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful() && response.body() != null) {
                        String responseData = response.body().string();
                        GloballyDBClass.fetchAndSaveUserDataGlobally();
                        Log.e("Response", "Response data: " + responseData);
                    } else {
                        Log.e("Response", "Failed response: " + response.toString());
                    }
                } catch (IOException e) {
                    Log.e("Request", "Failed to get API Request:", e);
                }

            } catch (JSONException e) {
                Log.e("JSON Error", "Failed to create JSON: " + e.getMessage());
            }

        }).start();
    }
}