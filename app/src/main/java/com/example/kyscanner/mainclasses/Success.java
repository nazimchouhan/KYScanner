package com.example.kyscanner.mainclasses;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import android.view.View;
import android.widget.Button;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.kyscanner.security.FlashLightManager;
import com.example.kyscanner.R;

public class Success extends AppCompatActivity {
    Toolbar userDetailToolbar;
    CardView cardview;
    TextView IDText;
    TextView GmailText;
    ProgressBar SuccessProgressBar;
    FlashLightManager flashlightManager;
    TextView UserName;
    Button SuccessButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        flashlightManager = FlashLightManager.getInstance(this);
        IDText=findViewById(R.id.KYidstatus);
        userDetailToolbar=findViewById(R.id.UserDetailToolbar);
        GmailText=findViewById(R.id.GmailStatus);
        cardview=findViewById(R.id.cardView);
        SuccessButton=findViewById(R.id.SuccessButton);


        UserName=findViewById(R.id.userName);
        SuccessProgressBar=findViewById(R.id.SuccessProgressbar);
        setSupportActionBar(userDetailToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("User Detail Page");
        }
        Intent intent=getIntent();
        String kyid=intent.getStringExtra("KYId");
        String Username=intent.getStringExtra("userName");
        String UserGmail = getIntent().getStringExtra("Gmail");
        boolean flashOn = getIntent().getBooleanExtra("flash_on", false);
        if (flashOn) {
            flashlightManager.toggleFlashlight();
        }
        long ScanTime = getIntent().getLongExtra("ScanTime", 0);
        Toast.makeText(this, "The Scan Time is:" + ScanTime + "ms", Toast.LENGTH_SHORT).show();

        if (Username != null && UserGmail != null) {
            UserName.setText(Username);
            IDText.setText(kyid);
            GmailText.setText(UserGmail);
        }
        SuccessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 =new Intent(getApplicationContext(), CodeScanner.class);
                //intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent1);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent2=new Intent(Success.this, CodeScanner.class);
        //intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent2);
        finish();
    }
}

