package com.example.quanlycuahangtrasua;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlycuahangtrasua.DesignPattern.Repository.CoffeeIProductRepository;
import com.example.quanlycuahangtrasua.DesignPattern.Repository.IProductRepository;
import com.example.quanlycuahangtrasua.Model.Products;
import com.example.quanlycuahangtrasua.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CoffeeSearchActivity extends AppCompatActivity {

    private EditText edtProductName;
    private Button btnSearch;
    private RecyclerView recyclerViewSearchList;
    private IProductRepository IProductRepository;
    private String searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee_search);

        IProductRepository = new CoffeeIProductRepository();

        edtProductName = findViewById(R.id.search_product_name);
        btnSearch = findViewById(R.id.coffee_search_btn);
        recyclerViewSearchList = findViewById(R.id.coffee_search_list);
        recyclerViewSearchList.setLayoutManager(new LinearLayoutManager(CoffeeSearchActivity.this));

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchInput = edtProductName.getText().toString();
                System.out.println("SearchInput: " + searchInput);
                searchProducts();
            }
        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");
//        System.out.println("Query: " + reference.orderByChild("productName").startAt(searchInput).endAt(searchInput + "\uf8ff").toString());
//
//        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
//                .setQuery(reference.orderByChild("productName").startAt(searchInput).endAt(searchInput + "\uf8ff"), Products.class)
//                .build();
//
//        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
//                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
//                    @Override
//                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {
//                        holder.productName.setText(model.getProductName());
//                        holder.productPrice.setText("Price = " + model.getPrice() + "");
//                        Picasso.get().load(model.getImage()).into(holder.productImage);
//                        holder.itemView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Intent intent = new Intent(SearchProductActivity.this,ProductDetailActivity.class);
//                                intent.putExtra("productId", model.getProductId());
//                                startActivity(intent);
//                            }
//                        });
//                    }
//
//                    @NonNull
//                    @Override
//                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout, parent, false);
//                        ProductViewHolder holder = new ProductViewHolder(view);
//                        return holder;
//                    }
//                };
//        recyclerViewSearchList.setAdapter(adapter);
//        adapter.startListening();
//    }
    private void searchProducts() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Query query = IProductRepository.createQuery(searchInput);
                setupRecyclerView(query);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        IProductRepository.searchProducts(searchInput, valueEventListener);
    }

    private void setupRecyclerView(Query query) {
        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.
                Builder<Products>()
                .setQuery(query, Products.class)
                .build();
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position,
                                                    @NonNull Products model) {
                        holder.productName.setText(model.getProductName());
                        holder.productPrice.setText(model.getPrice() + "đ");
                        Picasso.get().load(model.getImage()).into(holder.productImage);
                        Log.d("getProductId: ",model.getProductId());
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(CoffeeSearchActivity.this,
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