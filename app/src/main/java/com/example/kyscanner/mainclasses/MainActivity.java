package com.example.kyscanner.mainclasses;



import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kyscanner.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    Button loginButton;
    Toolbar LoginToolbar;
    private BroadcastReceiver userListReceiver;
    private static final String URL="https://apiv2.kashiyatra.org/api/user/login";
    TextView userSize;
    EditText LoginEmail, LoginPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        if (isLoggedIn) {
            // Redirect directly to CodeScannerActivity
            Intent intent = new Intent(this, Format.class);
            startActivity(intent);
            finish();  // Close MainActivity to prevent going back
        }
        userSize=findViewById(R.id.userSize);

        loginButton = findViewById(R.id.LoginButton);
        LoginEmail = findViewById(R.id.LoginEmail);
        LoginPass = findViewById(R.id.LoginPassword);
        LoginToolbar = findViewById(R.id.LoginToolbar);
        // Set up the toolbar
        setSupportActionBar(LoginToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Sign In Page");
        }


        // Set up the login button click listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
        updateUserListText();

        // Register BroadcastReceiver
        userListReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if ("User_List_UPDATED".equals(intent.getAction())) {
                    int updatedSize = intent.getIntExtra("UserList", 0);
                    userSize.setText("User List Size: " + updatedSize);
                }
            }
        };
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(userListReceiver, new IntentFilter("User_List_UPDATED"));
    }

    private void loginUser() {
        String email = LoginEmail.getText().toString().trim();
        String password = LoginPass.getText().toString().trim();

        // Basic validation
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }
        sendAuthRequestToServer(email,password);

        // Simulate a login process (replace with actual API call)

    }
    public void sendAuthRequestToServer(String Email,String Password){
        OkHttpClient client=new OkHttpClient();
        try{
            JSONObject object=new JSONObject();
            object.put("gmail",Email);
            object.put("password",Password);
            Log.e("Request Body", object.toString());
            //RequestBody requestBody=RequestBody.create(object.toString(), MediaType.get("application/json; charset=utf-8"));
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), object.toString());
            Log.e("Requestbody","RequestBody is" + requestBody);
            if(requestBody!=null){
                Log.e("Request body","Request Body is not null" +requestBody);
            }
            Request request=new Request.Builder()
                    .url(URL)
                    .header("Login-Method","password")
                    .header("Content-Type", "application/json")
                    .post(requestBody)
                    .build();
            Log.e("resuest","The Request is :" + request);
            new Thread(()->{
                try{
                    Response response=client.newCall(request).execute();

                    if (response.isSuccessful() && response.body() != null) {
                        String responseData = response.body().string();
                        if(responseData!=null){
                            // Save token in SharedPreferences
                            SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("AuthToken", responseData);  // Save token
                            editor.putBoolean("isLoggedIn", true);
                            editor.apply();
                            Intent intent=new Intent(getApplicationContext(), Format.class);
                            intent.putExtra("AuthToken",responseData);
                            startActivity(intent);
                            finish();
                        }
                        Log.e("Response", "Success: " + responseData);
                    } else {
                        runOnUiThread(() -> Toast.makeText(MainActivity.this, "Incorrect Credentials", Toast.LENGTH_SHORT).show());

                        Log.e("Response", "Failed: " + response.message());
                    }
                }
                catch (IOException e) {
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show());
                    e.printStackTrace();
                }
            }).start();
        }
        catch (JSONException e) {
            Log.e("JSON Error", "Failed to create JSON object: " + e.getMessage());
        }
    }

    private void updateUserListText() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyListPrefs", Context.MODE_PRIVATE);
        int savedSize = sharedPreferences.getInt("UserList", 0);
        userSize.setText("User List Size: " + savedSize);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(userListReceiver, new IntentFilter("User_List_UPDATED"));
    }
}
