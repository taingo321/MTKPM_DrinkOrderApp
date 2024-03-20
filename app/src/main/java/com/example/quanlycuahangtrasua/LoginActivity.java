package com.example.quanlycuahangtrasua;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlycuahangtrasua.Model.Users;
import com.example.quanlycuahangtrasua.Prevalent.Prevalent;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText inputEditTextUsername, inputEditTextPassword;
    private TextInputLayout inputLayoutUsername,inputLayoutPassword;
    private ProgressDialog progressBar;
    private AppCompatButton compatButtonLogin;
    private TextView txtAdmin, txtNotAdmin;
    //private TextView ChangeToReg;
    private String parentName = "Users";
    boolean isValidUsername,isValidPassword = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        compatButtonLogin = findViewById(R.id.login_button);
        //ChangeToReg = findViewById(R.id.tvChangeToReg);
        inputLayoutUsername= findViewById(R.id.til_UserName_log);
        inputLayoutPassword=findViewById(R.id.til_Pass_log);
        inputEditTextUsername = findViewById(R.id.edt_UserName_log);
        inputEditTextPassword = findViewById(R.id.edt_Pass_log);

        progressBar = new ProgressDialog(this);

        inputEditTextUsername.addTextChangedListener(new LoginActivity.UsernameTextWatcher());
        inputEditTextPassword.addTextChangedListener(new LoginActivity.PasswordTextWatcher());
        txtAdmin = findViewById(R.id.admin_panel_link);
        txtNotAdmin = findViewById(R.id.not_admin_panel_link);
        compatButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(areAllFieldsValid()){
                    loginUser();
                }

            }
        });
        txtAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compatButtonLogin.setText("Đăng nhập Admin");
                txtAdmin.setVisibility(View.INVISIBLE);
                txtNotAdmin.setVisibility(View.VISIBLE);
                parentName = "Admins";
            }
        });

        txtNotAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compatButtonLogin.setText("Đăng nhập");
                txtAdmin.setVisibility(View.VISIBLE);
                txtNotAdmin.setVisibility(View.INVISIBLE);
                parentName = "Users";
            }
        });

        /*ChangeToReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });*/

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
    private void loginUser() {
        String username = inputEditTextUsername.getText().toString();
        String password = inputEditTextPassword.getText().toString();

        if (TextUtils.isEmpty(username)){
            Toast.makeText(this, "Vui lòng nhập tên tài khoản", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
        }

        else {
            progressBar.setTitle("Đăng nhập tài khoản");
            progressBar.setMessage("Vui lòng chờ");
            progressBar.setCanceledOnTouchOutside(false);
            progressBar.show();
            AllowAccessToAccount(username, password);
        }
    }

    private void AllowAccessToAccount(String username, String password) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(parentName).child(username).exists()){
                    Users usersData = snapshot.child(parentName).child(username).getValue(Users.class);
                    if (usersData.getUsername().equals(username)){
                        if (usersData.getPassword().equals(password)){
                            if (parentName.equals("Admins")){
                                Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                progressBar.dismiss();
                                Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                                Prevalent.currentOnlineUser = usersData;
                                startActivity(intent);
                            }
                            else if (parentName.equals("Users")){
                                Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                progressBar.dismiss();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                Prevalent.currentOnlineUser = usersData;
                                startActivity(intent);

                            }

                        }
                        else {
                            progressBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                else {
                    Toast.makeText(LoginActivity.this, "Tài khoản " + username + " không tồn tại", Toast.LENGTH_SHORT).show();
                    progressBar.dismiss();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private boolean areAllFieldsValid() {
        return isValidUsername && isValidPassword ;
    }


}