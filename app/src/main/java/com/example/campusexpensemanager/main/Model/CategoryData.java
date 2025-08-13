package com.example.campusexpensemanager.main.Model;

public class CategoryData {
    private String name;         // Tên category
    private int totalSpent;      // Tổng chi tiêu (recurring + tracking)
    private int totalCategory;   // Tổng số tiền category (budget)

    public CategoryData(String name, int totalSpent, int totalCategory) {
        this.name = name;
        this.totalSpent = totalSpent;
        this.totalCategory = totalCategory;
    }

    // getter và setter

    public String getName() {
        return name;
    }

    public int getTotalSpent() {
        return totalSpent;
    }

    public int getTotalCategory() {
        return totalCategory;
    }
}
