package com.example.campusexpensemanager.main.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.main.Model.User_Model;
import com.example.campusexpensemanager.main.Repository.User_Repository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {
    EditText Name, Pass;
    Button btnLogin;

    TextView btnRegisterLoginpage;

    User_Repository user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Name     = findViewById(R.id.NameAccount);
        Pass     = findViewById(R.id.PassAccount);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegisterLoginpage = findViewById(R.id.btnRegisterLoginpage);
        user = new User_Repository(LoginActivity.this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = Name.getText().toString().trim();
                String pass = Pass.getText().toString().trim();
                if(TextUtils.isEmpty(name)){
                    Name.setError("Ngu");
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    Pass.setError("Ngu 2");
                    return;
                }
                String pass1 = Pass.getText().toString().trim();
                String hashedPass = hashPassword(pass1); // Hash mật khẩu nhập

                User_Model use = user.getInforUserByUsername(name, hashedPass);
                assert use != null;
                if(use.getId() > 0 && use.getUsername() != null){
                    SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("userId", use.getId());
                    editor.putString("username", name);  // lưu username
                    editor.putString("password", pass);  // lưu password
                    editor.apply();

                    Intent intent = new Intent(LoginActivity.this, DashBroadActivity.class);
                    startActivity(intent);
//                    finish();
                }
                else {
                    Toast.makeText(LoginActivity.this, "Account not exists", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnRegisterLoginpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
//                finish();
            }
        });
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
