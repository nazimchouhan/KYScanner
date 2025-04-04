package com.example.kyscanner.Events;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.kyscanner.R;
import com.example.kyscanner.model.EventUserModel;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TeamInfo extends AppCompatActivity {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    EditText teamname;
    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferences1;
    ProgressBar progressbar;
    String SubeventName;
    String eventName;

    Button ProceedButton;
    HashMap<String,String> map=new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_info);
        teamname=findViewById(R.id.TeamName);
        ProceedButton=findViewById(R.id.ProceedButton);
        progressbar=findViewById(R.id.InfoProgressBar);
        progressbar.setVisibility(View.GONE);
        map.put("Abhinay","https://script.google.com/macros/s/AKfycbysosZcgc6XH0W6xRkSi4A6lQySVTWDCBFTmav02GZDESeJB-waE99jOUVifROXRAQf/exec");
        map.put("Bandish","https://script.google.com/macros/s/AKfycbxm7B9mRjBm0XA9BH7OTr47hbXyrEkDHown-3cS9pgYEdm5YwBLpUErcrVug9_Qwb3chQ/exec");
        map.put("Crosswindz","https://script.google.com/macros/s/AKfycbzXzg91iIq4FGPqracL_jd5A_0CmooeGxdZ-EdXtk8eCMI8qgllXaAXzUKb4q-CD56Zfg/exec");
        map.put("Enquizta","https://script.google.com/macros/s/AKfycbzvF2O5NFOZjDlCd9ErCkX9FD3YUOGuovhgmijsBcVAu-_Xo1H3Emn3z69Jm-SohuoO/exec");
        map.put("Mirage","https://script.google.com/macros/s/AKfycbzxJKvOFlAr98Km6dNRZKVQPj2SLYFUWBNZsPelnkOJpUum9bfQ8Vqwp2tXJT0qcK6yyg/exec");
        map.put("Natraj","https://script.google.com/macros/s/AKfycbzEjt9NTgLrYZhSs9WayXUbg93E-mn9n4Kbnwqnjpxbm4yNPrl1W6zzIvBfKHQ5H3U/exec");
        map.put("Samwaad","https://script.google.com/macros/s/AKfycbzSS7gKXLPrX4KY7erAhJlspUS2jHtA9WfXzU4eewezSyPMsvKTzzMgp0GzPk82ZWk/exec");
        map.put("Toolika","https://script.google.com/macros/s/AKfycbyVGYBCzRHTRTI3chkLUiKNIyK5Sp2qc3j1pHxj8b1zvoK4Kw554J6NZgsq8Q60ubUo/exec");


        String ky_id=getIntent().getStringExtra("ky_id");

        Log.e("EventName","The SubeventName is :" + SubeventName);
        sharedPreferences = getSharedPreferences("EventPrefs", MODE_PRIVATE);
        eventName = sharedPreferences.getString("selected_event", "Unknown Event");
        sharedPreferences1=getSharedPreferences("SubEventPrefs", MODE_PRIVATE);
        SubeventName=sharedPreferences1.getString("selectedSub_event","Unknown Sub_Event");
        Log.e("EventName","The EventName is :" + eventName);
        Log.e("EventName","The SubeventName is :" + SubeventName);
        String teamName=teamname.getText().toString()!=null ? teamname.getText().toString() : "";

        ProceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ky_id != null && eventName!=null && SubeventName !=null) {
                    runOnUiThread(()->progressbar.setVisibility(View.VISIBLE));
                    // Initialize the Handler with the main thread's Looper
                    Handler handler = new Handler(Looper.getMainLooper());
                    // Execute database operation on a background thread
                    // Execute database operation on a background thread
                    executor.execute(() -> {
                        Future<List<EventUserModel>> futureList = EventServerRequest.fetchEventUserDetail(ky_id);
                        try {
                            List<EventUserModel> userDetails = futureList.get();
                            if(userDetails.size()==1){
                                EventUserModel model = userDetails.get(0);
                                // Switch to the main thread for UI updates
                                handler.post(() -> {
                                    if (model != null) {
                                        Log.e("UserDetail", "Successfully Fetched model");
                                        Log.e("KyID","The KYID is :" + model.getKYID());
                                        Log.e("Name","The Name is :" + model.getName());
                                        Log.e("PhoneNo","The Phone no is:" + model.getPhoneNo());
                                        Log.e("SubEvent","SubEvent name is :" + SubeventName);
                                        Log.e("CollegeName","the College Name is:" + model.getCollegeName());
                                        SendRequesttoServer.sendRequestToServer(
                                                getApplicationContext(),
                                                model,
                                                SubeventName,
                                                teamname.getText().toString(),
                                                map.get(eventName),
                                                new SendRequesttoServer.ServerResponseCallback() {
                                                    @Override
                                                    public void onSuccess() {
                                                        // Start the activity only when server request is successful
                                                        handler.post(() -> {
                                                            Intent intent = new Intent(getApplicationContext(), EventRequestInfo.class);
                                                            startActivity(intent);
                                                            progressbar.setVisibility(View.GONE);
                                                        });
                                                    }

                                                    @Override
                                                    public void onFailure(String errorMessage) {
                                                        Log.e("ServerError", "Failed to send request: " + errorMessage);
                                                        handler.post(() ->progressbar.setVisibility(View.GONE));
                                                        handler.post(() -> Toast.makeText(TeamInfo.this, "Failed to send request", Toast.LENGTH_SHORT).show());
                                                    }
                                                }
                                        );
                                    }else{
                                        Log.e("UserDetail", "Not successfully Fetched");
                                        runOnUiThread(() -> {
                                            Toast.makeText(TeamInfo.this,"Error Fetching User detail or Try Again",Toast.LENGTH_SHORT).show();
                                        });

                                    }
                                });
                            }
                            else{
                                runOnUiThread(() -> {
                                    Toast.makeText(TeamInfo.this, "The User is not found", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), EventInvalidUser.class);
                                    startActivity(intent);
                                });

                            }

                        }catch (ExecutionException e) {
                            Log.e("Evenet Detail","Error fetching Event Detail");
                            runOnUiThread(()->Toast.makeText(TeamInfo.this,"Please Check Your Internet Connection",Toast.LENGTH_SHORT));

                        } catch (InterruptedException e) {

                        }

                    });
                }else{
                    runOnUiThread(() -> {
                        Toast.makeText(TeamInfo.this,"Please Go back and Select the event again",Toast.LENGTH_SHORT);
                    });
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent2 = new Intent(TeamInfo.this, EventScanner.class);
        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent2);
        finish();  // Close all activities
    }
}