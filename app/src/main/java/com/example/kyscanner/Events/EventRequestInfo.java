package com.example.kyscanner.Events;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.kyscanner.R;
import com.example.kyscanner.mainclasses.Format;

public class EventRequestInfo extends AppCompatActivity {
    Button EventRequestButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_request_info);
        EventRequestButton=findViewById(R.id.ProceedButton);
        EventRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), EventScanner.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent2=new Intent(EventRequestInfo.this, EventScanner.class);
        startActivity(intent2);
        finishAffinity();

    }
}