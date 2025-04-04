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

public class Mirage extends AppCompatActivity {
    Button MrKY,MissKY,CostumeDesign,DesignElegante;
    SharedPreferences sharedPreferences;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mirage);
        sharedPreferences = getSharedPreferences("SubEventPrefs", MODE_PRIVATE);
        toolbar=findViewById(R.id.EventToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Sub Event Details");
        }
        MrKY=findViewById(R.id.MrKY);
        MissKY=findViewById(R.id.MissKY);
        CostumeDesign=findViewById(R.id.CostumeDesign);
        DesignElegante=findViewById(R.id.DesignElegante);
        MrKY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "Mr. KY");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);

                startActivity(intent);
                finish();
            }
        });
        MissKY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "Miss. KY");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);
                startActivity(intent);
                finish();
            }
        });
        CostumeDesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "Costume Design");
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);
                startActivity(intent);
                finish();
            }
        });
        DesignElegante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedSub_event", "Design Elegante");
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