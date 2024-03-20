package com.example.quanlycuahangtrasua.DesignPattern.Repository;

import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public interface IProductRepository {
    void searchProducts(String searchInput, ValueEventListener listener);
    Query createQuery(String searchInput);
}
