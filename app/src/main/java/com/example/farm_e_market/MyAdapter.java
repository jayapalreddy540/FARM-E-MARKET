package com.example.farm_e_market;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<Commodity> itemList;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, subtitle;
        public ImageView icon;
        private ConstraintLayout main;
        public MyViewHolder(final View parent) {
            super(parent);
            title = (TextView) parent.findViewById(R.id.title);
            subtitle = (TextView) parent.findViewById(R.id.subtitle);
            icon = (ImageView) parent.findViewById(R.id.icon);
            main = (ConstraintLayout) parent.findViewById(R.id.main);
            main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), "Position:" + Integer.toString(getPosition()), Toast.LENGTH_SHORT).show();

                    Commodity commodity=itemList.get(getPosition());
                Intent intent =new Intent(itemView.getContext(),DetailProductActivity.class);
                intent.putExtra("image",commodity.getImage());
                Log.d("image",commodity.getImage());
                itemView.getContext().startActivity(intent);
                }
            });
        }
    }
    public MyAdapter(List<Commodity>itemList){
        this.itemList=itemList;
    }
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Commodity row=itemList.get(position);
        Log.d("commodity : ",row.getName()+" "+row.getPrice()+" "+row.getImage());
        String name=row.getName();
        int price=row.getPrice();
        String image=row.getImage();
        holder.title.setText(name);
        holder.subtitle.setText("Rs. "+String.valueOf(price)+"/-");
        holder.icon.setImageResource(R.drawable.logo);

       /* FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference gsReference = storage.getReferenceFromUrl(image);
        // Load the image using Glide
        Glide.with(this)
                .load(gsReference)
                .into(holder.icon );
        */
    }
    @Override
    public int getItemCount() {
        return itemList.size();
    }
}