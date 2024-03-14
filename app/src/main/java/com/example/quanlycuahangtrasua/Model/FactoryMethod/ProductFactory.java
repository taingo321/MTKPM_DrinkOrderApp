package com.example.quanlycuahangtrasua.Model.FactoryMethod;

public abstract class ProductFactory {
    public abstract IProduct createProduct(String productId, String productName, String ingredient, String price, String image);
}
