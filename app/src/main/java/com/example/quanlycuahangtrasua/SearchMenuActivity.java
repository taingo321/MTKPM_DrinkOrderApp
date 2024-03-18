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

public class SearchMenuActivity extends AppCompatActivity {
    ImageView ivMilktea, ivCoffee, ivFruitTea;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_menu);

        ivMilktea = findViewById(R.id.ivMilktea);
        ivCoffee = findViewById(R.id.ivCoffee);
        ivFruitTea = findViewById(R.id.ivFruitTea);

        ivMilktea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchMenuActivity.this, MilkTeaSearchActivity.class);
                startActivity(intent);
            }
        });
        ivCoffee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchMenuActivity.this, CoffeeSearchActivity.class);
                startActivity(intent);
            }
        });
        ivFruitTea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchMenuActivity.this, FruitTeaSeachActivity.class);
                startActivity(intent);
            }
        });
    }
}