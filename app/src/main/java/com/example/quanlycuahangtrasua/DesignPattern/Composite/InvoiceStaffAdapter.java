package com.example.quanlycuahangtrasua.DesignPattern.Composite;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlycuahangtrasua.AdminOrderProductsDetailActivity;
import com.example.quanlycuahangtrasua.R;
import com.example.quanlycuahangtrasua.Model.Orders;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;

import java.util.Objects;

public class InvoiceStaffAdapter extends FirebaseRecyclerAdapter<Orders, InvoiceStaffAdapter.OrdersViewHolder> {

    public InvoiceStaffAdapter(@NonNull FirebaseRecyclerOptions<Orders> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull InvoiceStaffAdapter.OrdersViewHolder holder, int position, @NonNull Orders model) {
        String orderId = getRef(position).getKey();
        String date = model.getDate();
        String time = model.getTime();
        DataSnapshot orderSnapshot = model.getDataSnapshot();
        int totalAmount = calculateTotalAmount(orderSnapshot);
        holder.order_key.setText(orderId);
        holder.order_total_price.setText(String.valueOf(totalAmount) + "đ");
        holder.order_date_time.setText(date + " " + time);
        Context context = holder.itemView.getContext();
        holder.show_order_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AdminOrderProductsDetailActivity.class);
                intent.putExtra("uid", orderId); // Chuyển ID của đơn hàng
                context.startActivity(intent);
            }
        });


    }
    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout, parent, false);
        return new OrdersViewHolder(view);
    }

    public static class OrdersViewHolder extends RecyclerView.ViewHolder {
        public TextView order_key, order_total_price, order_date_time;
        public Button show_order_products;
        public OrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            order_key = itemView.findViewById(R.id.order_key);
            order_total_price = itemView.findViewById(R.id.order_total_price);
            order_date_time = itemView.findViewById(R.id.order_date_time);
            show_order_products = itemView.findViewById(R.id.show_order_products);
        }
    }
    private int calculateTotalAmount(DataSnapshot orderSnapshot) {
        int totalAmount = 0;
        if (orderSnapshot != null && orderSnapshot.child("products").exists()) {
            DataSnapshot productsSnapshot = orderSnapshot.child("products");
            for (DataSnapshot productSnapshot : productsSnapshot.getChildren()) {
                int price = Integer.parseInt(Objects.requireNonNull(productSnapshot.child("price").getValue(String.class)));
                int quantity = Integer.parseInt(Objects.requireNonNull(productSnapshot.child("quantity").getValue(String.class)));
                totalAmount += price * quantity;
            }
        }
        return totalAmount;
    }

}