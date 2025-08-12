package com.example.campusexpensemanager.main.Model;

import java.time.LocalDateTime;

public class Expense_Tracking_Model {
    private int id;
    private String name;
    private double expense;
    private String note;
    private LocalDateTime create_at;
    private LocalDateTime update_at;
    private int categoryId;
    private int userID;

    public Expense_Tracking_Model(int _id, String _name, double _expense, String _note,
                                  LocalDateTime _create_at, LocalDateTime _update_at,
                                  int _categoryId, int _userID) {
        this.id = _id;
        this.name = _name;
        this.expense = _expense;
        this.note = _note;
        this.create_at = _create_at;
        this.update_at = _update_at;
        this.categoryId = _categoryId;
        this.userID = _userID;
    }

    // Getter và Setter
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getExpense() {
        return expense;
    }
    public void setExpense(double expense) {
        this.expense = expense;
    }
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public LocalDateTime getCreate_at() {
        return create_at;
    }
    public void setCreate_at(LocalDateTime create_at) {
        this.create_at = create_at;
    }
    public LocalDateTime getUpdate_at() {
        return update_at;
    }
    public void setUpdate_at(LocalDateTime update_at) {
        this.update_at = update_at;
    }
    public int getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
}
