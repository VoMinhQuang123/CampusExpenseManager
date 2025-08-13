package com.example.campusexpensemanager.main.Activity.Recurring;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.main.Activity.Category.EditCategoryActivity;
import com.example.campusexpensemanager.main.Fragment.ExpenseFragment;
import com.example.campusexpensemanager.main.Model.Category_Expense_Model;
import com.example.campusexpensemanager.main.Repository.Category_Expense_Repository;
import com.example.campusexpensemanager.main.Repository.Expense_Reccuring_Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddRecurringActivity extends AppCompatActivity {

    private EditText edtName, edtExpense, edtNote, edtStartDate, edtEndDate, edtRepeatDays;
    private Spinner spinnerCategory;
    private Button btnAddRecurring, btnBack;

    private ArrayList<Category_Expense_Model> categoryList;
    private Category_Expense_Repository categoryRepository;
    private Expense_Reccuring_Repository recurringRepository;
    private int userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recurring);

        initViews();
        initRepositories();
        loadUserId();
        loadCategories();
        setupDatePickers();
        setupListeners();
    }

    /** Initialize UI components by finding views */
    private void initViews() {
        edtName = findViewById(R.id.edtRecurringName);
        edtExpense = findViewById(R.id.edtRecurringExpense);
        edtNote = findViewById(R.id.edtRecurringNote);
        edtStartDate = findViewById(R.id.edtRecurringStartDate);
        edtEndDate = findViewById(R.id.edtRecurringEndDate);
        edtRepeatDays = findViewById(R.id.edtRecurringRepeat);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnAddRecurring = findViewById(R.id.btnAddRecurring);
        btnBack = findViewById(R.id.btnBack);
        // Prevent keyboard from showing on date fields
        edtStartDate.setFocusable(false);
        edtEndDate.setFocusable(false);
    }

    /** Initialize repositories for data access */
    private void initRepositories() {
        categoryRepository = new Category_Expense_Repository(this);
        recurringRepository = new Expense_Reccuring_Repository(this);
    }
    /** Load user ID from SharedPreferences */
    private void loadUserId() {
        SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
        userId = sharedPref.getInt("userId", -1);
    }

    /** Load category data and populate spinner */
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
    /** Setup date pickers for start and end date EditTexts */
    private void setupDatePickers() {
        edtStartDate.setOnClickListener(v -> showDatePickerDialog(edtStartDate));
        edtEndDate.setOnClickListener(v -> showDatePickerDialog(edtEndDate));
    }
    /** Show a DatePickerDialog and set selected date on the given EditText */
    private void showDatePickerDialog(EditText editText) {
        final Calendar currentDate = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year, month, dayOfMonth);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    editText.setText(sdf.format(selectedDate.getTime()));
                },
                currentDate.get(Calendar.YEAR),
                currentDate.get(Calendar.MONTH),
                currentDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    /** Setup button click listeners */
    private void setupListeners() {
        btnBack.setOnClickListener(v -> {

            finish();
        });
        btnAddRecurring.setOnClickListener(v -> {
            if (validateInputs()) {
                addNewRecurringExpense();
            }
        });
    }
    /** Validate all user inputs before saving */
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

        // Check date fields are not empty
        String startDateStr = edtStartDate.getText().toString().trim();
        String endDateStr = edtEndDate.getText().toString().trim();

        if (TextUtils.isEmpty(startDateStr)) {
            edtStartDate.setError("Nhập ngày bắt đầu");
            return false;
        }
        if (TextUtils.isEmpty(endDateStr)) {
            edtEndDate.setError("Nhập ngày kết thúc");
            return false;
        }

        // Kiểm tra start date <= end date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            if (sdf.parse(startDateStr).after(sdf.parse(endDateStr))) {
                Toast.makeText(this, "Start date phải nhỏ hơn hoặc bằng End date", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (ParseException e) {
            Toast.makeText(this, "Ngày không hợp lệ", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(edtRepeatDays.getText().toString().trim())) {
            edtRepeatDays.setError("Nhập số ngày lặp");
            return false;
        }
        try {
            int repeat = Integer.parseInt(edtRepeatDays.getText().toString().trim());
            if (repeat <= 0) {
                edtRepeatDays.setError("Lặp không hợp lệ");
                return false;
            }
        } catch (NumberFormatException e) {
            edtRepeatDays.setError("Lặp không hợp lệ");
            return false;
        }

        int selectedIndex = spinnerCategory.getSelectedItemPosition();
        if (selectedIndex < 0 || selectedIndex >= categoryList.size()) {
            Toast.makeText(this, "Vui lòng chọn Category", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
    /** Add new recurring expense to the database */
    private void addNewRecurringExpense() {
        String name = edtName.getText().toString().trim();
        double expense = Double.parseDouble(edtExpense.getText().toString().trim());
        String note = edtNote.getText().toString().trim();
        String startDate = edtStartDate.getText().toString().trim();
        String endDate = edtEndDate.getText().toString().trim();
        int repeatInterval = Integer.parseInt(edtRepeatDays.getText().toString().trim());

        int selectedIndex = spinnerCategory.getSelectedItemPosition();

        int categoryId = categoryList.get(selectedIndex).getId();
        long result = recurringRepository.addNewRecurring(
                name,
                expense,
                note,
                repeatInterval,
                startDate,
                endDate,
                categoryId,
                userId
        );

        if(result != -1){
            Toast.makeText(AddRecurringActivity.this, "create success", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK); // Báo cho Activity trước là đã thêm thành công
            finish(); // Đóng AddCategoryActivity
        }
    }
}
