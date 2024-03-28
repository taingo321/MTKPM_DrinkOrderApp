package com.example.quanlycuahangtrasua.Model;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlycuahangtrasua.AdminOrderProductsDetailActivity;
import com.example.quanlycuahangtrasua.DesignPattern.Composite.Interface.IComposite;
import com.example.quanlycuahangtrasua.DesignPattern.Composite.InvoiceStaffAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Orders implements IComposite {
    private String paymentMethod;
    private String status;
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
                  List<Cart> products, String status, String paymentMethod) {
        this.oid = oid;
        this.totalAmount = totalAmount;
        this.note = note;
        this.date = date;
        this.time = time;
        this.products = products;
        this.status = status;
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public void setProducts(List<Cart> products) {
        this.products = products;
    }

    public DataSnapshot getDataSnapshot() {
        return dataSnapshot;
    }

    public void setDataSnapshot(DataSnapshot dataSnapshot) {
        this.dataSnapshot = dataSnapshot;
    }

    @Override
    public void display(RecyclerView.ViewHolder viewHolder, String uid) {
        InvoiceStaffAdapter.OrdersViewHolder holder = (InvoiceStaffAdapter.OrdersViewHolder) viewHolder;
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(uid);
        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    AdminOrder adminOrder = snapshot.getValue(AdminOrder.class);
                    if (adminOrder != null) {
                        holder.order_key.setText(uid);
                        holder.order_total_price.setText(adminOrder.getTotalAmount() + "đ");
                        holder.order_date_time.setText(adminOrder.getDate() + " " + adminOrder.getTime());
                        holder.show_order_products.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Context context = holder.itemView.getContext();
                                Intent intent = new Intent(context, AdminOrderProductsDetailActivity.class);
                                intent.putExtra("uid", uid);
                                context.startActivity(intent);
                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Orders", "onCancelled", error.toException());
            }
        });
    }


    public Orders() {
    }



}
