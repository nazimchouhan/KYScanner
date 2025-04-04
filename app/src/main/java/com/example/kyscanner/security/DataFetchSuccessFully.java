package com.example.kyscanner.security;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.kyscanner.R;
import com.example.kyscanner.mainclasses.CodeScanner;
import com.example.kyscanner.mainclasses.Format;

public class DataFetchSuccessFully extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_fetch_success_fully);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent2=new Intent(DataFetchSuccessFully.this, Format.class);
        startActivity(intent2);
        finishAffinity();
    }
}