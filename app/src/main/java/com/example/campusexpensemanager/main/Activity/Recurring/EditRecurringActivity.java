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
import com.example.campusexpensemanager.main.Fragment.CategoryFragment;
import com.example.campusexpensemanager.main.Fragment.ExpenseFragment;
import com.example.campusexpensemanager.main.Fragment.RecurringFragment;
import com.example.campusexpensemanager.main.Model.Category_Expense_Model;
import com.example.campusexpensemanager.main.Model.Expense_Recurring_Model;
import com.example.campusexpensemanager.main.Repository.Category_Expense_Repository;
import com.example.campusexpensemanager.main.Repository.Expense_Reccuring_Repository;
import com.example.campusexpensemanager.main.Repository.Expense_Tracking_Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
        // Initialize all UI components (EditTexts, Buttons, Spinners, etc.)
        initViews();
        // Initialize repositories for database operations
        initRepositories();
        // Load category data and populate the spinner
        loadCategories();
        // Retrieve and display data passed from the previous screen (via Bundle)
        loadBundleData();
        // Set up date pickers for start and end date fields
        setupDatePickers();
        // Set up button click listeners and other event handlers
        setupListeners();
    }
    private void initViews() {
        edtName = findViewById(R.id.edtRecurringName);
        edtExpense = findViewById(R.id.edtRecurringExpense);
        edtNote = findViewById(R.id.edtRecurringNote);
        edtStartDate = findViewById(R.id.edtRecurringStartDate);
        edtEndDate = findViewById(R.id.edtRecurringEndDate);
        edtRepeatDays = findViewById(R.id.edtRecurringRepeat);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnEditRecurring = findViewById(R.id.btnEditRecurring);
        btnBack = findViewById(R.id.btnBack);
    }
    private void initRepositories() {
        SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
        int userId = sharedPref.getInt("userId", -1);
        repository = new Expense_Reccuring_Repository(this);
        Category_Expense_Repository categoryRepository = new Category_Expense_Repository(this);
        categoryList = categoryRepository.getListBudget(userId);
    }
    private void loadCategories() {
        ArrayList<String> categoryNames = new ArrayList<>();
        for (Category_Expense_Model category : categoryList) {
            categoryNames.add(category.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, categoryNames
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }
    private void loadBundleData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return;

        ID_RECURRING = bundle.getInt("ID_RECURRING", 0);
        NAME_RECURRING = bundle.getString("NAME_RECURRING", "");
        MONEY_RECURRING = bundle.getDouble("MONEY_RECURRING", 0.0);
        NOTE_RECURRING = bundle.getString("NOTE_RECURRING", "");
        START_DATE = bundle.getString("START_DATE", "");
        END_DATE = bundle.getString("END_DATE", "");
        REPEAT_DAYS = bundle.getInt("REPEAT_DAYS", 0);
        CATEGORY_ID = bundle.getInt("CATEGORY_ID", 0);
        USER_ID = bundle.getInt("USER_ID", 0);

        edtName.setText(NAME_RECURRING);
        edtExpense.setText(String.valueOf(MONEY_RECURRING));
        edtNote.setText(NOTE_RECURRING);
        edtStartDate.setText(START_DATE);
        edtEndDate.setText(END_DATE);
        edtRepeatDays.setText(String.valueOf(REPEAT_DAYS));

        for (int i = 0; i < categoryList.size(); i++) {
            if (categoryList.get(i).getId() == CATEGORY_ID) {
                spinnerCategory.setSelection(i);
                break;
            }
        }
    }
    private void setupDatePickers() {
        edtStartDate.setOnClickListener(v -> showDatePicker(edtStartDate));
        edtEndDate.setOnClickListener(v -> showDatePicker(edtEndDate));
    }
    private void showDatePicker(EditText editText) {
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
    private void setupListeners() {
        btnEditRecurring.setOnClickListener(v -> handleEditRecurring());
        btnBack.setOnClickListener(v -> finish());
    }
    private void handleEditRecurring() {
        String name = edtName.getText().toString().trim();
        String moneyStr = edtExpense.getText().toString().trim();
        String note = edtNote.getText().toString().trim();
        String startInput = edtStartDate.getText().toString().trim();
        String endInput = edtEndDate.getText().toString().trim();
        String repeatStr = edtRepeatDays.getText().toString().trim();

        // Check null hoặc giống dữ liệu cũ
        if (name.equals(NAME_RECURRING)) name = null;
        if (!TextUtils.isEmpty(moneyStr) && Double.parseDouble(moneyStr) == MONEY_RECURRING) moneyStr = null;
        if (note.equals(NOTE_RECURRING)) note = null;
        if (startInput.equals(START_DATE)) startInput = null;
        if (endInput.equals(END_DATE)) endInput = null;
        if (!TextUtils.isEmpty(repeatStr) && Integer.parseInt(repeatStr) == REPEAT_DAYS) repeatStr = null;

        // Validate các trường bắt buộc
        if (TextUtils.isEmpty(name) && name != null) {
            edtName.setError("Required");
            return;
        }
        if (moneyStr != null && TextUtils.isEmpty(moneyStr)) {
            edtExpense.setError("Required");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        // Check start date < end date
        try {
            // Parse các ngày cũ
            Date startDateOld = null, endDateOld = null;
            if (START_DATE != null && !START_DATE.isEmpty())
                startDateOld = sdf.parse(START_DATE);
            if (END_DATE != null && !END_DATE.isEmpty())
                endDateOld = sdf.parse(END_DATE);

            // Parse các ngày nhập mới nếu có
            Date startDateNew = null, endDateNew = null;
            if (startInput != null && !startInput.isEmpty())
                startDateNew = sdf.parse(startInput);
            if (endInput != null && !endInput.isEmpty())
                endDateNew = sdf.parse(endInput);

            // Case 1: start mới, end cũ
            if (startDateNew != null && endDateOld != null && endInput == null) {
                if (startDateNew.after(endDateOld)) {
                    Toast.makeText(this, "Start date phải nhỏ hơn hoặc bằng End date", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            // Case 2: start cũ, end mới
            else if (startDateOld != null && endDateNew != null && startInput == null) {
                if (startDateOld.after(endDateNew)) {
                    Toast.makeText(this, "Start date phải nhỏ hơn hoặc bằng End date", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            // Case 3: start mới, end mới
            else if (startDateNew != null && endDateNew != null) {
                if (startDateNew.after(endDateNew)) {
                    Toast.makeText(this, "Start date phải nhỏ hơn hoặc bằng End date", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            // Nếu cả 2 không đổi hoặc không đủ dữ liệu để kiểm tra thì bỏ qua
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Ngày không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }


        Double money = moneyStr != null ? Double.parseDouble(moneyStr) : null;
        Integer repeat = repeatStr != null ? Integer.parseInt(repeatStr) : null;


        int selectedIndex = spinnerCategory.getSelectedItemPosition();
        int categoryId = categoryList.get(selectedIndex).getId();

        long result = repository.editRecurring(
                ID_RECURRING,
                name,
                money,
                note,
                repeat,
                startInput,
                endInput,
                categoryId,
                USER_ID
        );
        Log.d("EditRecurring", "Update result = " + result);

        if (result == -1) {
            Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

}


