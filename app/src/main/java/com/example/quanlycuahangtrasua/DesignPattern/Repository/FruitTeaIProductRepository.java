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
        Query query = createQuery(searchInput);
        query.addListenerForSingleValueEvent(listener);
    }

    @Override
    public Query createQuery(String searchInput) {
        Query query = this.databaseReference
                .orderByChild("productName")
                .startAt(searchInput)
                .endAt(searchInput + "\uf8ff");
        return query;
    }
}
