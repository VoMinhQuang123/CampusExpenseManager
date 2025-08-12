package com.example.campusexpensemanager.main.Activity.Tracking;

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
import com.example.campusexpensemanager.main.Fragment.ExpenseFragment;
import com.example.campusexpensemanager.main.Model.Category_Expense_Model;
import com.example.campusexpensemanager.main.Repository.Category_Expense_Repository;
import com.example.campusexpensemanager.main.Repository.Expense_Tracking_Repository;

import java.util.ArrayList;

public class EditTrackingActivity extends AppCompatActivity {
    Button btnBack;
    EditText edtName, edtExpense, edtNote;
    Spinner spinnerCategory;
    Button btnEditTracking;
    ArrayList<Category_Expense_Model> categoryList;
    Category_Expense_Repository categoryRepository;
    Expense_Tracking_Repository repository;

    // Dữ liệu nhận được
    private int ID_TRACKING;
    private String NAME_TRACKING;
    private double MONEY_TRACKING;
    private String NOTE_TRACKING;
    private int CATEGORY_ID;
    private int USER_ID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tracking);

        // Ánh xạ View
        edtName = findViewById(R.id.edtTrackingName);
        edtExpense = findViewById(R.id.edtTrackingExpense);
        edtNote = findViewById(R.id.edtTrackingNote);

        spinnerCategory = findViewById(R.id.spinnerCategory);

        btnEditTracking = findViewById(R.id.btnEditTracking);
        btnBack = findViewById(R.id.btnBack);

        // Khởi tạo repository
        SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
        USER_ID = sharedPref.getInt("userId", -1);

        repository = new Expense_Tracking_Repository(this);
        categoryRepository = new Category_Expense_Repository(this);
        categoryList = categoryRepository.getListBudget(USER_ID);

        // Load danh sách category lên spinner
        loadCategories();

        // Lấy dữ liệu từ bundle truyền vào
        loadBundleData();

        // Thiết lập sự kiện nút Lưu
        btnEditTracking.setOnClickListener(v -> handleEditTracking());

        // Nút Back quay lại fragment ExpenseFragment
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(EditTrackingActivity.this, ExpenseFragment.class);
            startActivity(intent);
            finish();
        });
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

        ID_TRACKING = bundle.getInt("ID_TRACKING", 0);
        NAME_TRACKING = bundle.getString("NAME_TRACKING", "");
        MONEY_TRACKING = bundle.getDouble("MONEY_TRACKING", 0.0);
        NOTE_TRACKING = bundle.getString("NOTE_TRACKING", "");
        CATEGORY_ID = bundle.getInt("CATEGORY_ID", 0);
        USER_ID = bundle.getInt("USER_ID", USER_ID);

        // Hiển thị dữ liệu lên form
        edtName.setText(NAME_TRACKING);
        edtExpense.setText(String.valueOf(MONEY_TRACKING));
        edtNote.setText(NOTE_TRACKING);

        // Chọn category tương ứng trên spinner
        for (int i = 0; i < categoryList.size(); i++) {
            if (categoryList.get(i).getId() == CATEGORY_ID) {
                spinnerCategory.setSelection(i);
                break;
            }
        }
    }

    private void handleEditTracking() {
        String name = edtName.getText().toString().trim();
        String moneyStr = edtExpense.getText().toString().trim();
        String note = edtNote.getText().toString().trim();

        // Validate các trường bắt buộc
        if (TextUtils.isEmpty(name)) {
            edtName.setError("Required");
            return;
        }
        if (TextUtils.isEmpty(moneyStr)) {
            edtExpense.setError("Required");
            return;
        }

        double money;
        try {
            money = Double.parseDouble(moneyStr);
        } catch (NumberFormatException e) {
            edtExpense.setError("Invalid number");
            return;
        }

        int selectedIndex = spinnerCategory.getSelectedItemPosition();
        int categoryId = categoryList.get(selectedIndex).getId();

        // Gọi hàm update trong repository
        long result = repository.editTracking(
                ID_TRACKING,
                name.equals(NAME_TRACKING) ? null : name,
                money == MONEY_TRACKING ? null : money,
                note.equals(NOTE_TRACKING) ? null : note,
                categoryId,
                USER_ID
        );
        Log.d("EditTracking", "Update result = " + result);

        if (result == -1) {
            Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
