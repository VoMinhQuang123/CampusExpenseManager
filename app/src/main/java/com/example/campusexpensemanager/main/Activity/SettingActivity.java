package com.example.campusexpensemanager.main.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.main.Model.User_Model;
import com.example.campusexpensemanager.main.Repository.User_Repository;

public class SettingActivity extends AppCompatActivity {

    EditText etUsername, etPassword, etEmail, etPhone;
    Button btnEdit, btnBack;

    User_Repository repository;
    User_Model currentUser;

    boolean isEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etEmail    = findViewById(R.id.etEmail);
        etPhone    = findViewById(R.id.etPhone);
        btnEdit    = findViewById(R.id.btnEdit);
        btnBack    = findViewById(R.id.btnBack);

        repository = new User_Repository(this);

        // Lấy userId hoặc username đã lưu trong SharedPreferences hoặc Intent
        SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
        String username = sharedPref.getString("username", null);
        String password = sharedPref.getString("password", null);

        if (username == null || password == null) {
            Toast.makeText(this, "Không tìm thấy thông tin đăng nhập", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadUserInfo(username, password);

        btnEdit.setOnClickListener(v -> {
            if (!isEditing) {
                // Bật chế độ chỉnh sửa
                enableEditing(true);
                btnEdit.setText("Save");
                isEditing = true;
            } else {
                // Lưu thông tin sau khi kiểm tra
                if (validateInputs()) {
                    saveUserInfo();
                }
            }
        });

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(SettingActivity.this, DashBroadActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loadUserInfo(String username, String password) {
        currentUser = repository.getInforUserByUsername(username, password);

        if (currentUser != null && currentUser.getId() != 0) {
            etUsername.setText(currentUser.getUsername());
            // Hiện password dấu *, bạn có thể lưu pass trong db hay không thì tùy
            etPassword.setText("");
            etEmail.setText(currentUser.getEmail());
            etPhone.setText(currentUser.getPhone());
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void enableEditing(boolean enable) {
        etPassword.setEnabled(enable);
        etEmail.setEnabled(enable);
        etPhone.setEnabled(enable);

        if (enable) {
            etPassword.setBackgroundResource(android.R.drawable.edit_text);
            etEmail.setBackgroundResource(android.R.drawable.edit_text);
            etPhone.setBackgroundResource(android.R.drawable.edit_text);
        } else {
            etPassword.setBackgroundColor(0x00000000);
            etEmail.setBackgroundColor(0x00000000);
            etPhone.setBackgroundColor(0x00000000);
        }
    }

    private boolean validateInputs() {
        String pass = etPassword.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (TextUtils.isEmpty(pass)) {
            etPassword.setError("Password không được để trống");
            return false;
        }
        if (!pass.matches(".*[a-zA-Z].*") || !pass.matches(".*[!@#$%^&*+=?-].*")) {
            etPassword.setError("Mật khẩu phải chứa ít nhất 1 ký tự chữ và 1 ký tự đặc biệt");
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email không được để trống");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email không hợp lệ");
            return false;
        }

        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Số điện thoại không được để trống");
            return false;
        }
        if (!phone.matches("\\d{9,11}")) {
            etPhone.setError("Số điện thoại không hợp lệ (9-11 chữ số)");
            return false;
        }

        return true;
    }

    private void saveUserInfo() {
        String newPass = etPassword.getText().toString().trim();
        String newEmail = etEmail.getText().toString().trim();
        String newPhone = etPhone.getText().toString().trim();

        long result = repository.updateUserInfo(currentUser.getId(), newPass, newEmail, newPhone);

        if (result > 0) {
            Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            enableEditing(false);
            btnEdit.setText("Edit information");
            isEditing = false;

            // Cập nhật currentUser
            currentUser.setPassword(newPass);
            currentUser.setEmail(newEmail);
            currentUser.setPhone(newPhone);
        } else {
            Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}
