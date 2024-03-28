package com.example.quanlycuahangtrasua.DesignPattern.Strategy;

import android.os.Bundle;

import com.example.quanlycuahangtrasua.Prevalent.Prevalent;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CashPaymentStrategy implements PaymentStrategy{


    @Override
    public void pay() {

    }

    @Override
    public String getPaymentMethod() {
        return "PayCash";
    }
}
