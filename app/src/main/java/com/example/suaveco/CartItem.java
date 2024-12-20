package com.example.suaveco;

public class CartItem {
    private String productName;
    private double price;
    private String size;
    private int quantity;
    private int imageResourceId;

    public CartItem(String productName, double price, String size, int quantity, int imageResourceId) {
        this.productName = productName;
        this.price = price;
        this.size = size;
        this.quantity = quantity;
        this.imageResourceId = imageResourceId;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public String getSize() {
        return size;
    }

    public int getQuantity() {
        return quantity;
    }
    public int getImageResourceId() { return imageResourceId; }
}
