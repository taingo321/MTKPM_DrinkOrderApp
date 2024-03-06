package com.example.quanlycuahangtrasua;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AdminActivity extends AppCompatActivity {
    ImageView ivAddProduct, ivMaintain, ivInvoice, ivLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        ivAddProduct = findViewById(R.id.ivAddProduct);
        ivMaintain = findViewById(R.id.ivMaintain);
        ivLogout = findViewById(R.id.ivLogout);
        ivInvoice = findViewById(R.id.ivInvoice);
        ivAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AddNewProductActivity.class);
                startActivity(intent);
            }
        });
        ivMaintain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, ProductActivity.class);
                intent.putExtra("Admin", "Admin");
                startActivity(intent);
            }
        });
        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        ivInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AdminOrderActivity.class);
                startActivity(intent);
            }
        });
    }
}