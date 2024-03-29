package com.example.quanlycuahangtrasua.DesignPattern.Command;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.quanlycuahangtrasua.ConfirmOrderActivity;
import com.example.quanlycuahangtrasua.MainActivity;
import com.example.quanlycuahangtrasua.Model.Cart;
import com.example.quanlycuahangtrasua.Model.Orders;
import com.example.quanlycuahangtrasua.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AddOrderCommand implements Command{
    private ConfirmOrderActivity confirmOrderActivity;
    public AddOrderCommand(ConfirmOrderActivity confirmOrderActivity) {
        this.confirmOrderActivity = confirmOrderActivity;
    }

    @Override
    public void execute() {
        confirmOrderActivity.confirmOrder();
    }
}
