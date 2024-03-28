package com.example.quanlycuahangtrasua;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.quanlycuahangtrasua.DesignPattern.Observer.UserNotifier;
import com.example.quanlycuahangtrasua.DesignPattern.Strategy.CashPaymentStrategy;
import com.example.quanlycuahangtrasua.DesignPattern.Strategy.CreditCardPaymentStrategy;
import com.example.quanlycuahangtrasua.DesignPattern.Strategy.PaymentStrategy;
import com.example.quanlycuahangtrasua.Model.Cart;
import com.example.quanlycuahangtrasua.Model.Orders;
import com.example.quanlycuahangtrasua.Model.Products;
import com.example.quanlycuahangtrasua.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfirmOrderActivity extends AppCompatActivity {

    private EditText edtNoteInput;
    private String orderKey;
    private Button btnConfirm;
    private String totalAmount = "";
    private String paymentMethod = "";
    private String status = "";
    private DatabaseReference ordersRef;
    private UserNotifier userNotifier;
    private PaymentStrategy paymentStrategy;
    private ImageView imgView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        totalAmount = getIntent().getStringExtra("Tổng tiền");
        Toast.makeText(this, "Tổng tiền = " + totalAmount + "đ", Toast.LENGTH_SHORT).show();
        edtNoteInput = findViewById(R.id.note_input);
        btnConfirm = findViewById(R.id.confirm_button);
        userNotifier = new UserNotifier();
        RadioGroup radioGroup = findViewById(R.id.payment_radio_group);
        RadioButton cashRadioButton = findViewById(R.id.cash_payment_radio_button);
        RadioButton onlineRadioButton = findViewById(R.id.online_payment_radio_button);
        imgView = findViewById(R.id.QR);
        imgView.setVisibility(View.GONE);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == cashRadioButton.getId()) {
                    paymentStrategy = new CashPaymentStrategy();
                    paymentMethod = paymentStrategy.getPaymentMethod();
                    paymentStrategy.getPaymentMethod();
                    imgView.setVisibility(View.GONE);
                } else if (checkedId == onlineRadioButton.getId()) {
                    paymentStrategy = new CreditCardPaymentStrategy();
                    paymentMethod = paymentStrategy.getPaymentMethod();
                    paymentStrategy.getPaymentMethod();
                    imgView.setVisibility(View.VISIBLE);
                }
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfirmOrder();
            }
        });
    }

    private void ConfirmOrder() {
        final String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy ");
        saveCurrentDate = currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());
        orderKey = ordersRef.push().getKey();
        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders").child(Prevalent.currentOnlineUser.getUsername()).child(orderKey);

        HashMap<String, Object> ordersMap = new HashMap<>();
        ordersMap.put("oid", orderKey);
        ordersMap.put("totalAmount", totalAmount);
        ordersMap.put("note", edtNoteInput.getText().toString());
        ordersMap.put("date", saveCurrentDate);
        ordersMap.put("time", saveCurrentTime);
        ordersMap.put("paymentMethod", paymentMethod);

        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference()
                .child("Cart List").child("User View").child(Prevalent.currentOnlineUser.getUsername()).child("Products");
        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Cart> productList = new ArrayList<>();
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    String productId = productSnapshot.getKey();
                    String productName = productSnapshot.child("productName").getValue(String.class);
                    String price = productSnapshot.child("price").getValue(String.class);
//                    String ingredient = productSnapshot.child("ingredient").getValue(String.class);
                    String quantity = productSnapshot.child("quantity").getValue(String.class);
//                    String image = productSnapshot.child("image").getValue(String.class);
                    productList.add(new Cart(productId,productName,price,quantity,status));
                }
                Orders order = new Orders(orderKey, totalAmount, edtNoteInput.getText().toString(), saveCurrentDate, saveCurrentTime, productList, status, paymentMethod);
                ordersRef.setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Cart List")
                                    .child("User View")
                                    .child(Prevalent.currentOnlineUser.getUsername())
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(ConfirmOrderActivity.this, "Đặt món thành công", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(ConfirmOrderActivity.this, MainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();

                                                DatabaseReference userOrderRef = FirebaseDatabase.getInstance().getReference()
                                                        .child("Orders")
                                                        .child(Prevalent.currentOnlineUser.getUsername())
                                                        .child(orderKey)
                                                        .child("products");

                                                userOrderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                                                            productSnapshot.getRef().child("status").setValue("done");
                                                            userNotifier.update("done");
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                        // Xử lý lỗi (nếu cần)
                                                    }
                                                });
                                            }
                                        }
                                    });
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi (nếu cần)
            }
        });
    }



}