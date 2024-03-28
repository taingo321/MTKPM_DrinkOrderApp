package com.example.quanlycuahangtrasua.Model;

import com.example.quanlycuahangtrasua.DesignPattern.Observer.IOrderObserver;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private String productId, productName, price, quantity, status;

    public Cart() {
    }

    public Cart(String productId, String productName, String price, String quantity, String status) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.status = status;

    }


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
    private List<IOrderObserver> observers = new ArrayList<>();


    public void addObserver(IOrderObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(IOrderObserver observer) {
        observers.remove(observer);
    }

    public void setStatus(String status) {
        this.status = status;
        notifyObservers();
    }
    public String getStatus(){
        return status;
    }
    private void notifyObservers() {
        for (IOrderObserver observer : observers) {
            observer.update(status);
        }
    }

}
