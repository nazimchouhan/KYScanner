package com.example.kyscanner.Events;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.kyscanner.R;
import com.example.kyscanner.mainclasses.Format;

public class CrossWindz extends AppCompatActivity {
    TextView BattleofBands,Strumento,Auralis,Spitfire,BoxBlitz;
    SharedPreferences sharedPreferences;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cross_windz);
        sharedPreferences = getSharedPreferences("SubEventPrefs", MODE_PRIVATE);
        toolbar=findViewById(R.id.EventToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Sub Event Details");
        }
        BattleofBands=findViewById(R.id.BattleOfBands);
        Strumento=findViewById(R.id.Strumento);
        Auralis=findViewById(R.id.Auralis);
        Spitfire=findViewById(R.id.Spitfire);
        BoxBlitz=findViewById(R.id.BoxBlitz);
        BattleofBands.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "Battle of Bands");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);
                startActivity(intent);
                finishAffinity();
            }
        });
        Strumento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "Strumento");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);
                startActivity(intent);
                finishAffinity();
            }
        });
        Auralis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "Auralis");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);
                startActivity(intent);
                finishAffinity();
            }
        });
        Spitfire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "Spitfire");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);
                startActivity(intent);
                finishAffinity();
            }
        });
        BoxBlitz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "Box-Blitz");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);
                startActivity(intent);
                finishAffinity();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent2=new Intent(getApplicationContext(), EventList.class);
        startActivity(intent2);
        finishAffinity();
    }
}