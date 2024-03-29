package com.example.quanlycuahangtrasua;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.quanlycuahangtrasua.Model.Products;
import com.example.quanlycuahangtrasua.DesignPattern.Singleton.ProductTypeSingleton;
import com.example.quanlycuahangtrasua.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ProductActivity extends AppCompatActivity {
    private DatabaseReference productsManagementRef;
    private RecyclerView recyclerViewProductListManagement;
    RecyclerView.LayoutManager layoutManager;
    private String type = "",productType;
    private RadioGroup radioGroup;
    private RadioButton selectedRadioButton,defaultRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        radioGroup = findViewById(R.id.radioGroup);
        recyclerViewProductListManagement = findViewById(R.id.rvProductListManagement);
        recyclerViewProductListManagement.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerViewProductListManagement.setLayoutManager(layoutManager);

        defaultRadioButton = findViewById(R.id.radioCoffee);
        defaultRadioButton.setChecked(true);
        productType = defaultRadioButton.getText().toString();
        ProductTypeSingleton.getInstance().setProductType(productType);// Singleton
        setupRecyclerView(productType);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectedRadioButton = findViewById(checkedId);
                if(selectedRadioButton != null){
                    productType = selectedRadioButton.getText().toString();
                    ProductTypeSingleton.getInstance().setProductType(productType);// Singleton
                    setupRecyclerView(productType);
                }
            }
        });
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            type = getIntent().getExtras().get("Admin").toString();
        }

    }
    private void setupRecyclerView(String productType){
        productsManagementRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productType);
        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(productsManagementRef, Products.class).build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {
                String originalName = model.getProductName();
                if (originalName != null) {
                    int maxLength = 20;
                    if (originalName.length() > maxLength) {
                        String truncatedName = originalName.substring(0, maxLength - 3) + "...";
                        holder.productName.setText(truncatedName);
                    } else {
                        holder.productName.setText(originalName);
                    }
                }

                holder.productPrice.setText(model.getPrice() + "đ");
                holder.productIngre.setText(model.getIngredient());
                Picasso.get().load(model.getImage()).into(holder.productImage);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (type.equals("Admin")){
                            Intent intent = new Intent(ProductActivity.this, AdminMaintainActivity.class);
                            intent.putExtra("productId", model.getProductId());
                            startActivity(intent);
                        }
                        else {
                            Intent intent = new Intent(ProductActivity.this, ProductDetailActivity.class);
                            intent.putExtra("productId", model.getProductId());
                            startActivity(intent);
                        }
                    }
                });
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout, parent, false);
                return new ProductViewHolder(view);
            }
        };

        recyclerViewProductListManagement.setAdapter(adapter);
        adapter.startListening();
    }
    @Override
    protected void onStart() {
        super.onStart();
//        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
//                .setQuery(productsManagementRef, Products.class).build();
//
//        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {
//                String originalName = model.getProductName();
//////                int maxLength = 20;
////
////                if (originalName.length() > maxLength) {
////                    String truncatedName = originalName.substring(0, maxLength - 3) + "...";
////                    holder.productName.setText(truncatedName);
////                } else {
////                    holder.productName.setText(originalName);
////                }
//                if (originalName != null) {
//                    int maxLength = 20;
//                    if (originalName.length() > maxLength) {
//                        String truncatedName = originalName.substring(0, maxLength - 3) + "...";
//                        holder.productName.setText(truncatedName);
//                    } else {
//                        holder.productName.setText(originalName);
//                    }
//                }
//
//                holder.productPrice.setText(model.getPrice() + "đ");
//                holder.productIngre.setText(model.getIngredient());
//                Picasso.get().load(model.getImage()).into(holder.productImage);
//
//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (type.equals("Admin")){
//                            Intent intent = new Intent(ProductActivity.this, AdminMaintainActivity.class);
//                            intent.putExtra("productId", model.getProductId());
//                            startActivity(intent);
//                        }
//                        else {
//                            Intent intent = new Intent(ProductActivity.this, ProductDetailActivity.class);
//                            intent.putExtra("productId", model.getProductId());
//                            startActivity(intent);
//                        }
//                    }
//                });
//            }
//
//            @NonNull
//            @Override
//            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout, parent, false);
//                return new ProductViewHolder(view);
//            }
//        };
//
//        recyclerViewProductListManagement.setAdapter(adapter);
//        adapter.startListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.nav_search);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                Intent intent = new Intent(ProductActivity.this, SearchMenuActivity.class);
                startActivity(intent);
                return false;
            }
        });
        return true;
    }


}