package com.example.quanlycuahangtrasua.Model;

public class Products {
    private String productId, productName, ingredient, price, image ;

    public Products() {
    }

    public Products(String productId, String productName, String ingredient, String price,String image) {
        this.productId = productId;
        this.productName = productName;
        this.ingredient = ingredient;
        this.price = price;
        this.image = image;
    }
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

