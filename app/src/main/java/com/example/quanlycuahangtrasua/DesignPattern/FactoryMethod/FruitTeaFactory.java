package com.example.quanlycuahangtrasua.DesignPattern.FactoryMethod;

public class FruitTeaFactory extends ProductFactory{
    @Override
    public IProduct createProduct(String productId, String productName, String ingredient, String price, String image) {
        return new FruitTea(productId,productName,ingredient,price,image);
    }
}
