package com.example.quanlycuahangtrasua.DesignPattern.Repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FruitTeaIProductRepository implements IProductRepository {
    private DatabaseReference databaseReference;

    public FruitTeaIProductRepository() {
        this.databaseReference = FirebaseDatabase.getInstance().getReference().child("Products")
                .child("Fruit Tea");
    }

    @Override
    public void searchProducts(String searchInput, ValueEventListener listener) {
        Query query = databaseReference
                .orderByChild("productName")
                .startAt(searchInput)
                .endAt(searchInput + "\uf8ff");
        query.addListenerForSingleValueEvent(listener);
    }
}
