package com.example.campusexpensemanager.main.Repository;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.Nullable;

import com.example.campusexpensemanager.main.Database.SQLite_Campus;
import com.example.campusexpensemanager.main.Model.Expense_Tracking_Model;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Expense_Tracking_Repository extends SQLite_Campus {
    public Expense_Tracking_Repository(@Nullable Context context) {
        super(context);
    }

    @SuppressLint("Range")
    public ArrayList<Expense_Tracking_Model> getListTracking(int userID) {
        ArrayList<Expense_Tracking_Model> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + SQLite_Campus.DB_TABLE_EXPENSE_TRACKING +
                        " WHERE " + SQLite_Campus.COL_EXP_TRACKING_USER_ID + " = ?",
                new String[]{String.valueOf(userID)});

        if (cursor != null && cursor.moveToFirst()) {
            DateTimeFormatter formatter = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            }

            do {
                LocalDateTime createdAt = null, updatedAt = null;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && formatter != null) {
                    String createdStr = cursor.getString(cursor.getColumnIndex(SQLite_Campus.COL_EXP_TRACKING_CREATED_AT));
                    String updatedStr = cursor.getString(cursor.getColumnIndex(SQLite_Campus.COL_EXP_TRACKING_UPDATED_AT));

                    if (createdStr != null && !createdStr.isEmpty()) createdAt = LocalDateTime.parse(createdStr, formatter);
                    if (updatedStr != null && !updatedStr.isEmpty()) updatedAt = LocalDateTime.parse(updatedStr, formatter);
                }

                list.add(new Expense_Tracking_Model(
                        cursor.getInt(cursor.getColumnIndex(SQLite_Campus.COL_EXP_TRACKING_ID)),
                        cursor.getString(cursor.getColumnIndex(SQLite_Campus.COL_EXP_TRACKING_NAME)),
                        cursor.getDouble(cursor.getColumnIndex(SQLite_Campus.COL_EXP_TRACKING_EXPENSE)),
                        cursor.getString(cursor.getColumnIndex(SQLite_Campus.COL_EXP_TRACKING_NOTE)),
                        createdAt,
                        updatedAt,
                        cursor.getInt(cursor.getColumnIndex(SQLite_Campus.COL_EXP_TRACKING_CATEGORY_ID)),
                        cursor.getInt(cursor.getColumnIndex(SQLite_Campus.COL_EXP_TRACKING_USER_ID))
                ));
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return list;
    }

    public long addNewTracking(String name, double expense, String note,
                               int categoryId, int userID) {
        @SuppressLint({"NewApi", "LocalSuppress"}) DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        @SuppressLint({"NewApi", "LocalSuppress"}) ZonedDateTime zone = ZonedDateTime.now();
        @SuppressLint({"NewApi", "LocalSuppress"}) String currentDate = dtf.format(zone);

        ContentValues values = new ContentValues();
        values.put(SQLite_Campus.COL_EXP_TRACKING_NAME, name);
        values.put(SQLite_Campus.COL_EXP_TRACKING_EXPENSE, expense);
        values.put(SQLite_Campus.COL_EXP_TRACKING_NOTE, note);
        values.put(SQLite_Campus.COL_EXP_TRACKING_CREATED_AT, currentDate);
        values.put(SQLite_Campus.COL_EXP_TRACKING_CATEGORY_ID, categoryId);
        values.put(SQLite_Campus.COL_EXP_TRACKING_USER_ID, userID);

        SQLiteDatabase db = this.getWritableDatabase();
        long insert = db.insert(SQLite_Campus.DB_TABLE_EXPENSE_TRACKING, null, values);
        db.close();
        return insert;
    }



}
