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
import com.example.kyscanner.mainclasses.CodeScanner;
import com.example.kyscanner.mainclasses.Format;

public class EventList extends AppCompatActivity {
    Button Abhinay,Bandish,Crosswindz,Enquizta,Mirage,Natraj,Samwaad,Toolika;
    SharedPreferences sharedPreferences;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        sharedPreferences = getSharedPreferences("EventPrefs", MODE_PRIVATE);
        toolbar=findViewById(R.id.EventToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Event Details");
        }
        Abhinay=findViewById(R.id.Abhinay);
        Bandish=findViewById(R.id.Bandish);
        Crosswindz=findViewById(R.id.Crosswindz);
        Enquizta=findViewById(R.id.Enquizta);
        Mirage=findViewById(R.id.Mirage);
        Natraj=findViewById(R.id.Natraj);
        Samwaad=findViewById(R.id.Samwaad);
        Toolika=findViewById(R.id.Toolika);
        Abhinay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selected_event", "Abhinay");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), AbhinayList.class);
                startActivity(intent);
                finishAffinity();
            }
        });
        Bandish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selected_event", "Bandish");
                editor.apply();

                Intent intent=new Intent(getApplicationContext(), Bandish.class);
                startActivity(intent);
                finishAffinity();
            }
        });
        Crosswindz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selected_event", "Crosswindz");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), CrossWindz.class);
                startActivity(intent);
                finishAffinity();
            }
        });
        Enquizta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selected_event", "Enquizta");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), Enquizta.class);
                startActivity(intent);
                finishAffinity();
            }
        });
        Mirage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selected_event", "Mirage");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), Mirage.class);
                startActivity(intent);
                finishAffinity();
            }
        });
        Natraj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selected_event", "Natraj");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), Natraj.class);
                startActivity(intent);
                finishAffinity();
            }
        });
        Samwaad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selected_event", "Samwaad");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(),Samwaad.class);
                startActivity(intent);
                finishAffinity();
            }
        });
        Toolika.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selected_event", "Toolika");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), Toolika.class);
                startActivity(intent);
                finishAffinity();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent2=new Intent(getApplicationContext(), Format.class);
        startActivity(intent2);
        finishAffinity();
    }

}