package com.example.campusexpensemanager.main.Activity.Tracking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.main.Activity.Category.AddCategoryActivity;
import com.example.campusexpensemanager.main.Activity.Recurring.AddRecurringActivity;
import com.example.campusexpensemanager.main.Database.SQLite_Campus;
import com.example.campusexpensemanager.main.Fragment.CategoryFragment;
import com.example.campusexpensemanager.main.Fragment.ExpenseFragment;
import com.example.campusexpensemanager.main.Model.Category_Expense_Model;
import com.example.campusexpensemanager.main.Repository.Category_Expense_Repository;
import com.example.campusexpensemanager.main.Repository.Expense_Reccuring_Repository;
import com.example.campusexpensemanager.main.Repository.Expense_Tracking_Repository;

import java.util.ArrayList;

public class AddTrackingActivity extends AppCompatActivity {
    Button btnBack;
    EditText edtName, edtExpense, edtNote;
    Spinner spinnerCategory;
    Button btnAddTracking;
    ArrayList<Category_Expense_Model> categoryList;
    Category_Expense_Repository repository_list;
    Expense_Tracking_Repository repository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tracking);

        // Ánh xạ View
        edtName = findViewById(R.id.edtTrackingName);
        edtExpense = findViewById(R.id.edtTrackingExpense);
        edtNote = findViewById(R.id.edtTrackingNote);

        spinnerCategory = findViewById(R.id.spinnerCategory);

        btnAddTracking = findViewById(R.id.btnAddTracking);
        btnBack = findViewById(R.id.btnBack);

        SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
        int userId = sharedPref.getInt("userId", -1);

        // Khởi tạo DB
        repository_list = new Category_Expense_Repository(AddTrackingActivity.this);
        categoryList = repository_list.getListBudget(userId);

        repository = new Expense_Tracking_Repository(AddTrackingActivity.this);
        // Tạo danh sách tên từ categoryList
        ArrayList<String> categoryNames = new ArrayList<>();
        for (Category_Expense_Model category : categoryList) {
            categoryNames.add(category.getName()); // getName() là phương thức trả về tên category
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categoryNames
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCategory.setAdapter(adapter);


        btnAddTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString().trim();
                String expenses = edtExpense.getText().toString().trim();
                String note = edtNote.getText().toString().trim();

                if(TextUtils.isEmpty(name)){
                    edtName.setError(" Enter Tracking Name ");
                    return;
                }
                if(TextUtils.isEmpty(expenses)){
                    edtExpense.setError(" Enter Tracking Expense ");
                    return;
                }

                double expense = Double.parseDouble(expenses);//??



                int selectedIndex = spinnerCategory.getSelectedItemPosition();//spinnerCategory : where
                if (selectedIndex < 0 || selectedIndex >= categoryList.size()) {
                    Toast.makeText(AddTrackingActivity.this, "Vui lòng chọn Category", Toast.LENGTH_SHORT).show();
                    return;
                }
                int categoryId = categoryList.get(selectedIndex).getId();
                Expense_Tracking_Repository trackingRepository = new Expense_Tracking_Repository(AddTrackingActivity.this);
                long result = trackingRepository.addNewTracking(
                        name,
                        expense,
                        note,
                        categoryId,
                        userId
                );//addNewTracking : where

                if (result != -1) {
                    Toast.makeText(AddTrackingActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddTrackingActivity.this, ExpenseFragment.class));
                    finish();
                } else {
                    Toast.makeText(AddTrackingActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                }





            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddTrackingActivity.this, ExpenseFragment.class);
                startActivity(intent);
                finish();
            }
        });

    }
}

