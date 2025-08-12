package com.example.campusexpensemanager.main.Activity.Recurring;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.main.Activity.Category.AddCategoryActivity;
import com.example.campusexpensemanager.main.Activity.Tracking.AddTrackingActivity;
import com.example.campusexpensemanager.main.Fragment.CategoryFragment;
import com.example.campusexpensemanager.main.Fragment.ExpenseFragment;
import com.example.campusexpensemanager.main.Model.Category_Expense_Model;
import com.example.campusexpensemanager.main.Repository.Category_Expense_Repository;
import com.example.campusexpensemanager.main.Repository.Expense_Reccuring_Repository;
import com.example.campusexpensemanager.main.Repository.Expense_Tracking_Repository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddRecurringActivity extends AppCompatActivity {
    Button btnBack;
    EditText edtName, edtExpense, edtNote, edtStartDate, edtEndDate, edtRepeatDays;
    Spinner spinnerCategory;
    Button btnAddRecurring;
    ArrayList<Category_Expense_Model> categoryList;
    Category_Expense_Repository repository_list;
    Expense_Reccuring_Repository repository;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recurring);
        edtName = findViewById(R.id.edtRecurringName);
        edtExpense = findViewById(R.id.edtRecurringExpense);
        edtNote = findViewById(R.id.edtRecurringNote);
        edtStartDate = findViewById(R.id.edtRecurringStartDate);
        edtEndDate = findViewById(R.id.edtRecurringEndDate);
        edtRepeatDays = findViewById(R.id.edtRecurringRepeat);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnAddRecurring = findViewById(R.id.btnAddRecurring);
        btnBack = findViewById(R.id.btnBack);


        edtStartDate.setFocusable(false);
        edtEndDate.setFocusable(false);

        edtStartDate.setOnClickListener(v -> showDateTimePicker(edtStartDate));
        edtEndDate.setOnClickListener(v -> showDateTimePicker(edtEndDate));

        SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
        int userId = sharedPref.getInt("userId", -1);

        // Khởi tạo DB
        repository_list = new Category_Expense_Repository(AddRecurringActivity.this);
        categoryList = repository_list.getListBudget(userId);

        repository = new Expense_Reccuring_Repository(AddRecurringActivity.this);
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

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddRecurringActivity.this, ExpenseFragment.class);
                startActivity(intent);
                finish();
            }
        });

        btnAddRecurring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString().trim();
                String expenseStr = edtExpense.getText().toString().trim();
                String note = edtNote.getText().toString().trim();
                String startDate = edtStartDate.getText().toString().trim();
                String endDate = edtEndDate.getText().toString().trim();
                String repeatStr = edtRepeatDays.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    edtName.setError("Nhập tên chi phí");
                    return;
                }

                if (TextUtils.isEmpty(expenseStr)) {
                    edtExpense.setError("Nhập số tiền");
                    return;
                }

                double expense = Double.parseDouble(expenseStr);
                if (expense <= 0) {
                    edtExpense.setError("Số tiền không hợp lệ");
                    return;
                }

                if (TextUtils.isEmpty(startDate)) {
                    edtStartDate.setError("Nhập ngày bắt đầu");
                    return;
                }

                if (TextUtils.isEmpty(endDate)) {
                    edtEndDate.setError("Nhập ngày kết thúc");
                    return;
                }

                if (TextUtils.isEmpty(repeatStr)) {
                    edtRepeatDays.setError("Nhập số ngày lặp");
                    return;
                }

                int repeatInterval = Integer.parseInt(repeatStr);
                if (repeatInterval <= 0) {
                    edtRepeatDays.setError("Lặp không hợp lệ");
                    return;
                }

                int selectedIndex = spinnerCategory.getSelectedItemPosition();
                if (selectedIndex < 0 || selectedIndex >= categoryList.size()) {
                    Toast.makeText(AddRecurringActivity.this, "Vui lòng chọn Category", Toast.LENGTH_SHORT).show();
                    return;
                }

                int categoryId = categoryList.get(selectedIndex).getId();

                Expense_Reccuring_Repository recurringRepository = new Expense_Reccuring_Repository(AddRecurringActivity.this);
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

                if (result != -1) {
                    Toast.makeText(AddRecurringActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddRecurringActivity.this, ExpenseFragment.class));
                    finish();
                } else {
                    Toast.makeText(AddRecurringActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    private void showDateTimePicker(EditText editText) {
        final Calendar currentDate = Calendar.getInstance();
        final Calendar date = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            date.set(year, month, dayOfMonth);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view1, hourOfDay, minute) -> {
                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                date.set(Calendar.MINUTE, minute);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                editText.setText(sdf.format(date.getTime()));
            }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), true);
            timePickerDialog.show();
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));
        datePickerDialog.show();
    }

}
