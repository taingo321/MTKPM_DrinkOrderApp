package com.example.quanlycuahangtrasua;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainActivity extends AppCompatActivity {

    private Button apply_change_button, delete_button;
    private EditText product_Name_maintain, product_Price_maintain, product_Ingre_maintain;
    private ImageView product_Image_maintain;
    private String productID = "";
    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_maintain);

        productID = getIntent().getStringExtra("pid");
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);

        apply_change_button = findViewById(R.id.apply_change_button);
        delete_button = findViewById(R.id.delete_button);

        product_Name_maintain = findViewById(R.id.product_Name_maintain);
        product_Price_maintain = findViewById(R.id.product_Price_maintain);
        product_Ingre_maintain = findViewById(R.id.product_Ingre_maintain);
        product_Image_maintain = findViewById(R.id.product_Image_maintain);

        displaySpecificProductInfo();

        apply_change_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyChanges();
            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence options[] = new CharSequence[]{
                        "Xác nhận",
                        "Hủy"
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminMaintainActivity.this);
                builder.setTitle("Xác nhận xóa sản phẩm?");

                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        if (i == 0){
                            deleteThisProduct();
                        }
                        else {
                            finish();
                        }
                    }
                });
                builder.show();
            }
        });
    }

    private void deleteThisProduct() {
        productsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(AdminMaintainActivity.this, AdminActivity.class);
                startActivity(intent);
                finish();

                Toast.makeText(AdminMaintainActivity.this, "Xóa sản phẩm thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void applyChanges() {
        String pName = product_Name_maintain.getText().toString();
        String pPrice = product_Price_maintain.getText().toString();
        String pIngre = product_Ingre_maintain.getText().toString();

        if (pName.equals("")){
            Toast.makeText(this, "Vui lòng nhập tên sản phẩm", Toast.LENGTH_SHORT).show();
        }else if (pPrice.equals("")){
            Toast.makeText(this, "Vui lòng nhập giá sản phẩm", Toast.LENGTH_SHORT).show();
        }else if (pIngre.equals("")){
            Toast.makeText(this, "Vui lòng nhập công thức sản phẩm", Toast.LENGTH_SHORT).show();
        }else {
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("pid", productID);
            productMap.put("ingre", pIngre);
            productMap.put("price", pPrice);
            productMap.put("pname", pName);

            productsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(AdminMaintainActivity.this, "Đã thay đổi thông tin sản phẩm thành công", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(AdminMaintainActivity.this, AdminActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }

    private void displaySpecificProductInfo() {
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String pName = snapshot.child("pname").getValue().toString();
                    String pPrice = snapshot.child("price").getValue().toString();
                    String pIngre = snapshot.child("ingre").getValue().toString();
                    String pImage = snapshot.child("image").getValue().toString();

                    product_Name_maintain.setText(pName);
                    product_Price_maintain.setText(pPrice);
                    product_Ingre_maintain.setText(pIngre);
                    Picasso.get().load(pImage).into(product_Image_maintain);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}