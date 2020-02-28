package com.example.farm_e_market;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomAdapter extends BaseAdapter {
    Context context;
    int logos[];
    String category[];
    LayoutInflater inflter;
    public CustomAdapter(Context applicationContext, int[] logos,String[] category) {
        this.context = applicationContext;
        this.logos = logos;
        this.category=category;
        inflter = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return logos.length;
    }
    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_gridview, null); // inflate the layout
        CircleImageView icon = (CircleImageView) view.findViewById(R.id.icon); // get the reference of ImageView
        TextView name=(TextView)view.findViewById(R.id.name);
        icon.setImageResource(logos[i]); // set logo images
        name.setText(category[i]);
        return view;
    }
}