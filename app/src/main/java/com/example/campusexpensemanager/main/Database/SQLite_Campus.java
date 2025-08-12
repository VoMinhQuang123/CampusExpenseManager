package com.example.campusexpensemanager.main.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLite_Campus extends SQLiteOpenHelper {

    private static final String DB_Name = "Campus_Expensive";

    private static final int DB_version = 5;

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
    protected static final String COL_BUDGET_USERID = "userID";

    // table expense_tracking
    protected static final String DB_TABLE_EXPENSE_TRACKING = "expense_tracking";
    protected static final String COL_EXP_TRACKING_ID = "id_Tracking";
    protected static final String COL_EXP_TRACKING_NAME = "name";
    protected static final String COL_EXP_TRACKING_EXPENSE = "expense";
    protected static final String COL_EXP_TRACKING_NOTE = "note";
    protected static final String COL_EXP_TRACKING_CREATED_AT = "created_at";
    protected static final String COL_EXP_TRACKING_UPDATED_AT = "update_at";
    protected static final String COL_EXP_TRACKING_CATEGORY_ID = "categoryId";
    protected static final String COL_EXP_TRACKING_USER_ID = "userID";

    // table expense_recurring
    protected static final String DB_TABLE_EXPENSE_RECURRING = "expense_recurring";
    protected static final String COL_EXP_RECURRING_ID = "id_Recurring";
    protected static final String COL_EXP_RECURRING_NAME = "name";
    protected static final String COL_EXP_RECURRING_EXPENSE = "expense";
    protected static final String COL_EXP_RECURRING_NOTE = "note";
    protected static final String COL_EXP_RECURRING_CREATED_AT = "created_at";
    protected static final String COL_EXP_RECURRING_UPDATED_AT = "update_at";
    protected static final String COL_EXP_RECURRING_REPEAT_INTERVAL = "repeatInterval";
    protected static final String COL_EXP_RECURRING_START_DATE = "start_date";
    protected static final String COL_EXP_RECURRING_END_DATE = "end_date";
    protected static final String COL_EXP_RECURRING_CATEGORY_ID = "categoryId";
    protected static final String COL_EXP_RECURRING_USER_ID = "userID";

    public SQLite_Campus(@Nullable Context context) {
        super(context, DB_Name, null, DB_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng User
        String CreateUserTable = "CREATE TABLE "
                + DB_table_user + " ( "
                + COl_User_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_User_USERNAME + " VARCHAR(200) NOT NULL, "
                + COL_User_Password + " VARCHAR(200) NOT NULL, "
                + COL_User_Email + " VARCHAR(100) NOT NULL, "
                + COL_User_Phone + " VARCHAR(10), "
                + COL_Create_at + " DATETIME, "
                + COL_Update_at + " DATETIME ) ";
        db.execSQL(CreateUserTable);

        // Tạo bảng Budget
        String CreateBudgetTable = "CREATE TABLE "
                + DB_TABLE_BUDGET + "("
                + COL_BUDGET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_BUDGET_NAME + " VARCHAR(200) NOT NULL,"
                + COL_BUDGET_EXPENSIVE + " INTEGER NOT NULL,"
                + COL_BUDGET_DESCRIPTION + " TEXT,"
                + COL_Create_at + " DATETIME,"
                + COL_Update_at + " DATETIME,"
                + COL_BUDGET_USERID + " INTEGER NOT NULL, "
                + " FOREIGN KEY(" + COL_BUDGET_USERID + ") REFERENCES " + DB_table_user + "(" + COl_User_ID + ")"
                + " )";
        db.execSQL(CreateBudgetTable);

        // Chèn mặc định category Other với id = 1
        String insertDefaultCategory = "INSERT INTO " + DB_TABLE_BUDGET +
                " (" + COL_BUDGET_ID + ", " + COL_BUDGET_NAME + ", " + COL_BUDGET_EXPENSIVE + ", " + COL_BUDGET_DESCRIPTION + ", " + COL_Create_at + ", " + COL_Update_at + ", " + COL_BUDGET_USERID + ") " +
                "VALUES (1, 'Other', 0, 'Default category', datetime('now'), datetime('now'), 0);";
        db.execSQL(insertDefaultCategory);

        // Tạo bảng Expense Tracking
        String CreateExpenseTrackingTable = "CREATE TABLE " + DB_TABLE_EXPENSE_TRACKING + " ("
                + COL_EXP_TRACKING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_EXP_TRACKING_NAME + " TEXT NOT NULL, "
                + COL_EXP_TRACKING_EXPENSE + " REAL NOT NULL, "
                + COL_EXP_TRACKING_NOTE + " TEXT, "
                + COL_EXP_TRACKING_CREATED_AT + " DATETIME, "
                + COL_EXP_TRACKING_UPDATED_AT + " DATETIME, "
                + COL_EXP_TRACKING_CATEGORY_ID + " INTEGER DEFAULT 1, "
                + COL_EXP_TRACKING_USER_ID + " INTEGER, "
                + "FOREIGN KEY(" + COL_EXP_TRACKING_USER_ID + ") REFERENCES " + DB_table_user + "(" + COl_User_ID + "), "
                + "FOREIGN KEY(" + COL_EXP_TRACKING_CATEGORY_ID + ") REFERENCES " + DB_TABLE_BUDGET + "(" + COL_BUDGET_ID + ")"
                + ")";
        db.execSQL(CreateExpenseTrackingTable);

        // Tạo bảng Expense Recurring
        String CreateExpenseRecurringTable = "CREATE TABLE " + DB_TABLE_EXPENSE_RECURRING + " ("
                + COL_EXP_RECURRING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_EXP_RECURRING_NAME + " TEXT NOT NULL, "
                + COL_EXP_RECURRING_EXPENSE + " REAL NOT NULL, "
                + COL_EXP_RECURRING_NOTE + " TEXT, "
                + COL_EXP_RECURRING_CREATED_AT + " DATETIME, "
                + COL_EXP_RECURRING_UPDATED_AT + " DATETIME, "
                + COL_EXP_RECURRING_REPEAT_INTERVAL + " INTEGER NOT NULL, "
                + COL_EXP_RECURRING_START_DATE + " DATETIME, "
                + COL_EXP_RECURRING_END_DATE + " DATETIME, "
                + COL_EXP_RECURRING_CATEGORY_ID + " INTEGER DEFAULT 1, "
                + COL_EXP_RECURRING_USER_ID + " INTEGER, "
                + "FOREIGN KEY(" + COL_EXP_RECURRING_USER_ID + ") REFERENCES " + DB_table_user + "(" + COl_User_ID + "), "
                + "FOREIGN KEY(" + COL_EXP_RECURRING_CATEGORY_ID + ") REFERENCES " + DB_TABLE_BUDGET + "(" + COL_BUDGET_ID + ")"
                + ")";
        db.execSQL(CreateExpenseRecurringTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DB_table_user);
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_BUDGET);
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_EXPENSE_TRACKING);
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_EXPENSE_RECURRING);
            onCreate(db);
        }
    }
}
