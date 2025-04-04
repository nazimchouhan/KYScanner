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
import com.example.kyscanner.mainclasses.Format;

public class Bandish extends AppCompatActivity {
    Button RagSamar,Sur,Kriti,SwarSangati,Sanlayan;
    SharedPreferences sharedPreferences;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bandish);
        sharedPreferences = getSharedPreferences("SubEventPrefs", MODE_PRIVATE);
        toolbar=findViewById(R.id.EventToolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle("Sub Event Details");
        }
        RagSamar=findViewById(R.id.RagSamar);
        Sur=findViewById(R.id.Sur);
        Kriti=findViewById(R.id.Kriti);
        SwarSangati=findViewById(R.id.SwarSangati);
        Sanlayan=findViewById(R.id.Sanlayan);
        RagSamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "Raagsamar");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);
                startActivity(intent);
                finish();
            }
        });
        Sur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "Sur");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);
                startActivity(intent);
                finish();
            }
        });
        Kriti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "Kriti");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);
                startActivity(intent);
                finish();

            }
        });
        SwarSangati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "Swar Sangati");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);
                startActivity(intent);
                finish();

            }
        });
        Sanlayan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "Sanlayan");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);
                startActivity(intent);
                finish();

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