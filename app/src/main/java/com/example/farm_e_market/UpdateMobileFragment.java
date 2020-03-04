package com.example.farm_e_market;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class UpdateMobileFragment extends Fragment {
    EditText mobileEditText;
    Button update;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.update_mobile_fragment, container, false);

        mobileEditText=(EditText)root.findViewById(R.id.mobile);
        update=(Button)root.findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(mobileEditText.getText().toString().length()!=10){
                    Snackbar snackbar = Snackbar
                            .make(v, "Please Enter a Valid Mobile Number", Snackbar.LENGTH_LONG);
                    snackbar.setTextColor(Color.WHITE);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.show();
                }
                else{
                    final String mobile=mobileEditText.getText().toString();
                    FirebaseAuth mAuth=FirebaseAuth.getInstance();
                    final FirebaseUser currentUser=mAuth.getCurrentUser();
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

                                            db.collection("data")
                                                    .document(currentUser.getUid())
                                                    .collection("products")
                                                    .document(document.getId())
                                                    .update("mobile",Long.parseLong(mobile))
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(getActivity(),"mobile number updated",Toast.LENGTH_SHORT).show();

                                                            final FragmentTransaction ft = getFragmentManager().beginTransaction();
                                                            ft.replace(R.id.nav_host_fragment, new SettingsFragment(), "NewFragmentTag");
                                                            ft.addToBackStack(null);
                                                            ft.commit();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getContext(),"Updation failed",Toast.LENGTH_SHORT).show();
                                                    Log.d("UpdateMobileFragment",e.getMessage());
                                                }
                                            });
                                        }
                                    } else {
                                        //Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });

                }
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
