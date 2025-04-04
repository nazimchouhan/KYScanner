package com.example.kyscanner.Events;


import android.content.Context;
import android.util.Log;

import com.example.kyscanner.model.EventUserModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendRequesttoServer {
    public interface ServerResponseCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }
    public static void sendRequestToServer(Context context,EventUserModel user,String SubEvent,String TeamName,String URL,ServerResponseCallback callback){
        OkHttpClient client=new OkHttpClient();
        try{
            JSONObject object=new JSONObject();
            object.put("ky_id",user.getKYID());
            object.put("name",user.getName());
            object.put("phone",user.getPhoneNo());
            object.put("college",user.getCollegeName());
            object.put("subevent",SubEvent);
            object.put("team",TeamName);

            RequestBody requestBody=RequestBody.create(object.toString(),MediaType.get("application/json; charset=utf-8"));
            Log.e("requestBody","The RequestBody is :" + requestBody);
            Request request=new Request.Builder()
                    .url(URL)
                    .post(requestBody)
                    .build();
            Log.e("Request","The Request is :" + request);
            new Thread(()->{
                try{
                    Response response=client.newCall(request).execute();
                    if (response.isSuccessful() && response.body() != null) {
                        String responseData = response.body().string();
                        // Start activity on the UI thread
                        Log.e("Response", "Success: " + responseData);
                        if(responseData!=null){
                            if (callback != null) {
                                callback.onSuccess(); // Notify success
                            }
                        }
                    }
                    else {
                        Log.e("Response", "Failed: " + response.message());
                        if (callback != null) {
                            callback.onFailure(response.message()); // Notify failure
                        }

                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                    if (callback != null) {
                        callback.onFailure(e.getMessage()); // Notify failure
                    }
                }
            }).start();
        }
        catch (JSONException e) {
            Log.e("JSON Error", "Failed to create JSON object: " + e.getMessage());
            if (callback != null) {
                callback.onFailure(e.getMessage()); // Notify failure
            }
        }
    }
}
