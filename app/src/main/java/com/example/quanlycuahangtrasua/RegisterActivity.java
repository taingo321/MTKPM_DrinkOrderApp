package com.example.quanlycuahangtrasua;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button btnRegister;
    private TextInputEditText inputEditTextRegisterUsername, inputEditTextRegisterPhone, inputEditTextRegisterPassword;
    private TextInputLayout inputLayoutUsername,inputLayoutPhone,inputLayoutPassword;
    private ProgressDialog progressBar;
    boolean isValidPhoneNumber,isValidUsername,isValidPassword=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister = findViewById(R.id.register);
        inputEditTextRegisterUsername = findViewById(R.id.edt_UserName_reg);
        inputEditTextRegisterPhone = findViewById(R.id.edt_Phone_reg);
        inputEditTextRegisterPassword = findViewById(R.id.edt_Pass_reg);
        inputLayoutUsername=findViewById(R.id.til_UserName_reg);
        inputLayoutPhone=findViewById(R.id.til_Phone_reg);
        inputLayoutPassword=findViewById(R.id.til_Pass_reg);

        progressBar = new ProgressDialog(this);

        inputEditTextRegisterUsername.addTextChangedListener(new UsernameTextWatcher());
        inputEditTextRegisterPhone.addTextChangedListener(new PhoneTextWatcher());
        inputEditTextRegisterPassword.addTextChangedListener(new PasswordTextWatcher());
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount() {
        String username = inputEditTextRegisterUsername.getText().toString();
        String phone = inputEditTextRegisterPhone.getText().toString();
        String password = inputEditTextRegisterPassword.getText().toString();

        if(areAllFieldsValid()){
            ValidateUsername(username, phone, password);
        }
    }

        private void ValidateUsername(String username, String phone, String password) {
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

                    RootRef.child("users").child(username).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "Tài khoản của bạn đã tạo thành công", Toast.LENGTH_SHORT).show();
                                        progressBar.dismiss();

                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                    else {
                                        progressBar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Đã xảy ra lỗi,vui lòng thử lại", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Tên tài khoản " + username + " đã tồn tại.", Toast.LENGTH_SHORT).show();
                    progressBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Vui lòng thử lại", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private class UsernameTextWatcher implements TextWatcher {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Not needed for this example
        }
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Check password and update error message accordingly
            String username = s.toString();

            if (username.isEmpty()) {
                isValidUsername=false;
                inputLayoutUsername.setErrorEnabled(true);
                inputLayoutUsername.setError("Không được bỏ trống");
            } else if (username.length() < 7) {
                isValidUsername=false;
                inputLayoutUsername.setErrorEnabled(true);
                inputLayoutUsername.setError("Username trên 7 kí tự");
            } else {
                isValidUsername=true;
                inputLayoutUsername.setErrorEnabled(false);
            }
        }
        @Override
        public void afterTextChanged(Editable s) {
            // Not needed for this example
        }
    }

    private class PhoneTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Not needed for this example
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String phoneNumber = s.toString().trim();
            if (phoneNumber.isEmpty()) {
                isValidPhoneNumber=false;
                inputLayoutPhone.setErrorEnabled(true);
                inputLayoutPhone.setError("Không được bỏ trống");
            } else if (!isValidPhoneNumber(phoneNumber)) {
                isValidPhoneNumber=false;
                inputLayoutPhone.setErrorEnabled(true);
                inputLayoutPhone.setError("Không hợp lệ");
            } else {
                isValidPhoneNumber=true;
                inputLayoutPhone.setErrorEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Not needed for this example
        }
    }

    private class PasswordTextWatcher implements TextWatcher {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Not needed for this example
        }
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Check password and update error message accordingly
            String password = s.toString();

            if (password.isEmpty()) {
                isValidPassword=false;
                inputLayoutPassword.setErrorEnabled(true);
                inputLayoutPassword.setError("Không được bỏ trống");
            } else if (password.length() < 6) {
                isValidPassword=false;
                inputLayoutPassword.setErrorEnabled(true);
                inputLayoutPassword.setError("Password trên 6 kí tự");
            } else {
                isValidPassword=true;
                inputLayoutPassword.setErrorEnabled(false);
            }
        }
        @Override
        public void afterTextChanged(Editable s) {
            // Not needed for this example
        }
    }
    private boolean isValidPhoneNumber(String phoneNumber) {
        return Patterns.PHONE.matcher(phoneNumber).matches();
    }
    private boolean areAllFieldsValid() {
        return isValidUsername  && isValidPhoneNumber
                 && isValidPassword ;
    }
}