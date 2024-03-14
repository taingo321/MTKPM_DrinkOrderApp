package com.example.quanlycuahangtrasua;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlycuahangtrasua.Model.Cart;
import com.example.quanlycuahangtrasua.Model.Orders;
import com.example.quanlycuahangtrasua.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminOrderDetailActivity extends AppCompatActivity {
    private String orderId,username,productName,price,quantity,productId;
    private RecyclerView recyclerViewProductsList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference orderRef;
    private int totalPrice = 0;
    private TextView textViewTotalPrice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_detail);

        textViewTotalPrice = findViewById(R.id.total_amount);
        recyclerViewProductsList = findViewById(R.id.products_list);
        layoutManager = new LinearLayoutManager(this);
        recyclerViewProductsList.setLayoutManager(layoutManager);
        orderId = getIntent().getStringExtra("orderId");
        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        orderRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                username = snapshot.getKey();
                if(username != null){
                    Log.d("username",username);
                    setupRecyclerView();
                }else {
                    Log.d("username","Username is null");
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void setupRecyclerView(){
        DatabaseReference orderProductsRef = orderRef.child(username).child(orderId).child("products");
        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(orderProductsRef, Cart.class)
                .build();
        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {

                holder.cartOrderDayOrName.setText(model.getProductName());
                holder.orderTotalPrice.setText(model.getQuantity()+"x"+model.getPrice() + "đ");
                int price = Integer.parseInt(model.getPrice());
                int quantity = Integer.parseInt(model.getQuantity());
                int productTotalPrice  = price * quantity;
                holder.cartOrderTimeOrQuality.setText(productTotalPrice + "đ");
                totalPrice += productTotalPrice;
                textViewTotalPrice.setText("Tổng tiền: " + String.valueOf(totalPrice) + "đ");
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_layout, parent, false);
                return new CartViewHolder(view);
            }
        };
        recyclerViewProductsList.setAdapter(adapter);
        adapter.startListening();

    }
}
