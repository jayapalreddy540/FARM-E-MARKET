package com.example.farm_e_market;

public class Product {
    String category;
    String name;
    int price;
    int quantity;
    double latitude;
    double longitude;
    String image;

    public Product(String category, String name,int price, int quantity, double latitude, double longitude, String image) {
        this.category = category;
        this.name=name;
        this.price = price;
        this.quantity = quantity;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image_url) {
        this.image = image;
    }
}
