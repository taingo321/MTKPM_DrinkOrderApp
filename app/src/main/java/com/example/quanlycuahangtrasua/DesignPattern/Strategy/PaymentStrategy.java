package com.example.quanlycuahangtrasua.DesignPattern.Strategy;

import android.os.Bundle;

public interface PaymentStrategy {

    void pay();

    String getPaymentMethod();
}
