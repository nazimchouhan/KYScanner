package com.example.kyscanner.mainclasses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.ListenableWorker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.example.kyscanner.Events.EventList;
import com.example.kyscanner.R;
import com.example.kyscanner.hospitality.hospitalityScanner;
import com.example.kyscanner.model.UserModel;
import com.example.kyscanner.security.DataFetchSuccessFully;
import com.example.kyscanner.security.FetchUserDetail;

import java.util.List;

public class Format extends AppCompatActivity {
    TextView BoysSecurity,hospitality,event,GirlsSecurity;
    Toolbar MainToolbar;
    Runnable callback;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_format);
        sharedPreferences = getSharedPreferences("GenderPrefs", MODE_PRIVATE);
        MainToolbar=findViewById(R.id.MainToolbar);
        BoysSecurity=findViewById(R.id.BoysSecurity);
        GirlsSecurity=findViewById(R.id.GirlsSecurity);
        hospitality=findViewById(R.id.Hospitality);
        event=findViewById(R.id.Event);
        setSupportActionBar(MainToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Home Page");
        }
        BoysSecurity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("gender", "male");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), CodeScanner.class);
                startActivity(intent);
                finish();
            }
        });
        GirlsSecurity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("gender", "female");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), CodeScanner.class);
                startActivity(intent);
                finish();
            }
        });
        hospitality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), hospitalityScanner.class);
                startActivity(intent);
                finish();
            }
        });
        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), EventList.class);
                startActivity(intent);
                finish();
            }
        });

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuitem, menu);
        return true;
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            SharedPreferences sharedPreferences1 = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences1.edit();
            editor.putBoolean("isLoggedIn", false);
            editor.apply();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_status) {
            try{
                FetchUserDetail.fetchAndSaveUserData();
                while (!FetchUserDetail.isDataReady()) { // Retry max 5 times
                    Log.e("UpdateKYidWorker", "UserModel is Empty, waiting for data... Retry: ");

                }
                List<UserModel> userModels= FetchUserDetail.getuserList();
                if(userModels.isEmpty()){
                    Log.e("UpdateKYidWorker", "UserModel is Empty, waiting for data...");
                }
                Log.e("UpdateKyIdWorker", "KY ID Set size saved in SharedPreferences in Format Class: " + userModels.size());
                FetchUserDetail.UpdateDB(getApplicationContext(),userModels);
                // Send a broadcast to notify MainActivity about data change
                Intent intent1=new Intent(getApplicationContext(), DataFetchSuccessFully.class);
                startActivity(intent1);
                return true;
            }
            catch(Exception e){
                Log.e("Fetching", "Fetching UserDetails is Failed: " + e.getMessage(), e);
            }


        } else {
            return super.onOptionsItemSelected(item);
        }
        return false;
    }
}
