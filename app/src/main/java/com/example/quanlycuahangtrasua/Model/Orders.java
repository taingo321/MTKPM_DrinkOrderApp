package com.example.quanlycuahangtrasua.Model;

import com.example.quanlycuahangtrasua.DesignPattern.Composite.Interface.Composite;
import com.google.firebase.database.DataSnapshot;

import java.util.List;

public class Orders implements Composite {
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
                  List<Cart> products) {
        this.oid = oid;
        this.totalAmount = totalAmount;
        this.note = note;
        this.date = date;
        this.time = time;
        this.products = products;
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

    }
    public Orders() {
    }



}
