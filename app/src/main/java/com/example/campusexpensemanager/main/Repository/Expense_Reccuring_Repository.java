package com.example.campusexpensemanager.main.Repository;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.Nullable;

import com.example.campusexpensemanager.main.Database.SQLite_Campus;
import com.example.campusexpensemanager.main.Model.Category_Expense_Model;
import com.example.campusexpensemanager.main.Model.Expense_Recurring_Model;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Expense_Reccuring_Repository extends SQLite_Campus {
    public Expense_Reccuring_Repository(@Nullable Context context) {
        super(context);
    }
    @SuppressLint("Range")
    public ArrayList<Expense_Recurring_Model> getListRecurring(int userID) {
        ArrayList<Expense_Recurring_Model> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + SQLite_Campus.DB_TABLE_EXPENSE_RECURRING +
                        " WHERE " + SQLite_Campus.COL_EXP_RECURRING_USER_ID + " = ?",
                new String[]{String.valueOf(userID)});

        if (cursor != null && cursor.moveToFirst()) {
            DateTimeFormatter formatter = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            }

            do {
                LocalDateTime createAt = null, updateAt = null, startDate = null, endDate = null;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && formatter != null) {
                    String createStr = cursor.getString(cursor.getColumnIndex(SQLite_Campus.COL_Create_at));
                    String updateStr = cursor.getString(cursor.getColumnIndex(SQLite_Campus.COL_Update_at));
                    String startStr  = cursor.getString(cursor.getColumnIndex(SQLite_Campus.COL_EXP_RECURRING_START_DATE));
                    String endStr    = cursor.getString(cursor.getColumnIndex(SQLite_Campus.COL_EXP_RECURRING_END_DATE));

                    if (createStr != null && !createStr.isEmpty()) createAt = LocalDateTime.parse(createStr, formatter);
                    if (updateStr != null && !updateStr.isEmpty()) updateAt = LocalDateTime.parse(updateStr, formatter);
                    if (startStr != null && !startStr.isEmpty()) startDate = LocalDateTime.parse(startStr, formatter);
                    if (endStr != null && !endStr.isEmpty()) endDate = LocalDateTime.parse(endStr, formatter);
                }

                list.add(new Expense_Recurring_Model(
                        cursor.getInt(cursor.getColumnIndex(SQLite_Campus.COL_EXP_RECURRING_ID)),
                        cursor.getString(cursor.getColumnIndex(SQLite_Campus.COL_EXP_RECURRING_NAME)),
                        cursor.getDouble(cursor.getColumnIndex(SQLite_Campus.COL_EXP_RECURRING_EXPENSE)),
                        cursor.getString(cursor.getColumnIndex(SQLite_Campus.COL_EXP_RECURRING_NOTE)),
                        createAt,
                        updateAt,
                        cursor.getInt(cursor.getColumnIndex(SQLite_Campus.COL_EXP_RECURRING_REPEAT_INTERVAL)),
                        startDate,
                        endDate,
                        cursor.getInt(cursor.getColumnIndex(SQLite_Campus.COL_EXP_RECURRING_CATEGORY_ID)),
                        cursor.getInt(cursor.getColumnIndex(SQLite_Campus.COL_EXP_RECURRING_USER_ID))
                ));
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return list;
    }

    public long addNewRecurring(String name, double expense, String note, int repeatInterval,
                                String startDate, String endDate, int categoryId, int userID){
        @SuppressLint({"NewApi", "LocalSuppress"}) DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        @SuppressLint({"NewApi", "LocalSuppress"}) ZonedDateTime zone = ZonedDateTime.now();

        @SuppressLint({"NewApi", "LocalSuppress"}) String CurrentDate = dtf.format(zone);

        ContentValues values = new ContentValues();
        values.put(SQLite_Campus.COL_EXP_RECURRING_NAME, name);
        values.put(SQLite_Campus.COL_EXP_RECURRING_EXPENSE, expense);
        values.put(SQLite_Campus.COL_EXP_RECURRING_NOTE, note);
        values.put(SQLite_Campus.COL_EXP_RECURRING_CREATED_AT, CurrentDate);
        values.put(SQLite_Campus.COL_EXP_RECURRING_REPEAT_INTERVAL, repeatInterval);
        values.put(SQLite_Campus.COL_EXP_RECURRING_START_DATE, startDate);
        values.put(SQLite_Campus.COL_EXP_RECURRING_END_DATE, endDate);
        values.put(SQLite_Campus.COL_EXP_RECURRING_CATEGORY_ID, categoryId);
        values.put(SQLite_Campus.COL_EXP_RECURRING_USER_ID, userID);
        SQLiteDatabase db = this.getWritableDatabase();
        long insert = db.insert(SQLite_Campus.DB_TABLE_EXPENSE_RECURRING, null, values);
        db.close();
        return insert;
    }
    public long editBudget(int id, String name, int expensive, String description){
        @SuppressLint({"NewApi", "LocalSuppress"}) DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        @SuppressLint({"NewApi", "LocalSuppress"}) ZonedDateTime zone = ZonedDateTime.now();

        @SuppressLint({"NewApi", "LocalSuppress"}) String CurrentDate = dtf.format(zone);

        ContentValues values = new ContentValues();
        values.put(SQLite_Campus.COL_BUDGET_NAME, name);
        values.put(SQLite_Campus.COL_BUDGET_EXPENSIVE, expensive);
        values.put(SQLite_Campus.COL_BUDGET_DESCRIPTION, description);
        values.put(SQLite_Campus.COL_Update_at, CurrentDate);
        SQLiteDatabase db = this.getWritableDatabase();
        String condition = SQLite_Campus.COL_BUDGET_ID + "=?";
        String[] params = { String.valueOf(id)};
        long result = db.update(SQLite_Campus.DB_TABLE_BUDGET, values, condition, params);
        db.close();
        return result;
    }
}
