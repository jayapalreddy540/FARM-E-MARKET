package com.example.farm_e_market;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    GridView simpleGrid;
    int logos[] = {R.drawable.agricultural_crops, R.drawable.organic_vegetables, R.drawable.seeds, R.drawable.dairy_products,
            R.drawable.flowers, R.drawable.fruits, R.drawable.plants, R.drawable.poultry};
    ArrayList<String> Categories;
    String[] category;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        DocumentReference docRef = db.collection("app").document("products");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            Categories = (ArrayList<String>) document.get("categories");
                            category = Categories.toArray(new String[Categories.size()]);

                            simpleGrid = (GridView)root.findViewById(R.id.gridview); // init GridView
                            // Create an object of CustomAdapter and set Adapter to GirdView
                            CustomAdapter customAdapter = new CustomAdapter(getContext(), logos,category);
                            simpleGrid.setAdapter(customAdapter);
                            // implement setOnItemClickListener event on GridView
                            simpleGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    // set an Intent to Another Activity

                                    //TODO send position to GalleryFragment
                                    Log.d("position", String.valueOf(position));
                                    SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putInt("position", position);
                                    editor.apply();

                                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                                    ft.replace(R.id.nav_host_fragment, new GalleryFragment(), "NewFragmentTag");
                                    ft.addToBackStack(null);
                                    ft.commit();

                                }
                            });

                        }
                    }
                }
            }
        });


        return root;
    }
}