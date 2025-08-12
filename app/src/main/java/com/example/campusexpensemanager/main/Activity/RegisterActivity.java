package com.example.campusexpensemanager.main.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.main.Repository.User_Repository;

public class RegisterActivity extends AppCompatActivity {
    EditText Name,Password, Email, Phone;
    Button btnLogin, btnRegister;

    User_Repository repository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Name        = findViewById(R.id.inputName);
        Password    = findViewById(R.id.inputPassword);
        Email       = findViewById(R.id.email);
        Phone       = findViewById(R.id.phone);
        repository  = new User_Repository(RegisterActivity.this);
        btnLogin    = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        register();
    }
    private void register() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user  = Name.getText().toString().trim();
                String pass  = Password.getText().toString().trim();
                String email = Email.getText().toString().trim();
                String phone = Phone.getText().toString().trim();

                // Kiểm tra username
                if (TextUtils.isEmpty(user)) {
                    Name.setError("Tên đăng nhập không được để trống");
                    Name.requestFocus();
                    return;
                }
                if (user.length() < 8) {
                    Name.setError("Tên đăng nhập phải có ít nhất 8 ký tự");
                    Name.requestFocus();
                    return;
                }

                // Kiểm tra password
                if (TextUtils.isEmpty(pass)) {
                    Password.setError("Mật khẩu không được để trống");
                    Password.requestFocus();
                    return;
                }
                if (pass.length() < 8) {
                    Password.setError("Mật khẩu phải có ít nhất 8 ký tự");
                    Password.requestFocus();
                    return;
                }
                if (!pass.matches(".*[a-zA-Z].*")) {
                    Password.setError("Mật khẩu phải có ít nhất một chữ cái");
                    Password.requestFocus();
                    return;
                }
                if (!pass.matches(".*[!@#$%^&*].*")) {
                    Password.setError("Mật khẩu phải có ít nhất một ký tự đặc biệt (!@#$%^&*)");
                    Password.requestFocus();
                    return;
                }

                // Kiểm tra email
                if (TextUtils.isEmpty(email)) {
                    Email.setError("Email không được để trống");
                    Email.requestFocus();
                    return;
                }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Email.setError("Email không hợp lệ");
                    Email.requestFocus();
                    return;
                }

                // Kiểm tra phone (có thể cho phép trống nếu bạn muốn)
                if (TextUtils.isEmpty(phone)) {
                    Phone.setError("Số điện thoại không được để trống");
                    Phone.requestFocus();
                    return;
                }
                if (!phone.matches("\\d{10}")) {
                    Phone.setError("Số điện thoại phải gồm 10 chữ số");
                    Phone.requestFocus();
                    return;
                }

                // Nếu qua hết các bước kiểm tra thì lưu user
                long insert = repository.saveUser(user, pass, email, phone);
                if (insert == -1) {
                    Toast.makeText(RegisterActivity.this, "Lưu thất bại", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

}
