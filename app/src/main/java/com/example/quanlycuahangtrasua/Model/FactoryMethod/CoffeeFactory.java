package com.example.quanlycuahangtrasua.Model.FactoryMethod;

public class CoffeeFactory extends ProductFactory{
    @Override
    public IProduct createProduct(String productId, String productName, String ingredient, String price, String image) {
        return new Coffee(productId, productName, ingredient, price, image);
    }
}
