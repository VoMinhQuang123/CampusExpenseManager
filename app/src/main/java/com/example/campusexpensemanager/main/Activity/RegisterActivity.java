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
    Button btnBack, btnRegister;

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
        btnBack    = findViewById(R.id.btnBack);
        btnRegister = findViewById(R.id.btnRegister);

        register();
    }
    private void register(){
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user  = Name.getText().toString().trim();
                String pass  = Password.getText().toString().trim();
                String email = Email.getText().toString().trim();
                String phone = Phone.getText().toString().trim();

                if(TextUtils.isEmpty(user)){
                    Name.setError("x");
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    Password.setError("y");
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    Name.setError("z");
                    return;
                }
                if(TextUtils.isEmpty(phone)){
                    Password.setError("b");
                    return;
                }

                long insert = repository.saveUser(user, pass, email, phone);
                if(insert == -1){
                    Toast.makeText(RegisterActivity.this, "Save fail", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Save success", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
