package com.example.quanlycuahangtrasua.DesignPattern.FactoryMethod;

import com.example.quanlycuahangtrasua.Model.Products;

public class Coffee extends Products implements IProduct {
    public Coffee(String productId, String productName, String ingredient, String price,String image){
        super(productId, productName, ingredient, price, image);
    }

    @Override
    public void prepare() {

    }
}
