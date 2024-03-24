package com.example.quanlycuahangtrasua.DesignPattern.Composite;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlycuahangtrasua.Model.Orders;
import com.example.quanlycuahangtrasua.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

