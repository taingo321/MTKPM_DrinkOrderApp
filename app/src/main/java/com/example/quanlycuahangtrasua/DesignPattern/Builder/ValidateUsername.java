package com.example.quanlycuahangtrasua.DesignPattern.Builder;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.quanlycuahangtrasua.LoginActivity;
import com.example.quanlycuahangtrasua.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ValidateUsername {
    private String username;
    private String phone;
    private String password;
    private ProgressDialog progressBar;
    private Context context;

    public ValidateUsername(ValidateUsernameBuilder builder, Context context) {
        this.username = builder.getUsername();
        this.phone = builder.getPhone();
        this.password = builder.getPassword();
        this.progressBar = builder.getProgressBar();
        this.context = context;
    }

    public void validate() {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("Users").child(username).exists())){
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone", phone);
                    userdataMap.put("password", password);
                    userdataMap.put("username", username);

                    RootRef.child("Users").child(username).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(context, "Tài khoản của bạn đã tạo thành công", Toast.LENGTH_SHORT).show();
                                        progressBar.dismiss();

                                        Intent intent = new Intent(context, LoginActivity.class);
                                        context.startActivity(intent);
                                    }
                                    else {
                                        progressBar.dismiss();
                                        Toast.makeText(context, "Đã xảy ra lỗi,vui lòng thử lại", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else {
                    Toast.makeText(context, "Tên tài khoản " + username + " đã tồn tại.", Toast.LENGTH_SHORT).show();
                    progressBar.dismiss();
                    Toast.makeText(context, "Vui lòng thử lại", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Phần xử lý khi cancel từ DatabaseError ở đây...
            }
        });
    }
}

