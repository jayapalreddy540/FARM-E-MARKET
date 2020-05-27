package com.example.farm_e_market;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyProductsFragment extends Fragment {

    private FirebaseAuth mAuth;

    int position=0;
    String category=null;

    ArrayList<String> Categories;

    private List<Commodity> commodities=new ArrayList<>();
    private RecyclerView recyclerview;
    private MyAdapter mAdapter;
    private Button btn;

    private String selectedOption="timestamp";
    private String selectedOption1="ASCENDING";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
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
        final Spinner spinner = (Spinner)root.findViewById(R.id.sort);
        String[] sortOptions={"timestamp","name","price"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,sortOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final Spinner spinner1 = (Spinner)root.findViewById(R.id.sortAscDesc);
        String[] ascOptions={"ASCENDING","DESCENDING"};

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,ascOptions);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        mAuth = FirebaseAuth.getInstance();

        final FirebaseUser currentUser = mAuth.getCurrentUser();
        final String userId=currentUser.getUid();
        getData(userId);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedOption = parent.getItemAtPosition(position).toString();
                Toast.makeText(getContext(),"sort options applied",Toast.LENGTH_SHORT).show();
                getData(userId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedOption1 = parent.getItemAtPosition(position).toString();
                Toast.makeText(getContext(),"sort options applied",Toast.LENGTH_SHORT).show();
                getData(userId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return root;
    }



    private void getData(String id) {

        Log.d("id:", id);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        Query query;
        if (!selectedOption.equals("")) {
            if (selectedOption1.equals("ASCENDING")) {
                query = FirebaseFirestore.getInstance()
                        .collection("data")
                        .document(id)
                        .collection("products")
                        .orderBy(selectedOption, Query.Direction.ASCENDING);
            } else {
                query = FirebaseFirestore.getInstance()
                        .collection("data")
                        .document(id)
                        .collection("products")
                        .orderBy(selectedOption, Query.Direction.DESCENDING);
            }
            query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot snapshot,
                                    @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.d("error", e.toString());
                        return;
                    }

                    // Convert query snapshot to a list of chats
                    commodities = snapshot.toObjects(Commodity.class);
                    Log.d("products", commodities.toString());
                    mAdapter = new MyAdapter(commodities);
                    mAdapter.notifyDataSetChanged();
                    recyclerview.setAdapter(mAdapter);
                    // Update UI
                    // ...
                }
            });
        }
    }
}



