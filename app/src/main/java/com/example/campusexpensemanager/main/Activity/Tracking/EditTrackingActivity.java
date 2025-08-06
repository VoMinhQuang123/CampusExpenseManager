package com.example.campusexpensemanager.main.Activity.Tracking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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
    Category_Expense_Repository repository_list;
    Expense_Tracking_Repository repository;

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





        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditTrackingActivity.this, ExpenseFragment.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
