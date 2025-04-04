package com.example.kyscanner.hospitality;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.kyscanner.R;
import com.example.kyscanner.mainclasses.CodeScanner;
import com.example.kyscanner.mainclasses.Format;

public class InvalidHospitalityUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invalid_hospitality_user);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent2=new Intent(InvalidHospitalityUser.this, hospitalityScanner.class);
        startActivity(intent2);
        finishAffinity();
    }
}