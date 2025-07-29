package com.example.campusexpensemanager.main.Repository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.Nullable;
import com.example.campusexpensemanager.main.Database.SQLite_Campus;
import com.example.campusexpensemanager.main.Model.Category_Expense_Model;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class Category_Expense_Repository extends SQLite_Campus {


    public Category_Expense_Repository(@Nullable Context context) {
        super(context);
    }

    @SuppressLint("Range")
    public ArrayList<Category_Expense_Model> getListBudget(){
        ArrayList<Category_Expense_Model> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + SQLite_Campus.DB_TABLE_BUDGET, null);
        if(cursor != null && cursor.getCount() > 0){
            if (cursor.moveToFirst()){
                do{
                    DateTimeFormatter formatter = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        arrayList.add(
                                new Category_Expense_Model(
                                        cursor.getInt(cursor.getColumnIndex(SQLite_Campus.COL_BUDGET_ID)),
                                        cursor.getString(cursor.getColumnIndex(SQLite_Campus.COL_BUDGET_NAME)),
                                        cursor.getInt(cursor.getColumnIndex(SQLite_Campus.COL_BUDGET_EXPENSIVE)),
                                        cursor.getString(cursor.getColumnIndex(SQLite_Campus.COL_BUDGET_DESCRIPTION)),
                                        LocalDateTime.parse(cursor.getString(cursor.getColumnIndex(SQLite_Campus.COL_Create_at)), formatter),
                                        LocalDateTime.parse(cursor.getString(cursor.getColumnIndex(SQLite_Campus.COL_Update_at)), formatter),
                                        cursor.getInt(cursor.getColumnIndex(SQLite_Campus.COL_BUDGET_USERID))
                                )
                        );
                    }
                }
                while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();

        return arrayList;
    }
}
