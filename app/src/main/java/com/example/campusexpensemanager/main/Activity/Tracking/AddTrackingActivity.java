package com.example.campusexpensemanager.main.Activity.Tracking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.main.Activity.Recurring.AddRecurringActivity;
import com.example.campusexpensemanager.main.Fragment.ExpenseFragment;
import com.example.campusexpensemanager.main.Model.Category_Expense_Model;
import com.example.campusexpensemanager.main.Repository.Category_Expense_Repository;
import com.example.campusexpensemanager.main.Repository.Expense_Tracking_Repository;

import java.util.ArrayList;

public class AddTrackingActivity extends AppCompatActivity {
    private EditText edtName, edtExpense, edtNote;
    private Spinner spinnerCategory;
    private Button btnAddTracking, btnBack;

    private ArrayList<Category_Expense_Model> categoryList;
    private Category_Expense_Repository categoryRepository;
    private Expense_Tracking_Repository trackingRepository;

    private int userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tracking);

        initViews();
        initRepositories();
        loadUserId();
        loadCategories();
        setupListeners();
    }

    private void initViews() {
        edtName = findViewById(R.id.edtTrackingName);
        edtExpense = findViewById(R.id.edtTrackingExpense);
        edtNote = findViewById(R.id.edtTrackingNote);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnAddTracking = findViewById(R.id.btnAddTracking);
        btnBack = findViewById(R.id.btnBack);
    }

    private void initRepositories() {
        categoryRepository = new Category_Expense_Repository(this);
        trackingRepository = new Expense_Tracking_Repository(this);
    }

    private void loadUserId() {
        SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
        userId = sharedPref.getInt("userId", -1);
    }

    private void loadCategories() {
        categoryList = categoryRepository.getListBudget(userId);

        ArrayList<String> categoryNames = new ArrayList<>();
        for (Category_Expense_Model category : categoryList) {
            categoryNames.add(category.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categoryNames
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    private void setupListeners() {
        btnAddTracking.setOnClickListener(v -> {
            if (validateInputs()) {
                addNewTracking();
            }
        });

        btnBack.setOnClickListener(v -> {
            finish();
        });
    }

    private boolean validateInputs() {
        if (TextUtils.isEmpty(edtName.getText().toString().trim())) {
            edtName.setError("Nhập tên chi phí");
            return false;
        }

        String expenseStr = edtExpense.getText().toString().trim();
        if (TextUtils.isEmpty(expenseStr)) {
            edtExpense.setError("Nhập số tiền");
            return false;
        }
        try {
            double expense = Double.parseDouble(expenseStr);
            if (expense <= 0) {
                edtExpense.setError("Số tiền không hợp lệ");
                return false;
            }
        } catch (NumberFormatException e) {
            edtExpense.setError("Số tiền không hợp lệ");
            return false;
        }

        int selectedIndex = spinnerCategory.getSelectedItemPosition();
        if (selectedIndex < 0 || selectedIndex >= categoryList.size()) {
            Toast.makeText(this, "Vui lòng chọn Category", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void addNewTracking() {
        String name = edtName.getText().toString().trim();
        double expense = Double.parseDouble(edtExpense.getText().toString().trim());
        String note = edtNote.getText().toString().trim();

        int selectedIndex = spinnerCategory.getSelectedItemPosition();
        int categoryId = categoryList.get(selectedIndex).getId();

        long result = trackingRepository.addNewTracking(
                name,
                expense,
                note,
                categoryId,
                userId
        );

        if(result != -1){
            Toast.makeText(AddTrackingActivity.this, "create success", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK); // Báo cho Activity trước là đã thêm thành công
            finish(); // Đóng AddCategoryActivity
        }
    }
}
