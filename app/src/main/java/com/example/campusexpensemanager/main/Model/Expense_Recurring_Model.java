package com.example.campusexpensemanager.main.Model;

import java.time.LocalDateTime;

public class Expense_Recurring_Model {
    private int id;
    private String name;
    private double expense;
    private String note;
    private LocalDateTime create_at;
    private LocalDateTime update_at;
    private int repeatInterval;
    private LocalDateTime start_date;
    private LocalDateTime end_date;
    private int categoryId;
    private int userID;

    public Expense_Recurring_Model(int _id, String _name, double _expense, String _note, LocalDateTime _create_at, LocalDateTime _update_at, int _repeatInterval, LocalDateTime _start_date, LocalDateTime _end_date, int _categoryId, int _userID) {
        this.id             = _id;
        this.name           = _name;
        this.expense        = _expense;
        this.note           = _note;
        this.create_at      = _create_at;
        this.update_at      = _update_at;
        this.repeatInterval = _repeatInterval;
        this.start_date     = _start_date;
        this.end_date       = _end_date;
        this.categoryId     = _categoryId;
        this.userID         = _userID;
    }

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
    public int getRepeatInterval() {
        return repeatInterval;
    }
    public void setRepeatInterval(int repeatInterval) {
        this.repeatInterval = repeatInterval;
    }
    public LocalDateTime getStart_date() {
        return start_date;
    }
    public void setStart_date(LocalDateTime start_date) {
        this.start_date = start_date;
    }
    public LocalDateTime getEnd_date() {
        return end_date;
    }
    public void setEnd_date(LocalDateTime end_date) {
        this.end_date = end_date;
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
