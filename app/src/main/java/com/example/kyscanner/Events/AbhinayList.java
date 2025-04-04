package com.example.kyscanner.Events;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.metrics.Event;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kyscanner.R;
import com.example.kyscanner.mainclasses.CodeScanner;
import com.example.kyscanner.mainclasses.Format;

public class AbhinayList extends AppCompatActivity {
    Button Rangmanch,Asmita,Hullad,Cheshta;
    SharedPreferences sharedPreferences;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abhinay_list);
        sharedPreferences = getSharedPreferences("SubEventPrefs", MODE_PRIVATE);
        toolbar=findViewById(R.id.EventToolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle("Sub Event Details");
        }
        Rangmanch=findViewById(R.id.Rangmanch);
        Asmita=findViewById(R.id.Asmita);
        Hullad=findViewById(R.id.Hullad);
        Cheshta=findViewById(R.id.Cheshta);
        Rangmanch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "Rangmanch");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(),EventScanner.class);

                startActivity(intent);
                finish();
            }
        });
        Asmita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "Asmita");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(),EventScanner.class);
                startActivity(intent);
                finish();
            }
        });
        Hullad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "Hullad");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(),EventScanner.class);
                intent.putExtra("event_name", "Hullad");
                intent.putExtra("Class_name",getClass().getSimpleName());
                intent.putExtra("eventId",6);
                startActivity(intent);
                finish();
            }
        });
        Cheshta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "Cheshta");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(),EventScanner.class);
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