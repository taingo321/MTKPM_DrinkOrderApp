package com.example.quanlycuahangtrasua;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlycuahangtrasua.Model.AdminOrder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminOrderActivity extends AppCompatActivity {

    private RecyclerView orders_list;
    private DatabaseReference ordersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        orders_list = findViewById(R.id.orders_list);
        orders_list.setLayoutManager(new LinearLayoutManager(this));
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
                        holder.order_key.setText(model.getOid());
                        holder.order_total_price.setText(model.getTotalAmount());
                        holder.order_date_time.setText(model.getDate());

                        holder.show_order_products.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String uID = getRef(position).getKey();

                                Intent intent = new Intent(AdminOrderActivity.this, AdminOrderProductsDetailActivity.class);
                                intent.putExtra("uid", uID);
                                startActivity(intent);
                            }
                        });

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

                                            RemoveOrder(uID);
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
        orders_list.setAdapter(adapter);
        adapter.startListening();
    }

    private void RemoveOrder(String uID) {
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
