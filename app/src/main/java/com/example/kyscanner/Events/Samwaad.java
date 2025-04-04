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
public class Samwaad extends AppCompatActivity {
    Button Scripturesque,WhatsTheWord,JustaMinute,Battlefront,INDIANHELMQUIZ,HINDIDEBATE,HINDISTORYTELLING,HINDIPOETRYSLAM,AJesterCourt,Aashubhashad,Legendofsirspeakalot,Improvecomedy,EnglishDebate;
    Toolbar toolbar;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_samwaad);
        toolbar=findViewById(R.id.EventToolbar);
        sharedPreferences = getSharedPreferences("SubEventPrefs", MODE_PRIVATE);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Sub Event Details");
        }
        Scripturesque=findViewById(R.id.Scripturesque);
        WhatsTheWord=findViewById(R.id.WhatsTheWord);
        JustaMinute=findViewById(R.id.JustaMinute);
        INDIANHELMQUIZ=findViewById(R.id.INDIANHELMQUIZ);
        HINDIDEBATE=findViewById(R.id.HINDIDEBATE);
        HINDISTORYTELLING=findViewById(R.id.HINDISTORYTELLING);
        HINDIPOETRYSLAM=findViewById(R.id.HINDIPOETRYSLAM);
        AJesterCourt=findViewById(R.id.AJesterCourt);
        Aashubhashad=findViewById(R.id.Aashubhashad);
        Legendofsirspeakalot=findViewById(R.id.Legendofsirspeakalot);
        Improvecomedy=findViewById(R.id.Improvecomedy);
        EnglishDebate=findViewById(R.id.EnglishDebate);

        Scripturesque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "Scripturesque");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);
                startActivity(intent);
                finish();
            }
        });
        WhatsTheWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "Whats the Word?");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);
                startActivity(intent);
                finish();
            }
        });
        JustaMinute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "Just a Minute ");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);
                startActivity(intent);
                finish();
            }
        });
        INDIANHELMQUIZ.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "हिन्द हििंदी हििंदुस्तान");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);
                startActivity(intent);
                finish();
            }
        });
        HINDIDEBATE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "तर्कसिंगत");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);
                startActivity(intent);
                finish();
            }
        });
        HINDISTORYTELLING.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "ह़िस्सागोई: HINDI STORY TELLING");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);
                startActivity(intent);
                finish();
            }
        });
        HINDIPOETRYSLAM.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "मधुरिमा");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);
                startActivity(intent);
                finish();
            }
        });
        AJesterCourt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "A Jester's Court");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);
                startActivity(intent);
                finish();
            }
        });
        Aashubhashad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "आशुभाषण");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);
                startActivity(intent);
                finish();
            }
        });
        Legendofsirspeakalot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "The Legend of Sir Speak-A-Lot ");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);
                startActivity(intent);
                finish();
            }
        });
        Improvecomedy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "Improve Comedy ");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);
                startActivity(intent);
                finish();
            }
        });
        EnglishDebate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "Battlefront");
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