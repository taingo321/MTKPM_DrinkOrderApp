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

    private TextInputEditText login_username_input, login_password_input;
    private TextInputLayout layusername,laypassword;
    private ProgressDialog loadingBar;
    private AppCompatButton login_button;
    private TextView admin_panel_link, not_admin_panel_link;
    //private TextView ChangeToReg;
    private String parentDBName = "Users";
    boolean isValidUsername,isValidPassword=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_button = findViewById(R.id.login_button);
        //ChangeToReg = findViewById(R.id.tvChangeToReg);
        layusername= findViewById(R.id.til_UserName_log);
        laypassword=findViewById(R.id.til_Pass_log);
        login_username_input = findViewById(R.id.edt_UserName_log);
        login_password_input = findViewById(R.id.edt_Pass_log);

        loadingBar = new ProgressDialog(this);

        login_username_input.addTextChangedListener(new LoginActivity.UsernameTextWatcher());
        login_password_input.addTextChangedListener(new LoginActivity.PasswordTextWatcher());
        admin_panel_link = findViewById(R.id.admin_panel_link);
        not_admin_panel_link = findViewById(R.id.not_admin_panel_link);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(areAllFieldsValid()){
                    loginUser();
                }

            }
        });
        admin_panel_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_button.setText("Đăng nhập Admin");
                admin_panel_link.setVisibility(View.INVISIBLE);
                not_admin_panel_link.setVisibility(View.VISIBLE);
                parentDBName = "Admins";
            }
        });

        not_admin_panel_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_button.setText("Đăng nhập");
                admin_panel_link.setVisibility(View.VISIBLE);
                not_admin_panel_link.setVisibility(View.INVISIBLE);
                parentDBName = "Users";
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
                layusername.setErrorEnabled(true);
                layusername.setError("Không được bỏ trống");
            } else if (username.length() < 7) {
                isValidUsername=false;
                layusername.setErrorEnabled(true);
                layusername.setError("Username trên 7 kí tự");
            } else {
                isValidUsername=true;
                layusername.setErrorEnabled(false);
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
                laypassword.setErrorEnabled(true);
                laypassword.setError("Không được bỏ trống");
            } else if (password.length() < 6) {
                isValidPassword=false;
                laypassword.setErrorEnabled(true);
                laypassword.setError("Password trên 6 kí tự");
            } else {
                isValidPassword=true;
                laypassword.setErrorEnabled(false);
            }
        }
        @Override
        public void afterTextChanged(Editable s) {
            // Not needed for this example
        }
    }
    private void loginUser() {
        String username = login_username_input.getText().toString();
        String password = login_password_input.getText().toString();

        if (TextUtils.isEmpty(username)){
            Toast.makeText(this, "Vui lòng nhập tên tài khoản", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
        }

        else {
            loadingBar.setTitle("Đăng nhập tài khoản");
            loadingBar.setMessage("Vui lòng chờ");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(username, password);
        }
    }

    private void AllowAccessToAccount(String username, String password) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(parentDBName).child(username).exists()){
                    Users usersData = snapshot.child(parentDBName).child(username).getValue(Users.class);

                    if (usersData.getUsername().equals(username)){
                        if (usersData.getPassword().equals(password)){
                            if (parentDBName.equals("Admins")){
                                Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                                Prevalent.currentOnlineUser = usersData;
                                startActivity(intent);
                            }
                            else if (parentDBName.equals("Users")){
                                Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                Prevalent.currentOnlineUser = usersData;
                                startActivity(intent);
                            }

                        }
                        else {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                else {
                    Toast.makeText(LoginActivity.this, "Tài khoản " + username + " không tồn tại", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
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