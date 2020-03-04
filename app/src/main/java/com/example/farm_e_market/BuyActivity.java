package com.example.farm_e_market;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BuyActivity extends AppCompatActivity {

    TextView textView;
    ImageView phone, message;
    String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        Intent intent = getIntent();
        if (intent.hasExtra("mobile")) {
            mobile = intent.getStringExtra("mobile");

            textView = (TextView) findViewById(R.id.textView);
            phone = (ImageView) findViewById(R.id.phone);
            message = (ImageView) findViewById(R.id.message);
            phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Log.d("mobile", mobile);
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + mobile));
                        if (ContextCompat.checkSelfPermission(BuyActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(BuyActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 100);
                        } else {
                            startActivity(callIntent);
                        }
                    }
                    catch (Exception e){
                        Log.d("call",e.getMessage());
                    }
                }
            });

            message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ContextCompat.checkSelfPermission(BuyActivity.this,
                            Manifest.permission.SEND_SMS)
                            != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(BuyActivity.this,
                                Manifest.permission.SEND_SMS)) {

                        } else {
                            ActivityCompat.requestPermissions(BuyActivity.this,
                                    new String[]{Manifest.permission.SEND_SMS},
                                    100);
                        }
                    }
                    else{
                        try {
                            SmsManager smgr = SmsManager.getDefault();
                            smgr.sendTextMessage(mobile,null,"I'm interested in your product @FARM-E-MARKET",null,null);
                            Toast.makeText(BuyActivity.this, "SMS Sent Successfully to the Farmer", Toast.LENGTH_SHORT).show();

                        }
                        catch (Exception e){
                            Toast.makeText(BuyActivity.this, "SMS Not Sent", Toast.LENGTH_SHORT).show();

                            Log.d("message",e.getMessage());
                        }
                    }

                }
            });

        }
    }
}
