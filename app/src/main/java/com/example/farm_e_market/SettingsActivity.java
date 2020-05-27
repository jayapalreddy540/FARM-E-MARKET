package com.example.farm_e_market;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {
    private TextView name,mobile,email;
    private CircleImageView imageView;
    private Button updateBtn;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        name=(TextView)findViewById(R.id.name);
        mobile=(TextView)findViewById(R.id.mobile);
        email=(TextView)findViewById(R.id.email);
        imageView=(CircleImageView) findViewById(R.id.image);
        updateBtn=(Button)findViewById(R.id.updateBtn);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        name.setText("Name : "+currentUser.getDisplayName());
        email.setText("Email : "+currentUser.getEmail());
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("data")
                .document(currentUser.getUid())
                .collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                               if (task.isSuccessful()) {
                                                   for (QueryDocumentSnapshot document : task.getResult()) {
                                                       //Log.d(TAG, document.getId() + " => " + document.getData());
                                                       mobile.setText("Mobile : " + document.get("mobile"));
                                                       break;
                                                   }
                                               }
                                           }
                                       });
        imageView.setImageResource(R.drawable.farmer);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingsActivity.this,UpdateMobileActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}