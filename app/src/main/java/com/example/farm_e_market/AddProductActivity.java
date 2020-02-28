package com.example.farm_e_market;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import java.util.UUID;

public class AddProductActivity extends AppCompatActivity implements LocationListener {

    private FirebaseAuth mAuth;
    private EditText nameEditText,priceEditText,quantityEditText;
    private Button btnAdd;
    private ImageView imageView;
    private EditText locationDetails;
    private ImageButton imageButton;
    private String selectedCategory = "No Item";
    private ArrayList<String> group;

    private Uri selectedImageUri;
    private StorageReference ref;
    private String TAG="AddProductActivity";

    protected LocationManager locationManager;

    private double latitude=0.0;
    private double longitude=0.0;
    private int price=0;
    private int quantity=0;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add Product");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nameEditText=(EditText)findViewById(R.id.name);
        priceEditText=(EditText)findViewById(R.id.price);
        quantityEditText=(EditText)findViewById(R.id.quantity);
        btnAdd=(Button)findViewById(R.id.addBtn);
        imageView=(ImageView)findViewById(R.id.image);
        imageButton=(ImageButton) findViewById(R.id.location);
        locationDetails=(EditText)findViewById(R.id.locationText);

        requestQueue= Volley.newRequestQueue(this);

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        mAuth = FirebaseAuth.getInstance();

        final FirebaseUser currentUser = mAuth.getCurrentUser();


        DocumentReference docRef = db.collection("app").document("products");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document=task.getResult();
                    if(document!=null){
                        if(document.exists()){
                            group = (ArrayList<String>) document.get("categories");
                            Spinner spinner = (Spinner) findViewById(R.id.category);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddProductActivity.this,
                                    android.R.layout.simple_spinner_item,group);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(adapter);

                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    selectedCategory = parent.getItemAtPosition(position).toString();
                                    Toast.makeText(AddProductActivity.this,selectedCategory+"",Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                        }
                    }
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),122);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=nameEditText.getText().toString();
                if(!priceEditText.getText().toString().equals("")&&!quantityEditText.getText().toString().equals("")) {
                    price = Integer.parseInt(priceEditText.getText().toString());
                    quantity = Integer.parseInt(quantityEditText.getText().toString());
                }
                Log.d("AddProductActivity.this","category: "+selectedCategory+"  name : "+name+" price : "+price+"  quantity : "+quantity);
                Log.d("AddProductActivity.this","latitude : "+latitude+" , longitude : "+longitude + "image : "+ref);

                Product product=new Product(selectedCategory,name,price,quantity,latitude,longitude,ref.toString());
                if(latitude==0.0 && longitude==0.0){
                    Toast.makeText(AddProductActivity.this,"Please wait, we are finding your location...",Toast.LENGTH_SHORT).show();
                }else if(!name.equals("")&&price>0&&quantity>0){
                    db.collection("data")
                            .document(currentUser.getUid())
                            .collection("products")
                            .add(product)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                    Toast.makeText(AddProductActivity.this,"Product added successfully..",Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(AddProductActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });

                }else{
                    Toast.makeText(AddProductActivity.this,"Please fill all the details",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            String filePath;
                    if (resultCode == Activity.RESULT_OK && requestCode==122) {
                        //data gives you the image uri. Try to convert that to bitmap
                        if(data != null) {
                            selectedImageUri = data.getData();
                            try {
                                InputStream ims = getContentResolver().openInputStream(selectedImageUri);

                                // just display image in imageview
                                imageView.setImageBitmap(BitmapFactory.decodeStream(ims));

                                FirebaseStorage storage=FirebaseStorage.getInstance();
                                // Defining the child of storageReference
                                StorageReference imageRef=storage.getReference();
                                ref = imageRef.child("images/" + UUID.randomUUID().toString());

                                // adding listeners on upload
                                // or failure of image
                                ref.putFile(selectedImageUri)
                                        .addOnSuccessListener(
                                                new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        Toast.makeText(AddProductActivity.this,"Image Uploaded!!",Toast.LENGTH_SHORT).show();
                                                    }
                                                })

                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e)
                                            {
                                                Toast.makeText(AddProductActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (resultCode == Activity.RESULT_CANCELED) {
                        Log.e(TAG, "Selecting picture cancelled");
                    }
            }
        catch(Exception e) {
            Log.e(TAG, "Exception in onActivityResult : " + e.getMessage());
        }
    }

    private void getLocation(){

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                Toast.makeText(this, "The permission to get BLE location data is required", Toast.LENGTH_SHORT).show();
            }else{
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }else{
            Toast.makeText(this, "Location permissions granted", Toast.LENGTH_SHORT).show();

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, this);
        }

        Toast.makeText(AddProductActivity.this,"Please turn on GPS/Location",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
        Toast.makeText(AddProductActivity.this,"Please turn on GPS/Location",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("latlng",location.getLatitude()+","+location.getLongitude());
        locationDetails.setText(location.getLatitude()+","+location.getLongitude());

        latitude=location.getLatitude();
        longitude=location.getLongitude();
        // for address from latitude and longitude using Google geocoding API

        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET,"https://maps.googleapis.com/maps/api/geocode/json?latlng="+location.getLatitude()+","+
                location.getLongitude()+"&key=AIzaSyBMAzf84Uro1HFzW4He6jgoHuoEg2CObnQ",null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("address response",response.toString());
                    String address = response.getJSONArray("results").getJSONObject(0).getString("formatted_address");
                    Log.d("location Address",address);
                    locationDetails.setText(address);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error",error.toString());
            }
        });
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }
}
