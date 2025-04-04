package com.example.kyscanner.hospitality;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.kyscanner.R;

public class hospitalityView extends AppCompatActivity {
    TextView userName,KYID,college,MobileNo,Gmail,Food,Accomodation,Tshirt;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospitality_view);
        toolbar=findViewById(R.id.HospitalityViewToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("User Detail Page");
        }
        userName=findViewById(R.id.userName);
        KYID=findViewById(R.id.KYID);
        college=findViewById(R.id.College);
        MobileNo=findViewById(R.id.MobileNo);
        Gmail=findViewById(R.id.gmail);
        Food=findViewById(R.id.foodStatus);
        Accomodation=findViewById(R.id.AccomodationStatus);
        Tshirt=findViewById(R.id.TshirtStatus);
        Intent intent=getIntent();
        if(intent!=null){
            String name=intent.getStringExtra("userName");
            String KYiD= intent.getStringExtra("KYId");
            String collge= intent.getStringExtra("college");
            String gmail= intent.getStringExtra("Gmail");
            String mobileNo= intent.getStringExtra("mobileNo");
            boolean food= intent.getBooleanExtra("hasfood",false);
            boolean accomodation= intent.getBooleanExtra("hasAccomodation",false);
            boolean tshirt= intent.getBooleanExtra("hasTshirt",false);
            Log.e("userdetails","User detail are :" + name + KYiD + collge + gmail + mobileNo + food + accomodation + tshirt);
            userName.setText(name != null ? name : "Null");
            KYID.setText(KYiD != null ? KYiD : "Null");
            college.setText(collge != null ? collge : "Null");
            MobileNo.setText(mobileNo != null ? mobileNo : "Null");
            Gmail.setText(gmail != null ? gmail : "Null");

            Food.setText(food ? "YES" : "NO");
            Accomodation.setText(accomodation ? "YES" : "NO");
            Tshirt.setText(tshirt ? "YES" : "NO");
        } else {
            // **If intent is null, set all TextViews to "Not Available"**
            userName.setText("Null");
            KYID.setText("Null");
            college.setText("Null");
            MobileNo.setText("Null");
            Gmail.setText("Null");

            Food.setText("NO");
            Accomodation.setText("NO");
            Tshirt.setText("NO");
        }


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent= new Intent(getApplicationContext(),hospitalityScanner.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finishAffinity();
    }
}