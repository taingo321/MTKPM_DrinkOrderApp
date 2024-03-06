package com.example.quanlycuahangtrasua;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class AddNewProductActivity extends AppCompatActivity {
    private String productName, productIngredient, productPrice;
    private Button btnAddNewProduct;
    private ImageView imageViewSelectProductImage;
    private EditText editTextProductName, editTextProductIngredient, editTextProductPrice;
    private  static final int GALLERY_PICK = 1;
    private Uri uri;
    private String productKey, downloadImageUrl;
    private StorageReference productImagesRef;
    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);

        productImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        btnAddNewProduct = findViewById(R.id.add_new_product);
        imageViewSelectProductImage = findViewById(R.id.select_product_image);
        editTextProductName = findViewById(R.id.product_name);
        editTextProductIngredient = findViewById(R.id.product_ingre);
        editTextProductPrice = findViewById(R.id.product_price);

        imageViewSelectProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        btnAddNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ValidateProductData();
            }
        });
    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK && data != null){
            uri = data.getData();
            imageViewSelectProductImage.setImageURI(uri);
        }
    }

    private void ValidateProductData(){
        productIngredient = editTextProductIngredient.getText().toString();
        productPrice = editTextProductPrice.getText().toString();
        productName = editTextProductName.getText().toString();

        if (uri == null){
            Toast.makeText(this, "Vui lòng chọn hình ảnh...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(productName)) {
            Toast.makeText(this, "Vui lòng nhập tên sản phẩm...", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(productIngredient)) {
            Toast.makeText(this, "Vui lòng nhập công thức...", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(productPrice)) {
            Toast.makeText(this, "Vui lòng nhập giá tiền sản phẩm...", Toast.LENGTH_SHORT).show();
        }else {
            StoreProductInformation();
        }
    }

    private void StoreProductInformation() {

        productKey = productsRef.push().getKey();

        StorageReference filePath = productImagesRef.child(uri.getLastPathSegment() + productKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(uri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AddNewProductActivity.this, "Lỗi" + message, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(AddNewProductActivity.this, "Tải hình ảnh sản phẩm thành công", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(AddNewProductActivity.this, "Thêm thành công hình ảnh sản phẩm", Toast.LENGTH_SHORT).show();

                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveProductInfoToDatabase() {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("productId", productKey);
        productMap.put("productName", productName);
        productMap.put("ingredient", productIngredient);
        productMap.put("image", downloadImageUrl);
        productMap.put("price", productPrice);

        productsRef.child(productKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(AddNewProductActivity.this, AdminActivity.class);
                            startActivity(intent);
                            Toast.makeText(AddNewProductActivity.this, "Thêm sản phẩm mới thành công", Toast.LENGTH_SHORT).show();
                        }else {
                            String message = task.getException().toString();
                            Toast.makeText(AddNewProductActivity.this, "Lỗi: them " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
