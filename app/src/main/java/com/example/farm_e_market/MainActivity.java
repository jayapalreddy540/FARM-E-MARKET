package com.example.farm_e_market;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

    private String TAG = "MainActivity.java";
    private AppBarConfiguration mAppBarConfiguration;
    private TextView profileName;
    private TextView profileEmail;

    private FirebaseAuth mAuth;

    ProgressDialog mProgressDialog;
    FirebaseFirestore db;

    private View navHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        /*  APP UPDATE CODE */

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        DocumentReference docRef = db.collection("app").document("current_version");

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            String app_version_code= Objects.requireNonNull(document.get("app_version_code")).toString();
                            String app_version_name= Objects.requireNonNull(document.get("app_version_name")).toString();
                            String app_link= Objects.requireNonNull(document.get("app_link")).toString();
                                Log.d("remote_app_version : ",app_version_code+"."+app_version_name);
                            try {
                                PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                                if (Integer.parseInt(app_version_code) >= pInfo.versionCode) {
                                    if (Double.parseDouble(app_version_name) > Double.parseDouble(pInfo.versionName)) {
                                        Log.d("Update Message : ", "App Update Available");
                                        Toast.makeText(MainActivity.this,"App Update Available",Toast.LENGTH_SHORT).show();
                                        try {
                                            FirebaseStorage storage = FirebaseStorage.getInstance();
                                            StorageReference storageRef = storage.getReferenceFromUrl(app_link);

                                            String fileName="FARM_E_MARKET.apk";
                                            File dir=getFilesDir();
                                            final File file=new File(dir,fileName);

                                            final Uri uri = FileProvider.getUriForFile(MainActivity.this, "com.example.farm_e_market.fileprovider", file);

                                            mProgressDialog = new ProgressDialog(MainActivity.this);
                                            mProgressDialog.setMessage("App Update Available Downloading...");
                                            mProgressDialog.setIndeterminate(true);
                                            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                            mProgressDialog.setCancelable(true);
                                            mProgressDialog.show();
                                            storageRef.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                    Log.d("firebase ", ";local tem file created " + file.toString());
                                                    Toast.makeText(MainActivity.this,"App Update Downloaded Ready for Installation.",Toast.LENGTH_LONG).show();
                                                    mProgressDialog.dismiss();
                                                    AppUpdate appUpdate =new AppUpdate();
                                                    appUpdate.updateApp(MainActivity.this,uri);
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception exception) {
                                                    Toast.makeText(MainActivity.this,"App Downloading Failed.",Toast.LENGTH_SHORT).show();
                                                    Log.d("firebase ", ";local tem file not created " + exception.toString());
                                                }
                                            });
                                        } catch (Exception e) {
                                            Log.d(TAG, e.toString());
                                        }
                                    }
                                }
                                String version = pInfo.versionCode + "." + pInfo.versionName;
                                Log.d("versions", "app_verison : " + version + " remote_app_version : " + Integer.parseInt(app_version_code) + "." + Double.parseDouble(app_version_name));
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });



        /*  END OF APP UPDATE */


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Snackbar.make(view, "Add Product", Snackbar.LENGTH_LONG)
               //         .setAction("Action", null).show();
                Intent intent=new Intent(MainActivity.this,AddProductActivity.class);
                startActivity(intent);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        navHeader = navigationView.getHeaderView(0);
        profileName = (TextView) navHeader.findViewById(R.id.profileName);
        profileEmail = (TextView) navHeader.findViewById(R.id.profileEmail);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            profileName.setText(name);
            profileEmail.setText(email);
            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_myproducts, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_feedback)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

       // checkInternetConnection();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                Toast.makeText(MainActivity.this,"Logging Out... ",Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
                return true;
            case R.id.action_version:
                Intent aboutIntent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(aboutIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void checkInternetConnection(){
        boolean isConnected=ConnectivityReceiver.isConnected();
        showSnackBar(isConnected);
    }

    private void showSnackBar(boolean isConnected) {
        String message;
        int color;
        if(isConnected) {
            message = "Connected.";
            color = Color.GREEN;
        }else {
            message="No Internet Connection.";
            color=Color.RED;
        }

        Log.d("connected to network : ",isConnected+"");

        Snackbar snackbar=Snackbar.make(findViewById(R.id.nav_view),message,Snackbar.LENGTH_LONG);

        View view =snackbar.getView();
        TextView textView=view.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected){
        showSnackBar(isConnected);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null) {
            Intent mainIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(mainIntent);
            finish();
        }

        FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications");

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Log.d(TAG, token);
                        Log.d(TAG,db.toString());
                        //Map<String, Object> data = new HashMap<>();
                        //data.put("token", Arrays.asList(token));
                        DocumentReference docRef = db.collection("app").document("tokens");
                        docRef.update("tokenArray",FieldValue.arrayUnion(token));
                    // Atomically add a new region to the "regions" array field.
                        //docRef.update("user_tokens", FieldValue.arrayUnion(token));

                        // Atomically remove a region from the "regions" array field.
                        //washingtonRef.update("regions", FieldValue.arrayRemove("east_coast"));
                    }
                });

        final IntentFilter intentFilter =new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");

        ConnectivityReceiver connectivityReceiver=new ConnectivityReceiver();
        registerReceiver(connectivityReceiver,intentFilter);

        MyApp.getInstance().setConnectivityListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}