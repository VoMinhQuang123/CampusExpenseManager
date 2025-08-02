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
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Category_Expense_Repository extends SQLite_Campus {
    public Category_Expense_Repository(@Nullable Context context) {
        super(context);
    }
    @SuppressLint("Range")
    public ArrayList<Category_Expense_Model> getListBudget(int userID) {
        ArrayList<Category_Expense_Model> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery( "SELECT * FROM " + SQLite_Campus.DB_TABLE_BUDGET +
                        " WHERE " + SQLite_Campus.COL_BUDGET_USERID + " = ?",
                new String[]{String.valueOf(userID)});


        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                DateTimeFormatter formatter = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                }

                do {
                    LocalDateTime createAt = null;
                    LocalDateTime updateAt = null;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && formatter != null) {
                        String createStr = cursor.getString(cursor.getColumnIndex(SQLite_Campus.COL_Create_at));
                        String updateStr = cursor.getString(cursor.getColumnIndex(SQLite_Campus.COL_Update_at));

                        if (createStr != null && !createStr.isEmpty()) {
                            createAt = LocalDateTime.parse(createStr, formatter);
                        }

                        if (updateStr != null && !updateStr.isEmpty()) {
                            updateAt = LocalDateTime.parse(updateStr, formatter);
                        }
                    }
                    arrayList.add(new Category_Expense_Model(
                            cursor.getInt(cursor.getColumnIndex(SQLite_Campus.COL_BUDGET_ID)),
                            cursor.getString(cursor.getColumnIndex(SQLite_Campus.COL_BUDGET_NAME)),
                            cursor.getInt(cursor.getColumnIndex(SQLite_Campus.COL_BUDGET_EXPENSIVE)),
                            cursor.getString(cursor.getColumnIndex(SQLite_Campus.COL_BUDGET_DESCRIPTION)),
                            createAt,
                            updateAt,
                            cursor.getInt(cursor.getColumnIndex(SQLite_Campus.COL_BUDGET_USERID))
                    ));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        db.close();
        return arrayList;
    }
    public long addNewBudget(String name, int expensive, String description, int userID){
        @SuppressLint({"NewApi", "LocalSuppress"}) DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        @SuppressLint({"NewApi", "LocalSuppress"}) ZonedDateTime zone = ZonedDateTime.now();

        @SuppressLint({"NewApi", "LocalSuppress"}) String CurrentDate = dtf.format(zone);

        ContentValues values = new ContentValues();
        values.put(SQLite_Campus.COL_BUDGET_NAME, name);
        values.put(SQLite_Campus.COL_BUDGET_EXPENSIVE, expensive);
        values.put(SQLite_Campus.COL_BUDGET_DESCRIPTION, description);
        values.put(SQLite_Campus.COL_Create_at, CurrentDate);
        values.put(SQLite_Campus.COL_BUDGET_USERID, userID);
        SQLiteDatabase db = this.getReadableDatabase();
        long insert = db.insert(SQLite_Campus.DB_TABLE_BUDGET, null, values);
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
    public int getTotalExpense(int userId) {
        int total = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        // Truy vấn tính tổng cột "expensive"
        Cursor cursor = db.rawQuery("SELECT SUM(" + SQLite_Campus.COL_BUDGET_EXPENSIVE + ") AS Total FROM " + SQLite_Campus.DB_TABLE_BUDGET +
                        " WHERE " + SQLite_Campus.COL_BUDGET_USERID + " = ?",
        new String[]{String.valueOf(userId)}
        );

        if (cursor != null && cursor.moveToFirst()) {
            int colIndex = cursor.getColumnIndex("Total");
            if (colIndex >= 0 && !cursor.isNull(colIndex)) {
                total = cursor.getInt(colIndex);
            }
            cursor.close();
        }
        db.close();
        return total;
    }
    public Map<Integer, Integer> getExpenseByCategory(int userId) {
        Map<Integer, Integer> map = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT " + SQLite_Campus.COL_BUDGET_ID + ", SUM(" + SQLite_Campus.COL_BUDGET_EXPENSIVE + ") as Total " +
                        "FROM " + SQLite_Campus.DB_TABLE_BUDGET +
                        " WHERE " + SQLite_Campus.COL_BUDGET_USERID + " = ?" +
                        " GROUP BY " + SQLite_Campus.COL_BUDGET_ID,
                new String[]{String.valueOf(userId)}
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") int categoryId = cursor.getInt(cursor.getColumnIndex(SQLite_Campus.COL_BUDGET_ID));
                @SuppressLint("Range") int total = cursor.getInt(cursor.getColumnIndex("Total"));
                map.put(categoryId, total);
            }
            cursor.close();
        }
        db.close();
        return map;

    }
    // Lấy danh sách các khoản chi tiêu theo categoryId
    @SuppressLint("Range")
    public ArrayList<Category_Expense_Model> getExpensesByCategoryId(int categoryId) {
        ArrayList<Category_Expense_Model> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + SQLite_Campus.DB_TABLE_BUDGET +
                        " WHERE " + SQLite_Campus.COL_BUDGET_ID + " = ?",
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

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && formatter != null) {
                    String createStr = cursor.getString(cursor.getColumnIndex(SQLite_Campus.COL_Create_at));
                    String updateStr = cursor.getString(cursor.getColumnIndex(SQLite_Campus.COL_Update_at));

                    if (createStr != null && !createStr.isEmpty()) {
                        createAt = LocalDateTime.parse(createStr, formatter);
                    }

                    if (updateStr != null && !updateStr.isEmpty()) {
                        updateAt = LocalDateTime.parse(updateStr, formatter);
                    }
                }

                list.add(new Category_Expense_Model(
                        cursor.getInt(cursor.getColumnIndex(SQLite_Campus.COL_BUDGET_ID)),
                        cursor.getString(cursor.getColumnIndex(SQLite_Campus.COL_BUDGET_NAME)),
                        cursor.getInt(cursor.getColumnIndex(SQLite_Campus.COL_BUDGET_EXPENSIVE)),
                        cursor.getString(cursor.getColumnIndex(SQLite_Campus.COL_BUDGET_DESCRIPTION)),
                        createAt,
                        updateAt,
                        cursor.getInt(cursor.getColumnIndex(SQLite_Campus.COL_BUDGET_USERID))
                ));
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return list;
    }

}
