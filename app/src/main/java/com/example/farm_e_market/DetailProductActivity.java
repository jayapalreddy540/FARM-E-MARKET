package com.example.farm_e_market;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;

public class DetailProductActivity extends AppCompatActivity {

    private TextView nameText,priceText,quantityText,timeText;
    private Button directionBtn,buyBtn;
    private ImageView imageView;
    private double longitude=0.0;
    private double latitude=0.0;
    private String imageUrl;
    private boolean isImageFitToScreen;
    private String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);
        nameText=(TextView)findViewById(R.id.name);
        priceText=(TextView)findViewById(R.id.price);
        quantityText=(TextView)findViewById(R.id.quantity);
        timeText=(TextView)findViewById(R.id.time);
        directionBtn=(Button)findViewById(R.id.direction);
        buyBtn=(Button)findViewById(R.id.buy);
        imageView=(ImageView)findViewById(R.id.image);


        final FirebaseStorage storage = FirebaseStorage.getInstance();

        Intent intent=getIntent();
        if(intent.hasExtra("image")) {
            String image = intent.getStringExtra("image");
            Log.d("DetailProductActivity", image);

            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(true)
                    .build();
            db.setFirestoreSettings(settings);

            db.collectionGroup("products")
                    .whereEqualTo("image", image)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("DetailProductActivity", document.getId() + " => " + document.getData());
                                    nameText.setText(document.get("name").toString());
                                    priceText.setText("Rs."+document.get("price")+"/-");
                                    quantityText.setText("Quantity : "+document.get("quantity"));
                                    Timestamp timestamp=(Timestamp)document.get("timestamp");
                                    Date date=timestamp.toDate();
                                    timeText.setText("Uploaded At : "+date);
                                    //imageView.setImageURI((Uri) document.get("image"));
                                    longitude=document.getDouble("longitude");
                                    latitude=document.getDouble("latitude");
                                    imageUrl=document.get("image").toString();
                                    mobile=document.get("mobile").toString();
                                    StorageReference gsReference = storage.getReferenceFromUrl(imageUrl);
                                    // Load the image using Glide
                                    Glide.with( DetailProductActivity.this)
                                            .load(gsReference)
                                            .into(imageView );

                                }
                            } else {
                                Log.d("DetailProductActivity", "Error getting documents: ", task.getException());
                            }
                        }
                    });


            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isImageFitToScreen) {
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }else{
                        isImageFitToScreen=true;
                        imageView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    }
                }
            });
            directionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude);
                    //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    //startActivity(intent);

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:"+latitude+","+longitude+"?q="+latitude+","+longitude+"(Farmer's Location)"));
                    startActivity(intent);
                }
            });
            buyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(DetailProductActivity.this,BuyActivity.class);
                    intent.putExtra("mobile",mobile);
                    startActivity(intent);
                }
            });
        }
    }
}
