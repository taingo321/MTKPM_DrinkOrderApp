package com.example.quanlycuahangtrasua;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlycuahangtrasua.DesignPattern.Repository.FruitTeaIProductRepository;
import com.example.quanlycuahangtrasua.DesignPattern.Repository.MilkTeaIProductRepository;
import com.example.quanlycuahangtrasua.DesignPattern.Repository.IProductRepository;
import com.example.quanlycuahangtrasua.Model.Products;
import com.example.quanlycuahangtrasua.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FruitTeaSeachActivity extends AppCompatActivity {

    private EditText edtProductName;
    private Button btnSearch;
    private RecyclerView recyclerViewSearchList;
    private IProductRepository IProductRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruit_tea_seach);

        IProductRepository = new FruitTeaIProductRepository();

        edtProductName = findViewById(R.id.search_product_name);
        btnSearch = findViewById(R.id.fruitTea_search_btn);
        recyclerViewSearchList = findViewById(R.id.fruitTea_search_list);
        recyclerViewSearchList.setLayoutManager(new LinearLayoutManager(FruitTeaSeachActivity.this));

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchInput = edtProductName.getText().toString();
                System.out.println("SearchInput: " + searchInput);
                searchProducts(searchInput);
            }
        });
    }

    private void searchProducts(String searchInput) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions
                        .Builder<Products>()
                        .setQuery(snapshot.getRef(), Products.class)
                        .build();
                setupRecyclerView(options);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };

        IProductRepository.searchProducts(searchInput, valueEventListener);
    }

    private void setupRecyclerView(FirebaseRecyclerOptions<Products> options) {
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position
                            , @NonNull Products model) {
                        holder.productName.setText(model.getProductName());
                        holder.productPrice.setText(model.getPrice() + "đ");
                        Picasso.get().load(model.getImage()).into(holder.productImage);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(FruitTeaSeachActivity.this,
                                        ProductDetailActivity.class);
                                intent.putExtra("productId", model.getProductId());
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.product_layout, parent, false);
                        return new ProductViewHolder(view);
                    }
                };
        recyclerViewSearchList.setAdapter(adapter);
        adapter.startListening();
    }
}