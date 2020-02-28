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

public class HomeFragment extends Fragment {

    GridView simpleGrid;
    int logos[] = {R.drawable.logo, R.drawable.ic_launcher_invert, R.drawable.images, R.drawable.home,
            R.drawable.ic_launcher, R.drawable.images, R.drawable.logo, R.drawable.ic_launcher_invert};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        simpleGrid = (GridView)root.findViewById(R.id.gridview); // init GridView
        // Create an object of CustomAdapter and set Adapter to GirdView
        CustomAdapter customAdapter = new CustomAdapter(getContext(), logos);
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

        return root;
    }
}