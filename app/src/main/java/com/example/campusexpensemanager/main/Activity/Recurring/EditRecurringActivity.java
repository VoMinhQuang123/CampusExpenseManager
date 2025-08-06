package com.example.campusexpensemanager.main.Activity.Recurring;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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
    List<Category_Expense_Model> categoryList;
    ArrayAdapter<Category_Expense_Model> adapterCategory;
    Map<String, Integer> categoryMap = new HashMap<>(); // Map tên -> ID
    Button btnEditRecurring;

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

        SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
        int userId = sharedPref.getInt("userId", -1);


        repository = new Expense_Reccuring_Repository(EditRecurringActivity.this);

        Category_Expense_Repository categoryRepository = new Category_Expense_Repository(EditRecurringActivity.this);
        categoryList = categoryRepository.getListBudget(userId);

        adapterCategory = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryList);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapterCategory);

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




        // Lấy dữ liệu từ Bundle
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ID_RECURRING = bundle.getInt("ID_RECURRING", 0);
            NAME_RECURRING = bundle.getString("NAME_RECURRING", "");
            MONEY_RECURRING = bundle.getDouble("MONEY_RECURRING", 0.0);
            NOTE_RECURRING = bundle.getString("NOTE_RECURRING", "");
            START_DATE = bundle.getString("START_DATE", "");
            END_DATE = bundle.getString("END_DATE", "");
            REPEAT_DAYS = bundle.getInt("REPEAT_DAYS", 0);
            CATEGORY_ID = bundle.getInt("CATEGORY_ID", 0);
            USER_ID = bundle.getInt("USER_ID", 0); // bạn cần chắc chắn bundle truyền user_id

            edtName.setText(NAME_RECURRING);
            edtExpense.setText(String.valueOf(MONEY_RECURRING));
            edtNote.setText(NOTE_RECURRING);
            edtStartDate.setText(START_DATE);
            edtEndDate.setText(END_DATE);
            edtRepeatDays.setText(String.valueOf(REPEAT_DAYS));

            // Thiết lập giá trị cho Spinner từ CATEGORY_ID
            for (int i = 0; i < categoryList.size(); i++) {
                if (categoryList.get(i).getId() == CATEGORY_ID) {
                    spinnerCategory.setSelection(i);
                    break;
                }
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
                    String selectedCategoryName = spinnerCategory.getSelectedItem().toString();

                    // Validate
                    if (TextUtils.isEmpty(name)) {
                        edtName.setError("Required");
                        return;
                    }
                    if (TextUtils.isEmpty(moneyStr)) {
                        edtExpense.setError("Required");
                        return;
                    }

                    double money = Double.parseDouble(moneyStr);
                    int repeat = TextUtils.isEmpty(repeatStr) ? 0 : Integer.parseInt(repeatStr);

                    int selectedIndex = spinnerCategory.getSelectedItemPosition();
                    if (selectedIndex < 0 || selectedIndex >= categoryList.size()) {
                        Toast.makeText(EditRecurringActivity.this, "Vui lòng chọn Category", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int categoryId = categoryList.get(selectedIndex).getId();
//                    int category = categoryMap.get(selectedCategoryName);

                    long result = repository.editRecurring(
                            ID_RECURRING, name, money, note, repeat, start, end, categoryId, USER_ID
                    );

                    if (result == -1) {
                        Toast.makeText(EditRecurringActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        Toast.makeText(EditRecurringActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditRecurringActivity.this, RecurringFragment.class);
                        startActivity(intent);
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
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                editText.setText(sdf.format(date.getTime()));
            }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), true);
            timePickerDialog.show();
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));
        datePickerDialog.show();
    }
}


