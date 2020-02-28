package com.example.farm_e_market;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsFragment extends Fragment {
    private TextView name,mobile,email;
    private CircleImageView imageView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        name=(TextView)root.findViewById(R.id.name);
        mobile=(TextView)root.findViewById(R.id.mobile);
        email=(TextView)root.findViewById(R.id.email);
        imageView=(CircleImageView) root.findViewById(R.id.image);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        name.setText("Name : "+currentUser.getDisplayName());
        email.setText("Email : "+currentUser.getEmail());
        mobile.setText("Mobile : "+currentUser.getPhoneNumber());
        imageView.setImageResource(R.drawable.farmer);

        return root;
    }
}