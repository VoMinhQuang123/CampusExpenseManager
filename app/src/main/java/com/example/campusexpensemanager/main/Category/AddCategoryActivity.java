package com.example.campusexpensemanager.main.Category;

import android.content.Context;
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
import com.example.campusexpensemanager.main.Fragment.CategoryFragment;
import com.example.campusexpensemanager.main.Repository.Category_Expense_Repository;

public class AddCategoryActivity extends AppCompatActivity {
    Button btnSave, btnBack;
    EditText edtNameBudget, edtBudgetMoney, edtDescritions;

    Category_Expense_Repository repository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSaveBudget);
        edtNameBudget = findViewById(R.id.edtBudgetName);
        edtBudgetMoney = findViewById(R.id.edtBudgetMoney);
        edtDescritions = findViewById(R.id.edtDescritions);
        repository = new Category_Expense_Repository(AddCategoryActivity.this);
        SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
        int userId = sharedPref.getInt("userId", -1);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtNameBudget.getText().toString().trim();
                int expensive = Integer.parseInt(edtBudgetMoney.getText().toString().trim());
                String descriptions = edtDescritions.getText().toString().trim();
                if(TextUtils.isEmpty(name)){
                    edtNameBudget.setError("Null!");
                    return;
                }
                if(expensive <= 0){
                    edtBudgetMoney.setError("Null!");
                    return;
                }
                long insert =  repository.addNewBudget(name, expensive, descriptions, userId);
                if(insert == -1){
                    Toast.makeText(AddCategoryActivity.this, "can not create", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    Toast.makeText(AddCategoryActivity.this, "create success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddCategoryActivity.this, CategoryFragment.class);
                    startActivity(intent);
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddCategoryActivity.this, CategoryFragment.class);
                startActivity(intent);
            }
        });
    }
}
