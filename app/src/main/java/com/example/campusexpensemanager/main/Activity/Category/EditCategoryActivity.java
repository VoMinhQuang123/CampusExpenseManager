package com.example.campusexpensemanager.main.Activity.Category;

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

public class EditCategoryActivity extends AppCompatActivity {
    Button btnSave, btnBack;
    EditText edtNameBudget, edtBudgetMoney, edtDescritions;

    Category_Expense_Repository repository;

    private int ID_BUDGET = 0;
    private String NAME_BUDGET = null;
    private int MONEY_BUDGET = 0;
    private String DESCRIPTION = null;

    Intent intentBudget;
    Bundle bundleBudget;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);
        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSaveBudget);
        edtNameBudget = findViewById(R.id.edtBudgetName);
        edtBudgetMoney = findViewById(R.id.edtBudgetMoney);
        edtDescritions = findViewById(R.id.edtDescritions);
        repository = new Category_Expense_Repository(EditCategoryActivity.this);
        intentBudget = getIntent();
        bundleBudget = intentBudget.getExtras();
        if (bundleBudget != null){
            ID_BUDGET = bundleBudget.getInt("ID_BUDGET", 0);
            NAME_BUDGET = bundleBudget.getString("NAME_BUDGET", null);
            MONEY_BUDGET = bundleBudget.getInt("MONEY_BUDGET", 0);
            DESCRIPTION = bundleBudget.getString("DESCRIPTION", null);
            edtNameBudget.setText(NAME_BUDGET);
            edtBudgetMoney.setText(String.valueOf(MONEY_BUDGET));
            edtDescritions.setText(DESCRIPTION);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id =  ID_BUDGET ;
                String name = edtNameBudget.getText().toString().trim();
                String input = edtBudgetMoney.getText().toString().trim();
                String descriptions = edtDescritions.getText().toString().trim();
                if(TextUtils.isEmpty(name)){
                    edtNameBudget.setError("Null!");
                    return;
                }
                if (input.isEmpty()) {
                    // Thông báo lỗi hoặc xử lý hợp lý
                    Toast.makeText(EditCategoryActivity.this, "Vui lòng nhập số", Toast.LENGTH_SHORT).show();
                    return;
                }
                int value = Integer.parseInt(input);
                long insert =  repository.editBudget(id, name, value, descriptions);
                if(insert != -1){
                    Toast.makeText(EditCategoryActivity.this, "create success", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK); // Báo cho Activity trước là đã thêm thành công
                    finish(); // Đóng AddCategoryActivity
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
