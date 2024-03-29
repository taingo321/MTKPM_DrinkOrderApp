package com.example.quanlycuahangtrasua;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlycuahangtrasua.DesignPattern.Strategy.CashPaymentStrategy;
import com.example.quanlycuahangtrasua.DesignPattern.Strategy.CreditCardPaymentStrategy;
import com.example.quanlycuahangtrasua.DesignPattern.Strategy.PaymentStrategy;
import com.example.quanlycuahangtrasua.Model.Cart;
import com.example.quanlycuahangtrasua.Prevalent.Prevalent;
import com.example.quanlycuahangtrasua.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerViewCartList;
    private RecyclerView.LayoutManager layoutManager;
    private Button buttonNext;
    private TextView textViewTotalAmount;
    private int overTotalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerViewCartList = findViewById(R.id.cart_list);
        recyclerViewCartList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerViewCartList.setLayoutManager(layoutManager);
        textViewTotalAmount = findViewById(R.id.total_price);
        buttonNext = findViewById(R.id.next_button);



        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewTotalAmount.setText("Tổng tiền = " + String.valueOf(overTotalPrice) + "đ");

                Intent intent = new Intent(CartActivity.this, ConfirmOrderActivity.class);
                intent.putExtra("Tổng tiền", String.valueOf(overTotalPrice));
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef.child("User View").child(Prevalent.currentOnlineUser.getUsername()).child("Products"), Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
                holder.cartOrderDayOrName.setText(model.getProductName());
                holder.cartOrderTimeOrQuality.setText("x" + model.getQuantity());
                holder.orderTotalPrice.setText(model.getPrice() + "đ");
                holder.status.setText(model.getStatus());
                int oneTypeProductPrice = ((Integer.valueOf(model.getPrice()))) * Integer.valueOf(model.getQuantity());
                overTotalPrice = overTotalPrice + oneTypeProductPrice;

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence options[] = new CharSequence[]{
                                "Chỉnh sửa",
                                "Xóa"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Tùy chỉnh giỏ hàng:");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0){
                                    Intent intent = new Intent(CartActivity.this, ProductDetailActivity.class);
                                    intent.putExtra("productId", model.getProductId());
                                    startActivity(intent);
                                }
                                if (i == 1){
                                    cartListRef.child("User View")
                                            .child(Prevalent.currentOnlineUser.getUsername())
                                            .child("Products")
                                            .child(model.getProductId())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(CartActivity.this, "Đã xóa khỏi giỏ hàng", Toast.LENGTH_SHORT).show();

                                                        Intent intent = new Intent(CartActivity.this, MainActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                }
                            }
                        });
                        builder.show();
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

        recyclerViewCartList.setAdapter(adapter);
        adapter.startListening();
    }
}
