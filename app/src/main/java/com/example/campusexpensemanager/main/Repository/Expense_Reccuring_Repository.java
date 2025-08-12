package com.example.campusexpensemanager.main.Repository;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.Nullable;

import com.example.campusexpensemanager.main.Database.SQLite_Campus;
import com.example.campusexpensemanager.main.Model.Expense_Recurring_Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Expense_Reccuring_Repository extends SQLite_Campus {

    @SuppressLint("NewApi")
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd yyyy-MM-dd HH:mm:ss");

    public Expense_Reccuring_Repository(@Nullable Context context) {
        super(context);
    }
    private LocalDateTime parseDateSafe(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) return null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return null;

        DateTimeFormatter[] formatters = new DateTimeFormatter[]{
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
        };

        for (DateTimeFormatter fmt : formatters) {
            try {
                if (dateStr.length() == 10) { // chỉ có ngày
                    return LocalDate.parse(dateStr, fmt).atStartOfDay();
                } else { // có giờ phút giây
                    return LocalDateTime.parse(dateStr, fmt);
                }
            } catch (Exception ignored) {}
        }
        return null;
    }
    @SuppressLint("Range")
    public ArrayList<Expense_Recurring_Model> getListRecurring(int userID) {
        ArrayList<Expense_Recurring_Model> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + SQLite_Campus.DB_TABLE_EXPENSE_RECURRING +
                        " WHERE " + SQLite_Campus.COL_EXP_RECURRING_USER_ID + " = ?",
                new String[]{String.valueOf(userID)}
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(new Expense_Recurring_Model(
                        cursor.getInt(cursor.getColumnIndex(SQLite_Campus.COL_EXP_RECURRING_ID)),
                        cursor.getString(cursor.getColumnIndex(SQLite_Campus.COL_EXP_RECURRING_NAME)),
                        cursor.getDouble(cursor.getColumnIndex(SQLite_Campus.COL_EXP_RECURRING_EXPENSE)),
                        cursor.getString(cursor.getColumnIndex(SQLite_Campus.COL_EXP_RECURRING_NOTE)),
                        parseDateSafe(cursor.getString(cursor.getColumnIndex(SQLite_Campus.COL_EXP_RECURRING_CREATED_AT))),
                        parseDateSafe(cursor.getString(cursor.getColumnIndex(SQLite_Campus.COL_EXP_RECURRING_UPDATED_AT))),
                        cursor.getInt(cursor.getColumnIndex(SQLite_Campus.COL_EXP_RECURRING_REPEAT_INTERVAL)),
                        parseDateSafe(cursor.getString(cursor.getColumnIndex(SQLite_Campus.COL_EXP_RECURRING_START_DATE))),
                        parseDateSafe(cursor.getString(cursor.getColumnIndex(SQLite_Campus.COL_EXP_RECURRING_END_DATE))),
                        cursor.getInt(cursor.getColumnIndex(SQLite_Campus.COL_EXP_RECURRING_CATEGORY_ID)),
                        cursor.getInt(cursor.getColumnIndex(SQLite_Campus.COL_EXP_RECURRING_USER_ID))
                ));
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        return list;
    }
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
            do {
                list.add(new Expense_Recurring_Model(
                        cursor.getInt(cursor.getColumnIndex(SQLite_Campus.COL_EXP_RECURRING_ID)),
                        cursor.getString(cursor.getColumnIndex(SQLite_Campus.COL_EXP_RECURRING_NAME)),
                        cursor.getDouble(cursor.getColumnIndex(SQLite_Campus.COL_EXP_RECURRING_EXPENSE)),
                        cursor.getString(cursor.getColumnIndex(SQLite_Campus.COL_EXP_RECURRING_NOTE)),
                        parseDateSafe(cursor.getString(cursor.getColumnIndex(SQLite_Campus.COL_EXP_RECURRING_CREATED_AT))),
                        parseDateSafe(cursor.getString(cursor.getColumnIndex(SQLite_Campus.COL_EXP_RECURRING_UPDATED_AT))),
                        cursor.getInt(cursor.getColumnIndex(SQLite_Campus.COL_EXP_RECURRING_REPEAT_INTERVAL)),
                        parseDateSafe(cursor.getString(cursor.getColumnIndex(SQLite_Campus.COL_EXP_RECURRING_START_DATE))),
                        parseDateSafe(cursor.getString(cursor.getColumnIndex(SQLite_Campus.COL_EXP_RECURRING_END_DATE))),
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
                                String startDate, String endDate, int categoryId, int userID) {

        String currentDate = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            currentDate = DATE_FORMAT.format(ZonedDateTime.now());
        }

        ContentValues values = new ContentValues();
        values.put(SQLite_Campus.COL_EXP_RECURRING_NAME, name);
        values.put(SQLite_Campus.COL_EXP_RECURRING_EXPENSE, expense);
        values.put(SQLite_Campus.COL_EXP_RECURRING_NOTE, note);
        values.put(SQLite_Campus.COL_EXP_RECURRING_CREATED_AT, currentDate);
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
    public long editRecurring(int id, @Nullable String name, @Nullable Double expense, @Nullable String note,
                              @Nullable Integer repeatDays, @Nullable String start, @Nullable String end,
                              @Nullable Integer categoryId, @Nullable Integer userId) {

        String currentDate = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            currentDate = DATE_FORMAT.format(ZonedDateTime.now());
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        if (name != null) values.put(SQLite_Campus.COL_EXP_RECURRING_NAME, name);
        if (expense != null) values.put(SQLite_Campus.COL_EXP_RECURRING_EXPENSE, expense);
        if (note != null) values.put(SQLite_Campus.COL_EXP_RECURRING_NOTE, note);
        if (repeatDays != null) values.put(SQLite_Campus.COL_EXP_RECURRING_REPEAT_INTERVAL, repeatDays);
        if (start != null) values.put(SQLite_Campus.COL_EXP_RECURRING_START_DATE, start);
        if (end != null) values.put(SQLite_Campus.COL_EXP_RECURRING_END_DATE, end);
        if (categoryId != null) values.put(SQLite_Campus.COL_EXP_RECURRING_CATEGORY_ID, categoryId);
        if (userId != null) values.put(SQLite_Campus.COL_EXP_RECURRING_USER_ID, userId);

        values.put(SQLite_Campus.COL_EXP_RECURRING_UPDATED_AT, currentDate);

        String condition = SQLite_Campus.COL_EXP_RECURRING_ID + "=?";
        String[] args = { String.valueOf(id) };

        long result = db.update(SQLite_Campus.DB_TABLE_EXPENSE_RECURRING, values, condition, args);
        db.close();
        return result;
    }
    public long deleteRecurringById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String condition = SQLite_Campus.COL_EXP_RECURRING_ID + "=?";
        String[] args = { String.valueOf(id) };

        long result = db.delete(SQLite_Campus.DB_TABLE_EXPENSE_RECURRING, condition, args);
        db.close();
        return result;
    }

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
}
