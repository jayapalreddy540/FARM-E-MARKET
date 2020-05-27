package com.example.farm_e_market;

import androidx.annotation.NonNull;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton,registerButton;
    private ProgressBar loadingProgressBar;
    private FirebaseAuth mAuth;
    private String TAG="LoginActivity";
    private String email,password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        emailEditText =(EditText)findViewById(R.id.email);
        passwordEditText = (EditText)findViewById(R.id.password);
        loginButton =(Button)findViewById(R.id.btnLogin);
        loadingProgressBar = (ProgressBar)findViewById(R.id.loading);
        registerButton = (Button)findViewById(R.id.btnRegister);

        mAuth = FirebaseAuth.getInstance();


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                email=emailEditText.getText().toString();
                password=passwordEditText.getText().toString();
                Log.d(TAG,"email : "+email);
                if(email.equals("")||password.equals("")){
                    Snackbar snackbar = Snackbar
                            .make(v, "Enter all the Details.", Snackbar.LENGTH_LONG);
                    snackbar.setTextColor(Color.WHITE);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.show();
                }
                else {
                    /* Connecting to Firebase */
                    loadingProgressBar.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    loadingProgressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        if(user.isEmailVerified()){
                                            Toast.makeText(LoginActivity.this, "Authentication success.",
                                                    Toast.LENGTH_SHORT).show();
                                            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(mainIntent);
                                            finish();
                                        }
                                        else{
                                            Snackbar snackbar = Snackbar
                                                    .make(v, "Please Verify Your Email for Authentication.", Snackbar.LENGTH_LONG);
                                            snackbar.setTextColor(Color.WHITE);
                                            snackbar.setBackgroundTint(Color.GREEN);
                                            snackbar.show();
                                        }

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, "Authentication failed." + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
                finish();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        }
    }

}
