package com.example.quanlycuahangtrasua.DesignPattern.FactoryMethod;

public class MilkTeaFactory extends ProductFactory{
    @Override
    public IProduct createProduct(String productId, String productName, String ingredient, String price, String image) {
        return new MilkTea(productId,productName,ingredient,price,image);
    }
}
