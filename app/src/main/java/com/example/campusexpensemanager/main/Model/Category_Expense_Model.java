package com.example.campusexpensemanager.main.Model;

public class Category_Expense_Model {
    private int id;
    private String name;
    private int budget;
    private String description;
    private String create_at;
    private String update_at;
    private int userID;

    public Category_Expense_Model(int _id, String _name, int _budget, String _descriptions, String _create_at, String _update_at, int _userID){
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
    public String getCreate_at() {
        return create_at;
    }
    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }
    public String getUpdate_at() {
        return update_at;
    }
    public void setUpdate_at(String update_at) {
        update_at = update_at;
    }
    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }

}
