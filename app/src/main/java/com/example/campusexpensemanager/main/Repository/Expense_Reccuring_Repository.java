package com.example.campusexpensemanager.main.Repository;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;

import com.example.campusexpensemanager.main.Database.SQLite_Campus;
import com.example.campusexpensemanager.main.Model.Category_Expense_Model;
import com.example.campusexpensemanager.main.Model.Expense_Recurring_Model;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Expense_Reccuring_Repository extends SQLite_Campus {
    private SQLiteOpenHelper dbHelper;

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

    public long editRecurring(int id, String name, double expense, String note,
                              int repeatDays, String start, String end,
                              int categoryId, int userId) {

        @SuppressLint({"NewApi", "LocalSuppress"})
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        @SuppressLint({"NewApi", "LocalSuppress"})
        ZonedDateTime zone = ZonedDateTime.now();
        @SuppressLint({"NewApi", "LocalSuppress"})
        String currentDate = dtf.format(zone);

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SQLite_Campus.COL_EXP_RECURRING_NAME, name);
        values.put(SQLite_Campus.COL_EXP_RECURRING_EXPENSE, expense);
        values.put(SQLite_Campus.COL_EXP_RECURRING_NOTE, note);
        values.put(SQLite_Campus.COL_EXP_RECURRING_REPEAT_INTERVAL, repeatDays);
        values.put(SQLite_Campus.COL_EXP_RECURRING_START_DATE, start);
        values.put(SQLite_Campus.COL_EXP_RECURRING_END_DATE, end);
        values.put(SQLite_Campus.COL_EXP_RECURRING_CATEGORY_ID, categoryId);
        values.put(SQLite_Campus.COL_EXP_RECURRING_USER_ID, userId);
        values.put(SQLite_Campus.COL_EXP_RECURRING_UPDATED_AT, currentDate);

        String condition = SQLite_Campus.COL_EXP_RECURRING_ID + "=?";
        String[] args = { String.valueOf(id) };

        long result = db.update(SQLite_Campus.DB_TABLE_EXPENSE_RECURRING, values, condition, args);
        db.close();
        return result;
    }
//Lấy tổng chi tiêu theo category_id từ bảng Expense_Recurring.
    public Map<Integer, Double> getRecurringExpenseByCategory(int userId) {
        Map<Integer, Double> map = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT " + SQLite_Campus.COL_EXP_RECURRING_CATEGORY_ID + ", " +
                        "SUM(" + SQLite_Campus.COL_EXP_RECURRING_EXPENSE + ") as Total " +
                        "FROM " + SQLite_Campus.DB_TABLE_EXPENSE_RECURRING +
                        " WHERE " + SQLite_Campus.COL_EXP_RECURRING_USER_ID + " = ?" +
                        " GROUP BY " + SQLite_Campus.COL_EXP_RECURRING_CATEGORY_ID,
                new String[]{String.valueOf(userId)}
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") int categoryId = cursor.getInt(cursor.getColumnIndex(SQLite_Campus.COL_EXP_RECURRING_CATEGORY_ID));
                @SuppressLint("Range") double total = cursor.getDouble(cursor.getColumnIndex("Total"));
                map.put(categoryId, total);
            }
            cursor.close();
        }

        db.close();
        return map;
    }

    //Lấy danh sách các khoản recurring theo category_id, trả về danh sách Expense_Recurring_Model.
    @SuppressLint("Range")
    public ArrayList<Expense_Recurring_Model> getRecurringByCategoryId(int categoryId) {
        ArrayList<Expense_Recurring_Model> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + SQLite_Campus.DB_TABLE_EXPENSE_RECURRING +
                        " WHERE " + SQLite_Campus.COL_EXP_RECURRING_CATEGORY_ID + " = ?",
                new String[]{String.valueOf(categoryId)}
        );

        if (cursor != null && cursor.moveToFirst()) {
            DateTimeFormatter formatter = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            }

            do {
                LocalDateTime createAt = null;
                LocalDateTime updateAt = null;
                LocalDateTime startDate = null;
                LocalDateTime endDate = null;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && formatter != null) {
                    String createStr = cursor.getString(cursor.getColumnIndex(SQLite_Campus.COL_EXP_RECURRING_CREATED_AT));
                    String updateStr = cursor.getString(cursor.getColumnIndex(SQLite_Campus.COL_EXP_RECURRING_UPDATED_AT));
                    String startStr = cursor.getString(cursor.getColumnIndex(SQLite_Campus.COL_EXP_RECURRING_START_DATE));
                    String endStr = cursor.getString(cursor.getColumnIndex(SQLite_Campus.COL_EXP_RECURRING_END_DATE));

                    if (createStr != null && !createStr.isEmpty())
                        createAt = LocalDateTime.parse(createStr, formatter);
                    if (updateStr != null && !updateStr.isEmpty())
                        updateAt = LocalDateTime.parse(updateStr, formatter);
                    if (startStr != null && !startStr.isEmpty())
                        startDate = LocalDateTime.parse(startStr, formatter);
                    if (endStr != null && !endStr.isEmpty())
                        endDate = LocalDateTime.parse(endStr, formatter);
                }

                Expense_Recurring_Model model = new Expense_Recurring_Model(
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
                );

                list.add(model);
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return list;
    }



}
