package com.example.farm_e_market;

import android.util.Log;

import com.google.firebase.Timestamp;

public class Commodity {
    private String category;
    private String image;
    private double latitude;
    private double longitude;

    private String name;
    private int price;
    private int quantity;
    private Timestamp timestamp;

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Commodity() {
    }

    public Commodity(String category, String image, double latitude, double longitude, String name, int price, int quantity) {
        this.category = category;
        this.image = image;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }



    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        Log.d("name :::",name);
        return name;
    }

    public void setName(String name) {
        Log.d("name",name);
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
