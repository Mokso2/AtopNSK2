package com.example.atopnsk2;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class Order {
    private String orderId;
    private String userId;
    private List<Product> products;
    private double totalPrice;
    private String status;

    public Order() {
        // Обязательный пустой конструктор для Firebase
    }

    public Order(String orderId, String userId, List<Product> products, double totalPrice, String status) {
        this.orderId = orderId;
        this.userId = userId;
        this.products = products;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    // Геттеры и сеттеры
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public void moveToCompleted() {
        DatabaseReference completedOrdersRef = FirebaseDatabase.getInstance().getReference("completed_orders");
        completedOrdersRef.child(orderId).setValue(this);
    }
}