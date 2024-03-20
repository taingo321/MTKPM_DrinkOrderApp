package com.example.quanlycuahangtrasua;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlycuahangtrasua.Model.Cart;
import com.example.quanlycuahangtrasua.Model.Orders;
import com.example.quanlycuahangtrasua.Model.Products;
import com.example.quanlycuahangtrasua.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminOrderProductsDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerViewProductsList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartListRef,orderListRef;
    private String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_products_detail);

        userId = getIntent().getStringExtra("uid");
        recyclerViewProductsList = findViewById(R.id.products_list);
        layoutManager = new LinearLayoutManager(this);
        recyclerViewProductsList.setLayoutManager(layoutManager);
        orderListRef = FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(userId);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("userId", userId);
        FirebaseRecyclerOptions<Orders> options = new FirebaseRecyclerOptions.Builder<Orders>()
                .setQuery(orderListRef, Orders.class)
                .build();
        FirebaseRecyclerAdapter<Orders, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Orders, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Orders model) {
                String orderId = getRef(position).getKey();
                Log.d("orderId", orderId);
                holder.cartOrderDayOrName.setText(model.getDate());
//                holder.product_Quantity_Cart.setText("x" + model.getQuantity());
                holder.cartOrderTimeOrQuality.setText(model.getTime());

                holder.orderTotalPrice.setText(model.getTotalAmount() + "đ");

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("bundleUsername", userId);
                        Log.d("bundleOrderId", orderId);
                        Intent intent = new Intent(AdminOrderProductsDetailActivity.this, AdminOrderDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("username", userId);
                        bundle.putString("orderId", orderId);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }


            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        recyclerViewProductsList.setAdapter(adapter);
        adapter.startListening();
    }
}

