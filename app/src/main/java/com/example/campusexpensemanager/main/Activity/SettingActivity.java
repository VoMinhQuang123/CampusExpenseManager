package com.example.campusexpensemanager.main.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.main.Activity.Tracking.AddTrackingActivity;
import com.example.campusexpensemanager.main.Fragment.ExpenseFragment;
import com.example.campusexpensemanager.main.Fragment.HomeFragment;
import com.example.campusexpensemanager.main.Model.User_Model;
import com.example.campusexpensemanager.main.Repository.User_Repository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SettingActivity extends AppCompatActivity {

    EditText etUsername, etEmail, etPhone;
    EditText etOldPassword, etNewPassword, etConfirmPassword;
    Button btnEditInfo, btnChangePassword, btnBack;

    User_Repository repository;
    User_Model currentUser;

    boolean isEditingInfo = false;
    boolean isChangingPassword = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);

        etOldPassword = findViewById(R.id.etOldPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        btnEditInfo = findViewById(R.id.btnEditInfo);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnBack = findViewById(R.id.btnBack);

        repository = new User_Repository(this);

        loadUserInfoFromSession();

        // Ban đầu ẩn form đổi mật khẩu
        showPasswordFields(false);

        // Disable tất cả edit text ban đầu
        enableEditingInfo(false);
        enableChangePassword(false);

        btnEditInfo.setOnClickListener(v -> {
            if (!isEditingInfo) {
                // Bật chế độ chỉnh sửa thông tin cá nhân
                enableEditingInfo(true);
                isEditingInfo = true;
                btnEditInfo.setText("Save Information");
                // Khi đang edit info thì disable đổi mật khẩu
                btnChangePassword.setEnabled(false);
            } else {
                // Lưu thông tin cá nhân
                if (validateInfoInputs()) {
                    boolean result = saveUserInfo();
                    if (result) {
                        Toast.makeText(this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                        enableEditingInfo(false);
                        btnEditInfo.setText("Edit Information");
                        isEditingInfo = false;
                        btnChangePassword.setEnabled(true);
                    } else {
                        Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnChangePassword.setOnClickListener(v -> {
            if (!isChangingPassword) {
                // Hiện form đổi mật khẩu
                showPasswordFields(true);
                enableChangePassword(true);
                isChangingPassword = true;
                btnChangePassword.setText("Save Password");
                // Khi đổi mật khẩu thì disable edit info
                btnEditInfo.setEnabled(false);
            } else {
                // Xử lý đổi mật khẩu
                if (validatePasswordInputs()) {
                    boolean result = saveUserPassword();
                    if (result) {
                        Toast.makeText(this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                        showPasswordFields(false);
                        enableChangePassword(false);
                        btnChangePassword.setText("Change Password");
                        isChangingPassword = false;
                        btnEditInfo.setEnabled(true);
                    } else {
                        Toast.makeText(this, "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(SettingActivity.this, DashBroadActivity.class));
            finish();
        });
    }

    private void loadUserInfoFromSession() {
        SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
        String username = sharedPref.getString("username", null);
        String password = hashPassword(sharedPref.getString("password", null));

        if (username == null || password == null) {
            Toast.makeText(this, "Không tìm thấy thông tin đăng nhập", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        currentUser = repository.getInforUserByUsername(username, password);
        if (currentUser == null || currentUser.getId() == 0) {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etUsername.setText(currentUser.getUsername());
        etEmail.setText(currentUser.getEmail());
        etPhone.setText(currentUser.getPhone());

        // Ban đầu disable edit text
        enableEditingInfo(false);
        enableChangePassword(false);
    }

    private void enableEditingInfo(boolean enable) {
        etUsername.setEnabled(enable);
        etEmail.setEnabled(enable);
        etPhone.setEnabled(enable);

        int bg = enable ? android.R.drawable.edit_text : android.R.color.transparent;
        etUsername.setBackgroundResource(bg);
        etEmail.setBackgroundResource(bg);
        etPhone.setBackgroundResource(bg);
    }

    private void enableChangePassword(boolean enable) {
        etOldPassword.setEnabled(enable);
        etNewPassword.setEnabled(enable);
        etConfirmPassword.setEnabled(enable);

        int bg = enable ? android.R.drawable.edit_text : android.R.color.transparent;
        etOldPassword.setBackgroundResource(bg);
        etNewPassword.setBackgroundResource(bg);
        etConfirmPassword.setBackgroundResource(bg);
    }

    private void showPasswordFields(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        etOldPassword.setVisibility(visibility);
        etNewPassword.setVisibility(visibility);
        etConfirmPassword.setVisibility(visibility);
        // Bạn có thể thêm label text view nếu có
        if (!show) {
            etOldPassword.setText("");
            etNewPassword.setText("");
            etConfirmPassword.setText("");
        }
    }

    private boolean validateInfoInputs() {
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (username.isEmpty()) {
            etUsername.setError("Tên đăng nhập không được để trống");
            return false;
        }
        if (username.length() < 8) {
            etUsername.setError("Tên đăng nhập phải có ít nhất 8 ký tự");
            return false;
        }
        if (repository.isUsernameExists(username)) {
            etUsername.setError("Tên đăng nhập đã tồn tại");
            return false;
        }

        if (email.isEmpty()) {
            etEmail.setError("Email không được để trống");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email không hợp lệ");
            return false;
        }
        if (repository.isEmailExists(email)) {
            etEmail.setError("Email đã được sử dụng");
            return false;
        }
        if (phone.isEmpty()) {
            etPhone.setError("Số điện thoại không được để trống");
            return false;
        }
        if (!phone.matches("\\d{9,11}")) {
            etPhone.setError("Số điện thoại không hợp lệ (9-11 chữ số)");
            return false;
        }
        return true;
    }

    private boolean saveUserInfo() {
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        long result = repository.updateUserInfo(currentUser.getId(), username, currentUser.getPassword(), email, phone);
        if (result > 0) {
            // Update currentUser để đồng bộ
            currentUser.setUsername(username);
            currentUser.setEmail(email);
            currentUser.setPhone(phone);
            return true;
        }
        return false;
    }

    private boolean validatePasswordInputs() {
        String oldPass =  hashPassword(etOldPassword.getText().toString().trim());
        String newPass = etNewPassword.getText().toString().trim();
        String confirmPass = etConfirmPassword.getText().toString().trim();

        if (oldPass.isEmpty()) {
            etOldPassword.setError("Vui lòng nhập mật khẩu cũ");
            return false;
        }
        if (!oldPass.equals(currentUser.getPassword())) {
            etOldPassword.setError("Mật khẩu cũ không đúng");
            return false;
        }
        if (newPass.isEmpty()) {
            etNewPassword.setError("Mật khẩu mới không được để trống");
            return false;
        }
        if (!newPass.matches(".*[a-zA-Z].*") || !newPass.matches(".*[!@#$%^&*+=?-].*")) {
            etNewPassword.setError("Mật khẩu phải chứa ít nhất 1 ký tự chữ và 1 ký tự đặc biệt");
            return false;
        }
        if (!newPass.equals(confirmPass)) {
            etConfirmPassword.setError("Mật khẩu xác nhận không khớp");
            return false;
        }
        return true;
    }

    private boolean saveUserPassword() {
        String newPass = hashPassword(etNewPassword.getText().toString().trim());

        long result = repository.updateUserInfo(currentUser.getId(), currentUser.getUsername(), newPass, currentUser.getEmail(), currentUser.getPhone());
        if (result > 0) {
            currentUser.setPassword(newPass);
            return true;
        }
        return false;
    }
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

}

