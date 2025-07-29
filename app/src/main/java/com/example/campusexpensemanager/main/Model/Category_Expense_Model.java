package com.example.campusexpensemanager.main.Model;

import android.content.Context;

import java.time.LocalDateTime;

public class Category_Expense_Model {
    private int id;
    private String name;
    private int budget;
    private String description;
    private LocalDateTime create_at;
    private LocalDateTime update_at;
    private int userID;

    public Category_Expense_Model(int _id, String _name, int _budget, String _descriptions, LocalDateTime _create_at, LocalDateTime _update_at, int _userID){
        this.id          = _id;
        this.name        = _name;
        this.budget      = _budget;
        this.description = _descriptions;
        this.create_at   = _create_at;
        this.update_at   = _update_at;
        this.userID      = _userID;
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
    public int getBudget() {
        return budget;
    }
    public void setBudget(int budget) {
        this.budget = budget;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
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
    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
}
