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

public class Enquizta extends AppCompatActivity {
    Button IndianQuiz,SciBizTechQuiz,GeneralQuiz,FoodQuiz,SportsQuiz,MelaQuiz;
    SharedPreferences sharedPreferences;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquizta);
        sharedPreferences = getSharedPreferences("SubEventPrefs", MODE_PRIVATE);
        toolbar=findViewById(R.id.EventToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Sub Event Details");
        }
        IndianQuiz=findViewById(R.id.IndianQuiz);
        SciBizTechQuiz=findViewById(R.id.SciBizTechQuiz);
        GeneralQuiz=findViewById(R.id.GeneralQuiz);
        FoodQuiz=findViewById(R.id.FoodQuiz);
        SportsQuiz=findViewById(R.id.SportsQuiz);
        MelaQuiz=findViewById(R.id.MelaQuiz);
        IndianQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "India Quiz");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);
                startActivity(intent);
                finish();
            }
        });
        SciBizTechQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "Sci-Biz-Tech Quiz");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);
                startActivity(intent);
                finish();
            }
        });
        GeneralQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "General Quiz");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);
                intent.putExtra("event_name", "General Quiz");
                intent.putExtra("Class_name",getClass().getSimpleName());
                intent.putExtra("eventId",48);
                startActivity(intent);
                finish();
            }
        });
        FoodQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "Food Quiz");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);
                startActivity(intent);
                finish();
            }
        });
        SportsQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "Sports Quiz");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);
                startActivity(intent);
                finish();
            }
        });
        MelaQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "Mela Quiz");
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