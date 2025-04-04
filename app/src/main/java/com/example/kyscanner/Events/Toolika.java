package com.example.kyscanner.Events;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kyscanner.R;

public class Toolika extends AppCompatActivity {
    Button LiveSketching,SoapCarving,RapidFire,InkIt,Rangriti,VastraShilp,FacePainting,SpoiltheTees,AetherandAsh;
    Toolbar toolbar;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolika);
        sharedPreferences = getSharedPreferences("SubEventPrefs", MODE_PRIVATE);
        toolbar=findViewById(R.id.EventToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Sub Event Details");
        }
        LiveSketching=findViewById(R.id.LiveSketching);
        SoapCarving=findViewById(R.id.SoapCarving);
        RapidFire=findViewById(R.id.RapidFire);
        InkIt=findViewById(R.id.InkIt);
        Rangriti=findViewById(R.id.Rangriti);
        VastraShilp=findViewById(R.id.VastraShilp);
        FacePainting=findViewById(R.id.FacePainting);
        SpoiltheTees=findViewById(R.id.SpoiltheTees);
        AetherandAsh=findViewById(R.id.AetherandAsh);
        LiveSketching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "Live Sketching");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);

                startActivity(intent);
                finishAffinity();
            }
        });
        SoapCarving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "Soap Carving");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);

                startActivity(intent);
                finishAffinity();
            }
        });
        RapidFire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "Rapid Fire");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);

                startActivity(intent);
                finishAffinity();
            }
        });
        InkIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "Ink It");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);

                startActivity(intent);
                finishAffinity();
            }
        });
        Rangriti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "Rangriti");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);

                startActivity(intent);
                finishAffinity();
            }
        });
        VastraShilp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "Vastra Shilp");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);

                startActivity(intent);
                finishAffinity();
            }
        });
        FacePainting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "Face Painting");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);

                startActivity(intent);
                finishAffinity();
            }
        });
        SpoiltheTees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "Spoil the Tees");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);

                startActivity(intent);
                finishAffinity();
            }
        });
        AetherandAsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "Aether and Ash");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);

                startActivity(intent);
                finishAffinity();
            }
        });
    }
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent2=new Intent(getApplicationContext(), EventList.class);
        startActivity(intent2);
        finishAffinity();
    }
}