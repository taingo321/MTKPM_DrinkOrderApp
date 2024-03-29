package com.example.quanlycuahangtrasua;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.quanlycuahangtrasua.DesignPattern.Singleton.ProductTypeSingleton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainActivity extends AppCompatActivity {

    private Button buttonApllayChange, buttonDelete;
    private EditText editTextProductNameMaintain, editTextProductPriceMaintain, editTextProductIngredientMaintain;
    private ImageView imageViewProductImageMaintain;
    private String productId = "",typeProduct;
    private DatabaseReference productsRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_maintain);

        productId = getIntent().getStringExtra("productId");
        typeProduct = ProductTypeSingleton.getInstance().getProductType(); // Singleton
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(typeProduct).child(productId);

        buttonApllayChange = findViewById(R.id.apply_change_button);
        buttonDelete = findViewById(R.id.delete_button);

        editTextProductNameMaintain = findViewById(R.id.product_Name_maintain);
        editTextProductPriceMaintain = findViewById(R.id.product_Price_maintain);
        editTextProductIngredientMaintain = findViewById(R.id.product_Ingre_maintain);
        imageViewProductImageMaintain = findViewById(R.id.product_Image_maintain);

        displaySpecificProductInfo();

        buttonApllayChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyChanges();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence options[] = new CharSequence[]{
                        "Xác nhận",
                        "Hủy"
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminMaintainActivity.this);
                builder.setTitle("Xác nhận xóa sản phẩm?");

                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        if (i == 0){
                            deleteThisProduct();
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

    private void deleteThisProduct() {
        productsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(AdminMaintainActivity.this, AdminActivity.class);
                startActivity(intent);
                finish();

                Toast.makeText(AdminMaintainActivity.this, "Xóa sản phẩm thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void applyChanges() {
        String productName = editTextProductNameMaintain.getText().toString();
        String productPrice = editTextProductPriceMaintain.getText().toString();
        String productIngredient = editTextProductIngredientMaintain.getText().toString();

        if (productName.equals("")){
            Toast.makeText(this, "Vui lòng nhập tên sản phẩm", Toast.LENGTH_SHORT).show();
        }else if (productPrice.equals("")){
            Toast.makeText(this, "Vui lòng nhập giá sản phẩm", Toast.LENGTH_SHORT).show();
        }else if (productIngredient.equals("")){
            Toast.makeText(this, "Vui lòng nhập công thức sản phẩm", Toast.LENGTH_SHORT).show();
        }else {
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("productId", productId);
            productMap.put("ingredient", productIngredient);
            productMap.put("price", productPrice);
            productMap.put("productName", productName);

            productsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(AdminMaintainActivity.this, "Đã thay đổi thông tin sản phẩm thành công", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(AdminMaintainActivity.this, AdminActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }

    private void displaySpecificProductInfo() {
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String productName = snapshot.child("productName").getValue().toString();
                    String productPrice = snapshot.child("price").getValue().toString();
                    String productIngredient = snapshot.child("ingredient").getValue().toString();
                    String productImage = snapshot.child("image").getValue().toString();

                    editTextProductNameMaintain.setText(productName);
                    editTextProductIngredientMaintain.setText(productPrice);// Tại sao set 2 cái tên
                    editTextProductIngredientMaintain.setText(productIngredient);// note
                    Picasso.get().load(productImage).into(imageViewProductImageMaintain);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}