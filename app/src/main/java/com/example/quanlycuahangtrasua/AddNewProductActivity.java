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
    private String product_name, product_ingre, product_price;
    private Button add_new_product;
    private ImageView select_product_image;
    private EditText ProductName, ProductIngre, ProductPrice;
    private  static final int GalleryPick = 1;
    private Uri ImageUri;
    private String productKey, downloadImageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);

        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        add_new_product = findViewById(R.id.add_new_product);
        select_product_image = findViewById(R.id.select_product_image);
        ProductName = findViewById(R.id.product_name);
        ProductIngre = findViewById(R.id.product_ingre);
        ProductPrice = findViewById(R.id.product_price);

        select_product_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        add_new_product.setOnClickListener(new View.OnClickListener() {
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
        startActivityForResult(galleryIntent, GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null){
            ImageUri = data.getData();
            select_product_image.setImageURI(ImageUri);
        }
    }

    private void ValidateProductData(){
        product_ingre = ProductIngre.getText().toString();
        product_price = ProductPrice.getText().toString();
        product_name = ProductName.getText().toString();

        if (ImageUri == null){
            Toast.makeText(this, "Vui lòng chọn hình ảnh...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(product_name)) {
            Toast.makeText(this, "Vui lòng nhập tên sản phẩm...", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(product_ingre)) {
            Toast.makeText(this, "Vui lòng nhập công thức...", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(product_price)) {
            Toast.makeText(this, "Vui lòng nhập giá tiền sản phẩm...", Toast.LENGTH_SHORT).show();
        }else {
            StoreProductInformation();
        }
    }

    private void StoreProductInformation() {

        productKey = ProductsRef.push().getKey();

        StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() + productKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);

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
        productMap.put("productName", product_name);
        productMap.put("ingredient", product_ingre);
        productMap.put("image", downloadImageUrl);
        productMap.put("price", product_price);

        ProductsRef.child(productKey).updateChildren(productMap)
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
