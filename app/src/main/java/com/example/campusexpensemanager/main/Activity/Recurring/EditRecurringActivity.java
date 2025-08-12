package com.example.campusexpensemanager.main.Activity.Recurring;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
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
import com.example.campusexpensemanager.main.Fragment.CategoryFragment;
import com.example.campusexpensemanager.main.Fragment.ExpenseFragment;
import com.example.campusexpensemanager.main.Fragment.RecurringFragment;
import com.example.campusexpensemanager.main.Model.Category_Expense_Model;
import com.example.campusexpensemanager.main.Model.Expense_Recurring_Model;
import com.example.campusexpensemanager.main.Repository.Category_Expense_Repository;
import com.example.campusexpensemanager.main.Repository.Expense_Reccuring_Repository;
import com.example.campusexpensemanager.main.Repository.Expense_Tracking_Repository;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EditRecurringActivity extends AppCompatActivity {
    Button btnBack;
    EditText edtName, edtExpense, edtNote, edtStartDate, edtEndDate, edtRepeatDays;
    Spinner spinnerCategory;
    Button btnEditRecurring;
    ArrayList<Category_Expense_Model> categoryList;
    Category_Expense_Repository repository_list;
    Expense_Reccuring_Repository repository;

    private int ID_RECURRING;
    private String NAME_RECURRING;
    private double MONEY_RECURRING;
    private String NOTE_RECURRING;
    private String START_DATE;
    private String END_DATE;

    private int REPEAT_DAYS;
    private int CATEGORY_ID;
    private int USER_ID;







    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recurring);
        edtName = findViewById(R.id.edtRecurringName);
        edtExpense = findViewById(R.id.edtRecurringExpense);
        edtNote = findViewById(R.id.edtRecurringNote);
        edtStartDate = findViewById(R.id.edtRecurringStartDate);
        edtEndDate = findViewById(R.id.edtRecurringEndDate);
        edtRepeatDays = findViewById(R.id.edtRecurringRepeat);
        spinnerCategory = findViewById(R.id.spinnerCategory);

        btnEditRecurring = findViewById(R.id.btnEditRecurring);
        btnBack = findViewById(R.id.btnBack);


        edtStartDate.setFocusable(false);
        edtEndDate.setFocusable(false);

        edtStartDate.setOnClickListener(v -> showDateTimePicker(edtStartDate));
        edtEndDate.setOnClickListener(v -> showDateTimePicker(edtEndDate));

        SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
        int userId = sharedPref.getInt("userId", -1);

        // Khởi tạo DB
        repository_list = new Category_Expense_Repository(EditRecurringActivity.this);
        categoryList = repository_list.getListBudget(userId);

        repository = new Expense_Reccuring_Repository(EditRecurringActivity.this);
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
        Intent intentRecurring;
        Bundle bundleRecurring =getIntent().getExtras();
        if (bundleRecurring != null) {
            ID_RECURRING = bundleRecurring.getInt("EXP_RECURRING_ID", 0);
            NAME_RECURRING = bundleRecurring.getString("EXP_RECURRING_NAME", null);
            MONEY_RECURRING = bundleRecurring.getDouble("EXP_RECURRING_EXPENSE", 0.0);
            NOTE_RECURRING = bundleRecurring.getString("EXP_RECURRING_NOTE", null);
            START_DATE = bundleRecurring.getString("EXP_RECURRING_START_DATE", null);
            END_DATE = bundleRecurring.getString("EXP_RECURRING_END_DATE", null);
            REPEAT_DAYS = bundleRecurring.getInt("EXP_RECURRING_REPEAT_INTERVAL", 0);
            CATEGORY_ID = bundleRecurring.getInt("EXP_RECURRING_CATEGORY_ID", 0);


            // Gán dữ liệu vào EditText hoặc View tương ứng
            edtName.setText(NAME_RECURRING);
            edtExpense.setText(String.valueOf(MONEY_RECURRING));
            edtNote.setText(NOTE_RECURRING);
            edtStartDate.setText(START_DATE);
            edtEndDate.setText(END_DATE);

            // Các field như CategoryID, UserID có thể hiển thị ở Spinner hoặc TextView

            // --- Set Spinner Category ---
            // Tìm vị trí category trong danh sách dựa vào CATEGORY_ID
            int position = -1;
            for (int i = 0; i < categoryList.size(); i++) {
                if (categoryList.get(i).getId() == CATEGORY_ID) { // getId() là ID category trong DB
                    position = i;
                    break;
                }
            }
            if (position >= 0) {
                spinnerCategory.setSelection(position);
            }
        }



        btnEditRecurring.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = edtName.getText().toString().trim();
                    String moneyStr = edtExpense.getText().toString().trim();
                    String note = edtNote.getText().toString().trim();
                    String start = edtStartDate.getText().toString().trim();
                    String end = edtEndDate.getText().toString().trim();
                    String repeatStr = edtRepeatDays.getText().toString().trim();


                    // Validate
                    if (TextUtils.isEmpty(name)) {
                        edtName.setError("Required");
                        return;
                    }
                    if (TextUtils.isEmpty(moneyStr)) {
                        edtExpense.setError("Required");
                        return;
                    }

                    double money ;
                    try {
                        money = Double.parseDouble(moneyStr);
                        if (money <= 0) {
                            edtExpense.setError("Must be greater than 0");
                            return;
                        }
                    } catch (NumberFormatException e) {
                        edtExpense.setError("Invalid number");
                        return;
                    }

                    int repeat = 0;
                    if (!TextUtils.isEmpty(repeatStr)) {
                        try {
                            repeat = Integer.parseInt(repeatStr);
                            if (repeat < 0) {
                                edtRepeatDays.setError("Must be >= 0");
                                return;
                            }
                        } catch (NumberFormatException e) {
                            edtRepeatDays.setError("Invalid number");
                            return;
                        }
                    }
                    int selectedIndex = spinnerCategory.getSelectedItemPosition();
                    if (selectedIndex < 0 || selectedIndex >= categoryList.size()) {
                        Toast.makeText(EditRecurringActivity.this, "Vui lòng chọn Category", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int categoryId = categoryList.get(selectedIndex).getId();
//
                    // Gán USER_ID từ SharedPreferences (bạn đã lấy userId ở onCreate)
                    USER_ID = sharedPref.getInt("userId", -1);
                    if (USER_ID == -1) {
                        Toast.makeText(EditRecurringActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    long result = repository.editRecurring(
                            ID_RECURRING, name, money, note, repeat, start, end, categoryId, USER_ID
                    );

                    if (result != -1) {
                        Toast.makeText(EditRecurringActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditRecurringActivity.this, RecurringFragment.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(EditRecurringActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            });




        btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EditRecurringActivity.this, ExpenseFragment.class);
                    startActivity(intent);
                    finish();
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


