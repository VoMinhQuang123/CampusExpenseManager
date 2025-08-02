package com.example.campusexpensemanager.main.Activity.Tracking;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.main.Database.SQLite_Campus;
import com.example.campusexpensemanager.main.Model.Category_Expense_Model;
import com.example.campusexpensemanager.main.Repository.Category_Expense_Repository;
import com.example.campusexpensemanager.main.Repository.Expense_Tracking_Repository;

import java.util.ArrayList;

public class AddTrackingActivity extends AppCompatActivity {

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


    }
}

