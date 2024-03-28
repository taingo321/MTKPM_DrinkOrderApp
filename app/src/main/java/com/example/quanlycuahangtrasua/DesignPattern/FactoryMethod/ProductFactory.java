package com.example.quanlycuahangtrasua.DesignPattern.FactoryMethod;

public abstract class ProductFactory {
    public abstract Product createProduct(String productId, String productName, String ingredient, String price, String image);
}
