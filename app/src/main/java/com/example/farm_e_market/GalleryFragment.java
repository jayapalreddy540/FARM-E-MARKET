package com.example.farm_e_market;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farm_e_market.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    private FirebaseAuth mAuth;

    int position=0;
    String category=null;

    ArrayList<String> Categories;

    private List<Commodity> commodities=new ArrayList<>();
    private RecyclerView recyclerview;
    private MyAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        //final TextView textView = root.findViewById(R.id.text_gallery);

        recyclerview=(RecyclerView)root.findViewById(R.id.recycler_view);
        mAdapter = new MyAdapter(commodities);
        RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(getContext());
        recyclerview.setLayoutManager(mLayoutManger);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(mAdapter);

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
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            Categories = (ArrayList<String>) document.get("categories");
                            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                            int position = sharedPref.getInt("position", 0);
                            // textView.setText(Categories.get(position));
                            category = Categories.get(position);
                            Log.d("category : ", category);
                            getData(category);

                        }
                    }
                }
            }
        });

        return root;
}



    private void getData(String category) {

            Log.d("category:", category);
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(true)
                    .build();
            db.setFirestoreSettings(settings);
        Query query = FirebaseFirestore.getInstance()
                .collectionGroup("products")
                .whereEqualTo("category",category);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d("error",e.toString());
                    return;
                }

                // Convert query snapshot to a list of chats
                commodities = snapshot.toObjects(Commodity.class);
                Log.d("products",commodities.toString());
                mAdapter = new MyAdapter(commodities);
                mAdapter.notifyDataSetChanged();
                recyclerview.setAdapter(mAdapter);
                // Update UI
                // ...
            }
        });
    }

}