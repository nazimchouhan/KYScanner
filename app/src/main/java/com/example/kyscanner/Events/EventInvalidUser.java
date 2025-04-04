package com.example.kyscanner.Events;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.kyscanner.R;
import com.example.kyscanner.mainclasses.Format;

public class EventInvalidUser extends AppCompatActivity {
    Button EventInvalidButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_invalid_user);
        EventInvalidButton=findViewById(R.id.InvalidButtonEvent);
        EventInvalidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2=new Intent(EventInvalidUser.this, EventScanner.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
                finishAffinity();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent2=new Intent(EventInvalidUser.this, EventScanner.class);
        startActivity(intent2);
        finishAffinity();
    }
}