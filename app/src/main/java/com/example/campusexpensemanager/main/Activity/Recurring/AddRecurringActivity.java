package com.example.campusexpensemanager.main.Activity.Recurring;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.main.Activity.Tracking.AddTrackingActivity;
import com.example.campusexpensemanager.main.Model.Category_Expense_Model;
import com.example.campusexpensemanager.main.Repository.Category_Expense_Repository;
import com.example.campusexpensemanager.main.Repository.Expense_Reccuring_Repository;
import com.example.campusexpensemanager.main.Repository.Expense_Tracking_Repository;

import java.util.ArrayList;

public class AddRecurringActivity extends AppCompatActivity {
    EditText edtName, edtExpense, edtNote;
    Spinner spinnerCategory;
    Button btnAddTracking;
    ArrayList<Category_Expense_Model> categoryList;
    Category_Expense_Repository repository_list;
    Expense_Tracking_Repository repository;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recurring);
        edtName = findViewById(R.id.edtRecurringName);
        edtExpense = findViewById(R.id.edtRecurringName);
        edtNote = findViewById(R.id.edtRecurringNote);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnAddTracking = findViewById(R.id.btnAddRecurring);

        SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
        int userId = sharedPref.getInt("userId", -1);

        // Khởi tạo DB
        repository_list = new Category_Expense_Repository(AddRecurringActivity.this);
        categoryList = repository_list.getListBudget(userId);

        repository = new Expense_Tracking_Repository(AddRecurringActivity.this);
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
    }
}
