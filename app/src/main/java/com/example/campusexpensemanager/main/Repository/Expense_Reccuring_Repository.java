package com.example.campusexpensemanager.main.Repository;

import android.content.Context;

import androidx.annotation.Nullable;

import com.example.campusexpensemanager.main.Database.SQLite_Campus;

public class Expense_Reccuring_Repository extends SQLite_Campus {
    public Expense_Reccuring_Repository(@Nullable Context context) {
        super(context);
    }
}
