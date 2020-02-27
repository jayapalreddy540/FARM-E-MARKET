package com.example.farm_e_market;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import java.util.List;

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

                    Log.d("position", String.valueOf(getPosition()));
                    SharedPreferences sharedPref = MyViewHolder.this.getClass().getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("positionDetail", getPosition());
                    editor.apply();
                    Intent intent =new Intent(,DetailProductActivity.class);
                    startActivity(intent);

                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.nav_host_fragment, new GalleryFragment(), "NewFragmentTag");
                    ft.addToBackStack(null);
                    ft.commit();
                    Toast.makeText(itemView.getContext(), "Position:" + Integer.toString(getPosition()), Toast.LENGTH_SHORT).show();
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
        Picasso.get().load(image).placeholder(R.drawable.logo).into(holder.icon);

    }
    @Override
    public int getItemCount() {
        return itemList.size();
    }
}