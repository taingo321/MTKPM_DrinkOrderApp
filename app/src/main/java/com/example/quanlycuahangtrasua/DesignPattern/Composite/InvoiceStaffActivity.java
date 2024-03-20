package com.example.quanlycuahangtrasua.DesignPattern.Composite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quanlycuahangtrasua.AdminOrderDetailActivity;
import com.example.quanlycuahangtrasua.AdminOrderProductsDetailActivity;
import com.example.quanlycuahangtrasua.Model.AdminOrder;
import com.example.quanlycuahangtrasua.Model.Orders;
import com.example.quanlycuahangtrasua.Model.Products;
import com.example.quanlycuahangtrasua.R;
import com.example.quanlycuahangtrasua.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class InvoiceStaffActivity extends AppCompatActivity {
    private RecyclerView recyclerViewOrdersList;
    private DatabaseReference ordersRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_staff);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        recyclerViewOrdersList = findViewById(R.id.orders_list_staff);
        recyclerViewOrdersList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Orders> options =
                new FirebaseRecyclerOptions.Builder<Orders>()
                        .setQuery(ordersRef, Orders.class)
                        .build();
        InvoiceStaffAdapter adapter = new InvoiceStaffAdapter(options);
        recyclerViewOrdersList.setAdapter(adapter);
        adapter.startListening();
        };
    }

