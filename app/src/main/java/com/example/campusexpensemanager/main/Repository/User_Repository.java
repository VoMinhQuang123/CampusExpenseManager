package com.example.campusexpensemanager.main.Repository;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.campusexpensemanager.main.Database.SQLite_Campus;
import com.example.campusexpensemanager.main.Model.User_Model;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class User_Repository extends SQLite_Campus {
    public User_Repository(@Nullable Context context) {
        super(context);
    }

    public long saveUser(String username, String password, String email, String phone){
        @SuppressLint({"NewApi", "LocalSuppress"}) DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        @SuppressLint({"NewApi", "LocalSuppress"}) ZonedDateTime zone = ZonedDateTime.now();

        @SuppressLint({"NewApi", "LocalSuppress"}) String CurrentDate = dtf.format(zone);

        ContentValues values = new ContentValues();
        values.put(SQLite_Campus.COL_User_USERNAME, username);
        values.put(SQLite_Campus.COL_User_Password, password);
        values.put(SQLite_Campus.COL_User_Email, email);
        values.put(SQLite_Campus.COL_User_Phone, phone);
        values.put(SQLite_Campus.COL_Create_at, CurrentDate);
        SQLiteDatabase db = this.getWritableDatabase();
        long insert = db.insert(SQLite_Campus.DB_table_user, null, values);
        db.close();
        return insert;
    }

    @SuppressLint("Range")
    public  User_Model getInforUserByUsername(String username, String password){
        User_Model user = new User_Model();
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            String[] col = {SQLite_Campus.COl_User_ID,
                    SQLite_Campus.COL_User_USERNAME,
                    SQLite_Campus.COL_User_Email,
                    SQLite_Campus.COL_User_Phone
            };
            String condition = SQLite_Campus.COL_User_USERNAME + " = ? And " + SQLite_Campus.COL_User_Password + " = ? " ;
            String[] params = { username, password};
            Cursor cursor = db.query(SQLite_Campus.DB_table_user, col, condition, params, null, null, null);
            if (cursor.getCount()>0){
                cursor.moveToFirst();
                user.setId(cursor.getInt(cursor.getColumnIndex(SQLite_Campus.COl_User_ID)));
                user.setUsername(cursor.getString(cursor.getColumnIndex(SQLite_Campus.COL_User_USERNAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(SQLite_Campus.COL_User_Email)));
                user.setPhone(cursor.getString(cursor.getColumnIndex(SQLite_Campus.COL_User_Phone)));
            }
            cursor.close();
            db.close();
        }
        catch (RuntimeException e){
            throw new RuntimeException(e);
        }
        return user;
    }
}

