package com.example.quanlycuahangtrasua.DesignPattern.FactoryMethod;

import com.example.quanlycuahangtrasua.Model.Products;

public class FruitTea extends Products implements IProduct {
    public FruitTea(String productId, String productName, String ingredient, String price,String image){
        super(productId, productName, ingredient, price, image);
    }

    @Override
    public void prepare() {

    }
}
