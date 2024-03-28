package com.example.quanlycuahangtrasua;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlycuahangtrasua.DesignPattern.Singleton.ProductTypeSingleton;

public class SearchMenuActivity extends AppCompatActivity {
    ImageView imageViewMilktea, imageViewCoffee, imageViewFruitTea;
    private String productType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_menu);

        imageViewMilktea = findViewById(R.id.ivMilktea);
        imageViewCoffee = findViewById(R.id.ivCoffee);
        imageViewFruitTea = findViewById(R.id.ivFruitTea);

        imageViewMilktea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductTypeSingleton.getInstance().setProductType("Milk Tea");
                productType = ProductTypeSingleton.getInstance().getProductType();
                Intent intent = new Intent(SearchMenuActivity.this, MilkTeaSearchActivity.class);
                intent.putExtra("productType",productType);
                startActivity(intent);
            }
        });
        imageViewCoffee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductTypeSingleton.getInstance().setProductType("Coffee");
                productType = ProductTypeSingleton.getInstance().getProductType();
                Intent intent = new Intent(SearchMenuActivity.this, CoffeeSearchActivity.class);
                intent.putExtra("productType",productType);
                startActivity(intent);
            }
        });
        imageViewFruitTea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductTypeSingleton.getInstance().setProductType("Fruit Tea");
                productType = ProductTypeSingleton.getInstance().getProductType();
                Intent intent = new Intent(SearchMenuActivity.this, FruitTeaSeachActivity.class);
                intent.putExtra("productType",productType);
                startActivity(intent);
            }
        });
    }
}