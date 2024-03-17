package com.example.quanlycuahangtrasua.Model;

public class AdminOrder {
    private String note, date, time, totalAmount, oid;

    public AdminOrder() {
    }


    public AdminOrder(String note, String date, String time, String totalAmount, String oid) {
        this.note = note;
        this.date = date;
        this.time = time;
        this.totalAmount = totalAmount;
        this.oid = oid;
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

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }
}


