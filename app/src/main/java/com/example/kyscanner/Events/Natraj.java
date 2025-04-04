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

public class Natraj extends AppCompatActivity {
    Button CypherofMobs,Glitz,Ecstacy,CutaRug,Razzmatazz,Bliss;
    SharedPreferences sharedPreferences;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_natraj);
        toolbar=findViewById(R.id.EventToolbar);
        sharedPreferences = getSharedPreferences("SubEventPrefs", MODE_PRIVATE);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Sub Event Details");
        }
        CypherofMobs=findViewById(R.id.CypherofMobs);
        Glitz=findViewById(R.id.Glitz);
        Ecstacy=findViewById(R.id.Ecstacy);
        CutaRug=findViewById(R.id.CutARug);
        Razzmatazz=findViewById(R.id.Razzmatazz);
        Bliss=findViewById(R.id.Bliss);
        CypherofMobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "Cypher of Mobs");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);
                startActivity(intent);
                finish();
            }
        });
        Glitz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "Glitz");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);
                startActivity(intent);
                finish();
            }
        });
        Ecstacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "Ecstacy");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);
                startActivity(intent);
                finish();
            }
        });
        Razzmatazz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "Razzmatazz");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);
                startActivity(intent);
                finish();
            }
        });
        Bliss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "Bliss");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);
                intent.putExtra("event_name", "Bliss");
                intent.putExtra("eventId",4);
                startActivity(intent);
                finish();
            }
        });
        CutaRug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "Cut a Rug");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);

                startActivity(intent);
                finish();
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