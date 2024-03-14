package com.example.quanlycuahangtrasua.Model.FactoryMethod;

import com.example.quanlycuahangtrasua.Model.Products;

public class MilkTea extends Products implements IProduct{
    public MilkTea(String productId, String productName, String ingredient, String price,String image){
        super(productId, productName, ingredient, price, image);
    }

    @Override
    public void prepare() {

    }
}
