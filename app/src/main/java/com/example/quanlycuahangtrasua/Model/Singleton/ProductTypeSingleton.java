package com.example.quanlycuahangtrasua.Model.Singleton;

public class ProductTypeSingleton {
    private static ProductTypeSingleton instance;
    private String productType;
    private ProductTypeSingleton(){

    }
    public static synchronized ProductTypeSingleton getInstance(){
        if(instance == null){
            instance = new ProductTypeSingleton();
        }
        return instance;
    }
    public String getProductType(){
        return productType;
    }
    public void setProductType(String productType) {
        this.productType = productType;
    }
}
