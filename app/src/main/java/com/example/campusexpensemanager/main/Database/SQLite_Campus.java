package com.example.campusexpensemanager.main.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLite_Campus extends SQLiteOpenHelper {

    private static final String DB_Name = "Campus_Expensive";

    private static final int DB_version = 3;

    //User table
    protected static final String DB_table_user = "User";
    protected static final String COl_User_ID = "id";
    protected static final String COL_User_USERNAME = "username";
    protected static final String COL_User_Password = "password";
    protected static final String COL_User_Email = "email";
    protected static final String COL_User_Phone = "phone";
    protected static final String COL_Create_at = "created_at";
    protected static final String COL_Update_at = "update_at";

    //table budget
    protected static final String DB_TABLE_BUDGET = "budget";
    protected static final String COL_BUDGET_ID = "ID";
    protected static final String COL_BUDGET_NAME = "name";
    protected static final String COL_BUDGET_EXPENSIVE = "expensive";
    protected static final String COL_BUDGET_DESCRIPTION = "description";
    protected  static  final String COL_BUDGET_USERID = "userID";
    public SQLite_Campus(@Nullable Context context) {
        super(context, DB_Name, null, DB_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // tao bang user
        String CreateUserTable = "CREATE TABLE "
                + DB_table_user     + " ( "
                + COl_User_ID       + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_User_USERNAME + " VARCHAR(200) NOT NULL, "
                + COL_User_Password + " VARCHAR(200) NOT NULL, "
                + COL_User_Email    + " VARCHAR(100) NOT NULL, "
                + COL_User_Phone    + " VARCHAR(10), "
                + COL_Create_at     + " DATETIME, "
                + COL_Update_at     + " DATETIME ) ";
        db.execSQL(CreateUserTable);

        // tạo bảng budget
        String CreateBudgetTable = "CREATE TABLE "
                + DB_TABLE_BUDGET        + "("
                + COL_BUDGET_ID          + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_BUDGET_NAME        + " VARCHAR(200) NOT NULL,"
                + COL_BUDGET_EXPENSIVE   + " INTEGER NOT NULL,"
                + COL_BUDGET_DESCRIPTION + " TEXT,"
                + COL_Create_at          + " DATETIME,"
                + COL_Update_at          + " DATETIME,"
                + COL_BUDGET_USERID      + " INTEGER NOT NULL, "
                + " FOREIGN KEY(" + COL_BUDGET_USERID + ") REFERENCES " + DB_table_user + "(" + COl_User_ID + ")"
                + " )";
        db.execSQL(CreateBudgetTable);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion != newVersion){
            db.execSQL("DROP TABLE IF EXISTS " + DB_table_user);
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_BUDGET);
            onCreate(db);}
    }
}
