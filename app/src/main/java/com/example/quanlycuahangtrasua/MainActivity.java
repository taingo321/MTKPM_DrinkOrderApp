package com.example.quanlycuahangtrasua;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.quanlycuahangtrasua.DesignPattern.Composite.InvoiceStaffActivity;

public class MainActivity extends AppCompatActivity {
    ImageView imageViewProduct, imageViewCart, imageViewLogout, imageInvoiceStaff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageViewProduct = findViewById(R.id.ivProduct);
        imageViewCart = findViewById(R.id.ivCart);
        imageViewLogout = findViewById(R.id.ivLogout);
        imageInvoiceStaff = findViewById(R.id.ivInvoiceStaff);

        imageViewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProductActivity.class);
                startActivity(intent);
            }
        });
        imageViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });
        imageViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        imageInvoiceStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InvoiceStaffActivity.class);
                startActivity(intent);
            }
        });
    }
}
