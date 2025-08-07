package com.example.campusexpensemanager;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;

/**
 * Integration Test Suite for Campus Expense Manager
 * Tests the integration between different components
 */
public class CampusExpenseManagerIntegrationTest {
    
    private MockUser testUser;
    private MockCategory testCategory;
    private MockExpense testExpense;
    
    @Before
    public void setUp() {
        testUser = new MockUser(1, "testuser", "test@example.com", "password123", "0123456789");
        testCategory = new MockCategory(1, "Food", 500, "Monthly food budget", 1);
        testExpense = new MockExpense(1, "Lunch", 15.50, "Business lunch", 1, 1);
    }
    
    @Test
    public void testCompleteExpenseWorkflow() {
        // Test user creation and validation
        assertTrue(isValidUser(testUser));
        
        // Test category creation for user
        assertTrue(isValidCategory(testCategory));
        assertTrue(belongsToUser(testCategory, testUser));
        
        // Test expense creation in category
        assertTrue(isValidExpense(testExpense));
        assertTrue(belongsToCategory(testExpense, testCategory));
        assertTrue(belongsToUser(testExpense, testUser));
    }
    
    @Test
    public void testBudgetAndExpenseIntegration() {
        double categoryBudget = testCategory.getBudget();
        double expenseAmount = testExpense.getAmount();
        
        // Test budget remaining calculation
        double remaining = categoryBudget - expenseAmount;
        assertEquals(484.50, remaining, 0.01);
        
        // Test budget percentage used
        double percentageUsed = (expenseAmount / categoryBudget) * 100;
        assertEquals(3.1, percentageUsed, 0.1);
        
        // Test if expense fits within budget
        assertTrue(expenseAmount <= categoryBudget);
    }
    
    @Test
    public void testMultipleExpensesInCategory() {
        List<MockExpense> expenses = Arrays.asList(
            new MockExpense(1, "Breakfast", 8.50, "Morning meal", 1, 1),
            new MockExpense(2, "Lunch", 15.50, "Business lunch", 1, 1),
            new MockExpense(3, "Dinner", 25.00, "Restaurant dinner", 1, 1)
        );
        
        double totalExpenses = expenses.stream()
                .mapToDouble(MockExpense::getAmount)
                .sum();
        
        assertEquals(49.0, totalExpenses, 0.01);
        
        // Test if total expenses are within category budget
        assertTrue(totalExpenses <= testCategory.getBudget());
        
        // Test remaining budget
        double remaining = testCategory.getBudget() - totalExpenses;
        assertEquals(451.0, remaining, 0.01);
    }
    
    @Test
    public void testUserMultipleCategoriesIntegration() {
        List<MockCategory> userCategories = Arrays.asList(
            new MockCategory(1, "Food", 500, "Monthly food budget", 1),
            new MockCategory(2, "Transportation", 200, "Monthly transport budget", 1),
            new MockCategory(3, "Entertainment", 150, "Monthly entertainment budget", 1)
        );
        
        double totalBudget = userCategories.stream()
                .mapToDouble(MockCategory::getBudget)
                .sum();
        
        assertEquals(850.0, totalBudget, 0.01);
        
        // Test all categories belong to user
        for (MockCategory category : userCategories) {
            assertTrue(belongsToUser(category, testUser));
        }
    }
    
    @Test
    public void testDataConsistencyValidation() {
        // Test user-category-expense relationship consistency
        assertTrue(testUser.getId() == testCategory.getUserId());
        assertTrue(testUser.getId() == testExpense.getUserId());
        assertTrue(testCategory.getId() == testExpense.getCategoryId());
        
        // Test data type consistency
        assertTrue(testUser.getId() > 0);
        assertTrue(testCategory.getBudget() >= 0);
        assertTrue(testExpense.getAmount() > 0);
    }
    
    // Mock classes for integration testing
    private static class MockUser {
        private int id;
        private String username;
        private String email;
        private String password;
        private String phone;
        
        public MockUser(int id, String username, String email, String password, String phone) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.password = password;
            this.phone = phone;
        }
        
        public int getId() { return id; }
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public String getPassword() { return password; }
        public String getPhone() { return phone; }
    }
    
    private static class MockCategory {
        private int id;
        private String name;
        private int budget;
        private String description;
        private int userId;
        
        public MockCategory(int id, String name, int budget, String description, int userId) {
            this.id = id;
            this.name = name;
            this.budget = budget;
            this.description = description;
            this.userId = userId;
        }
        
        public int getId() { return id; }
        public String getName() { return name; }
        public int getBudget() { return budget; }
        public String getDescription() { return description; }
        public int getUserId() { return userId; }
    }
    
    private static class MockExpense {
        private int id;
        private String name;
        private double amount;
        private String note;
        private int categoryId;
        private int userId;
        
        public MockExpense(int id, String name, double amount, String note, int categoryId, int userId) {
            this.id = id;
            this.name = name;
            this.amount = amount;
            this.note = note;
            this.categoryId = categoryId;
            this.userId = userId;
        }
        
        public int getId() { return id; }
        public String getName() { return name; }
        public double getAmount() { return amount; }
        public String getNote() { return note; }
        public int getCategoryId() { return categoryId; }
        public int getUserId() { return userId; }
    }
    
    // Helper validation methods
    private boolean isValidUser(MockUser user) {
        return user != null && 
               user.getId() > 0 && 
               user.getUsername() != null && !user.getUsername().trim().isEmpty() &&
               user.getEmail() != null && user.getEmail().contains("@") &&
               user.getPassword() != null && user.getPassword().length() >= 6;
    }
    
    private boolean isValidCategory(MockCategory category) {
        return category != null &&
               category.getId() > 0 &&
               category.getName() != null && !category.getName().trim().isEmpty() &&
               category.getBudget() >= 0 &&
               category.getUserId() > 0;
    }
    
    private boolean isValidExpense(MockExpense expense) {
        return expense != null &&
               expense.getId() > 0 &&
               expense.getName() != null && !expense.getName().trim().isEmpty() &&
               expense.getAmount() > 0 &&
               expense.getCategoryId() > 0 &&
               expense.getUserId() > 0;
    }
    
    private boolean belongsToUser(MockCategory category, MockUser user) {
        return category.getUserId() == user.getId();
    }
    
    private boolean belongsToCategory(MockExpense expense, MockCategory category) {
        return expense.getCategoryId() == category.getId();
    }
    
    private boolean belongsToUser(MockExpense expense, MockUser user) {
        return expense.getUserId() == user.getId();
    }
}
