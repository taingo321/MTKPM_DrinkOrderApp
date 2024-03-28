package com.example.quanlycuahangtrasua;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlycuahangtrasua.DesignPattern.Command.Command;
import com.example.quanlycuahangtrasua.DesignPattern.Command.CommandInvoker;
import com.example.quanlycuahangtrasua.DesignPattern.Command.RemoveOrderCommand;
import com.example.quanlycuahangtrasua.Model.AdminOrder;
import com.example.quanlycuahangtrasua.Model.Orders;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class AdminOrderActivity extends AppCompatActivity {

    private RecyclerView recyclerViewOrdersList;
    private DatabaseReference ordersRef;
    private String totalAmount,date,time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        recyclerViewOrdersList = findViewById(R.id.orders_list);
        recyclerViewOrdersList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrder> options =
                new FirebaseRecyclerOptions.Builder<AdminOrder>()
                        .setQuery(ordersRef, AdminOrder.class)
                        .build();

        FirebaseRecyclerAdapter<AdminOrder, AdminOrdersViewHolder> adapter =
                new FirebaseRecyclerAdapter<AdminOrder, AdminOrdersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, int position, @NonNull AdminOrder model) {
                        String uid = getRef(position).getKey();
                        String username = uid;
                        int resultTotalAmount = 0;
                        DatabaseReference orderRef = ordersRef.child(username);
                        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot orderSnapshot: snapshot.getChildren()){
//                                    String orderId = orderSnapshot.getKey();

                                    AdminOrder adminOrder = orderSnapshot.getValue(AdminOrder.class);

                                    holder.order_key.setText(username);
                                    holder.order_total_price.setText(adminOrder.getTotalAmount() + "đ");
                                    holder.order_date_time.setText(adminOrder.getDate() + " " + adminOrder.getTime());

                                }
                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("AdminOrderActivity", "onCancelled", error.toException());
                            }
                        });
                        Log.d("uid",username);
                        holder.show_order_products.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(AdminOrderActivity.this, AdminOrderProductsDetailActivity.class);
                                intent.putExtra("uid", username);
                                startActivity(intent);
                            }
                        });
//                        holder.order_key.setText(username);
//                        holder.order_total_price.setText(String.valueOf(resultTotalAmount)+"đ");
//                        holder.order_date_time.setText(date+time);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CharSequence options[] = new CharSequence[]{
                                        "Xác nhận",
                                        "Hủy"
                                };
                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminOrderActivity.this);
                                builder.setTitle("Bạn có muốn xóa đơn hàng này?");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        if (i == 0){
                                            String uID = getRef(position).getKey();
                                            CommandInvoker invoker = new CommandInvoker();
                                            Command command = new RemoveOrderCommand(AdminOrderActivity.this, uID);
                                            invoker.setCommand(command);
                                            invoker.executeCommand();
                                        }
                                        else {
                                            finish();
                                        }
                                    }
                                });
                                builder.show();
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout, parent, false);
                        return new AdminOrdersViewHolder(view);
                    }
                };
        recyclerViewOrdersList.setAdapter(adapter);
        adapter.startListening();
    }

    public void removeOrder(String uID) {
        ordersRef.child(uID).removeValue();
    }

    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder{

        public TextView order_key, order_total_price, order_date_time;
        public Button show_order_products;

        public AdminOrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            order_key = itemView.findViewById(R.id.order_key);
            order_total_price = itemView.findViewById(R.id.order_total_price);
            order_date_time = itemView.findViewById(R.id.order_date_time);
            show_order_products = itemView.findViewById(R.id.show_order_products);
        }
    }
}
