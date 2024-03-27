package com.example.quanlycuahangtrasua;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlycuahangtrasua.Model.Products;
import com.example.quanlycuahangtrasua.DesignPattern.Singleton.ProductTypeSingleton;
import com.example.quanlycuahangtrasua.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.travijuu.numberpicker.library.NumberPicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView imageViewProductDetail;
    private TextView txtProductNameDetail, txtProductIngredientDetail, txtProductPriceDetail;
    private Button btnAddProductToCart;
    private NumberPicker numberPicker;
    private String productId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        productId = getIntent().getStringExtra("productId");

        imageViewProductDetail = findViewById(R.id.product_Image_Detail);
        txtProductNameDetail = findViewById(R.id.product_Name_Detail);
        txtProductIngredientDetail = findViewById(R.id.product_Ingre_Detail);
        txtProductPriceDetail = findViewById(R.id.product_Price_Detail);

        btnAddProductToCart = findViewById(R.id.fabAddProductToCart);
        numberPicker = findViewById(R.id.number_picker);

        btnAddProductToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabAddProductToCart();
            }
        });

        getProductDetails(productId);
    }

    private void fabAddProductToCart() {
        String quantity = String.valueOf(numberPicker.getValue());
        checkIfProductExistsInCart(productId, quantity);
    }

    private void checkIfProductExistsInCart(final String productID, final String quantity) {
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        cartListRef.child("Products").child(productID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer quantityInteger = snapshot.child("quantity").getValue(Integer.class);
                if (quantityInteger != null) {
                    int existingQuantity = quantityInteger.intValue();
                    String newQuantity = existingQuantity + quantity;
                    cartListRef.child("Products").child(productID).child("quantity").setValue(newQuantity);
                } else {
                    // Sản phẩm chưa tồn tại trong giỏ hàng, thêm mới
                    addProductToCart(productID, quantity);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi (nếu cần)
            }
        });
    }

    private void addProductToCart(String productID, String quantity) {
        String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy ");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("productId", productID);
        cartMap.put("productName", txtProductNameDetail.getText().toString());
        cartMap.put("price", txtProductPriceDetail.getText().toString());
        cartMap.put("ingredient", txtProductIngredientDetail.getText().toString());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", quantity);
        cartMap.put("status", "undone");

        cartListRef.child("User View")
                .child(Prevalent.currentOnlineUser.getUsername())
                .child("Products")
                .child(productID).updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        cartListRef.child("Admin View")
                                .child(Prevalent.currentOnlineUser.getUsername())
                                .child("Products")
                                .child(productID).updateChildren(cartMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(ProductDetailActivity.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(ProductDetailActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        }
                                    }
                                });

                    }
                });
    }

    private void getProductDetails(String productID) {
        String productType = ProductTypeSingleton.getInstance().getProductType();
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productType);

        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Products products = snapshot.getValue(Products.class);

                    txtProductNameDetail.setText(products.getProductName());
                    txtProductIngredientDetail.setText(products.getIngredient());
                    txtProductPriceDetail.setText(products.getPrice());
                    Picasso.get().load(products.getImage()).into(imageViewProductDetail);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi (nếu cần)
            }
        });
    }
}