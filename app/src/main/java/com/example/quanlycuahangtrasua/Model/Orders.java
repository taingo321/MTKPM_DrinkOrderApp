package com.example.quanlycuahangtrasua.Model;


import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlycuahangtrasua.DesignPattern.Composite.Interface.IComposite;
import com.google.firebase.database.DataSnapshot;

import java.util.List;

public class Orders implements IComposite {

    private String status;
    private String oid;
    private String totalAmount;
    private String note;
    private String date;
    private String time;
    private List<Cart> products;
    private DataSnapshot dataSnapshot;
    public Orders(String productId, int quantity) {
    }

    public Orders(String oid, String totalAmount, String note, String date, String time,
                  List<Cart> products, String status) {
        this.oid = oid;
        this.totalAmount = totalAmount;
        this.note = note;
        this.date = date;
        this.time = time;
        this.products = products;
        this.status = status;
    }



    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<Cart> getProducts() {
        return products;
    }
    public DataSnapshot getDataSnapshot() {
        return dataSnapshot;
    }

    public void setDataSnapshot(DataSnapshot dataSnapshot) {
        this.dataSnapshot = dataSnapshot;
    }

    public void setProducts(List<Cart> products) {
        this.products = products;
    }

    @Override
    public void display() {
        Log.d("Orders", "Order ID: " + oid);
        Log.d("Orders", "Total Amount: " + totalAmount);
        Log.d("Orders", "Note: " + note);
        Log.d("Orders", "Date: " + date);
        Log.d("Orders", "Time: " + time);
        Log.d("Orders", "Status: " + status);
        // Hiển thị danh sách sản phẩm
        for (Cart product : products) {
            Log.d("Orders", "Product ID: " + product.getProductId());
            Log.d("Orders", "Product Name: " + product.getProductName());
            Log.d("Orders", "Quantity: " + product.getQuantity());

        }
    }



    public Orders() {
    }





}
