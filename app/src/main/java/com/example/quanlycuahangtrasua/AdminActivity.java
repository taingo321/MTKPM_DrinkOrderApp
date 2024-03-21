package com.example.quanlycuahangtrasua;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AdminActivity extends AppCompatActivity {
    ImageView imageViewAddProduct, imageViewMaintain, imageViewInvoice, imageViewLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        imageViewAddProduct = findViewById(R.id.ivAddProduct);
        imageViewMaintain = findViewById(R.id.ivMaintain);
        imageViewLogout = findViewById(R.id.ivLogout);
        imageViewInvoice = findViewById(R.id.ivInvoice);
        imageViewAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AddNewProductActivity.class);
                startActivity(intent);
            }
        });
        imageViewMaintain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, ProductActivity.class);
                intent.putExtra("Admin", "Admin");
                startActivity(intent);
            }
        });
        imageViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        imageViewInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AdminOrderActivity.class);
                startActivity(intent);
            }
        });
    }
}