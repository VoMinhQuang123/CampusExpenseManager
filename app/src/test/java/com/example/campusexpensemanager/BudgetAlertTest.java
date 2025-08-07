package com.example.campusexpensemanager;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Test cases for Budget Alert and Notification Logic
 */
public class BudgetAlertTest {
    
    private double monthlyBudget;
    private List<Double> expenses;
    
    @Before
    public void setUp() {
        monthlyBudget = 1000.0;
        expenses = Arrays.asList(200.0, 150.0, 300.0, 100.0);
    }
    
    @Test
    public void testBudgetWarningAlert() {
        // Test 80% threshold warning
        double totalExpenses = 800.0; // 80% of 1000
        assertTrue(shouldShowWarningAlert(monthlyBudget, totalExpenses, 0.8));
        
        // Test below threshold
        totalExpenses = 700.0; // 70% of 1000
        assertFalse(shouldShowWarningAlert(monthlyBudget, totalExpenses, 0.8));
        
        // Test exactly at threshold
        totalExpenses = 800.0;
        assertTrue(shouldShowWarningAlert(monthlyBudget, totalExpenses, 0.8));
    }
    
    @Test
    public void testBudgetExceededAlert() {
        // Test budget exceeded
        double totalExpenses = 1100.0;
        assertTrue(isBudgetExceeded(monthlyBudget, totalExpenses));
        
        // Test budget not exceeded
        totalExpenses = 900.0;
        assertFalse(isBudgetExceeded(monthlyBudget, totalExpenses));
        
        // Test exactly at budget
        totalExpenses = 1000.0;
        assertFalse(isBudgetExceeded(monthlyBudget, totalExpenses));
    }
    
    @Test
    public void testBudgetRemainingCalculation() {
        double totalExpenses = calculateTotalExpenses(expenses);
        double remaining = calculateBudgetRemaining(monthlyBudget, totalExpenses);
        assertEquals(250.0, remaining, 0.01);
        
        // Test with exceeded budget
        expenses = Arrays.asList(400.0, 300.0, 200.0, 200.0);
        totalExpenses = calculateTotalExpenses(expenses);
        remaining = calculateBudgetRemaining(monthlyBudget, totalExpenses);
        assertEquals(-100.0, remaining, 0.01);
    }
    
    @Test
    public void testAlertSeverityLevel() {
        // Test different severity levels
        assertEquals("LOW", getAlertSeverity(monthlyBudget, 500.0)); // 50%
        assertEquals("MEDIUM", getAlertSeverity(monthlyBudget, 750.0)); // 75%
        assertEquals("HIGH", getAlertSeverity(monthlyBudget, 900.0)); // 90%
        assertEquals("CRITICAL", getAlertSeverity(monthlyBudget, 1100.0)); // 110%
        assertEquals("NONE", getAlertSeverity(monthlyBudget, 400.0)); // 40%
    }
    
    @Test
    public void testCategoryBudgetAlert() {
        Map<String, Double> categoryBudgets = new HashMap<>();
        categoryBudgets.put("Food", 300.0);
        categoryBudgets.put("Transportation", 200.0);
        categoryBudgets.put("Entertainment", 150.0);
        
        Map<String, Double> categoryExpenses = new HashMap<>();
        categoryExpenses.put("Food", 280.0); // 93% - should alert
        categoryExpenses.put("Transportation", 100.0); // 50% - no alert
        categoryExpenses.put("Entertainment", 160.0); // 107% - exceeded
        
        assertTrue(shouldAlertForCategory("Food", categoryBudgets, categoryExpenses, 0.9));
        assertFalse(shouldAlertForCategory("Transportation", categoryBudgets, categoryExpenses, 0.9));
        assertTrue(shouldAlertForCategory("Entertainment", categoryBudgets, categoryExpenses, 0.9));
    }
    
    @Test
    public void testDailySpendingLimit() {
        int daysInMonth = 30;
        double dailyLimit = calculateDailySpendingLimit(monthlyBudget, daysInMonth);
        assertEquals(33.33, dailyLimit, 0.01);
        
        // Test with remaining budget and days
        double remainingBudget = 600.0;
        int remainingDays = 15;
        dailyLimit = calculateDailySpendingLimit(remainingBudget, remainingDays);
        assertEquals(40.0, dailyLimit, 0.01);
    }
    
    @Test
    public void testWeeklyBudgetProjection() {
        double weeklySpent = 150.0;
        double projectedMonthly = projectMonthlySpending(weeklySpent);
        assertEquals(650.0, projectedMonthly, 0.01); // 150 * 52 / 12
        
        boolean willExceedBudget = willExceedMonthlyBudget(monthlyBudget, projectedMonthly);
        assertFalse(willExceedBudget);
        
        weeklySpent = 250.0;
        projectedMonthly = projectMonthlySpending(weeklySpent);
        willExceedBudget = willExceedMonthlyBudget(monthlyBudget, projectedMonthly);
        assertTrue(willExceedBudget);
    }
    
    // Helper methods
    private double calculateTotalExpenses(List<Double> expenses) {
        return expenses.stream().mapToDouble(Double::doubleValue).sum();
    }
    
    private boolean shouldShowWarningAlert(double budget, double expenses, double threshold) {
        return (expenses / budget) >= threshold;
    }
    
    private boolean isBudgetExceeded(double budget, double expenses) {
        return expenses > budget;
    }
    
    private double calculateBudgetRemaining(double budget, double expenses) {
        return budget - expenses;
    }
    
    private String getAlertSeverity(double budget, double expenses) {
        double percentage = (expenses / budget);
        if (percentage >= 1.0) return "CRITICAL";
        if (percentage >= 0.9) return "HIGH";
        if (percentage >= 0.75) return "MEDIUM";
        if (percentage >= 0.6) return "LOW";
        return "NONE";
    }
    
    private boolean shouldAlertForCategory(String category, Map<String, Double> budgets, 
                                         Map<String, Double> expenses, double threshold) {
        double budget = budgets.getOrDefault(category, 0.0);
        double expense = expenses.getOrDefault(category, 0.0);
        return budget > 0 && (expense / budget) >= threshold;
    }
    
    private double calculateDailySpendingLimit(double budget, int days) {
        return days > 0 ? budget / days : 0.0;
    }
    
    private double projectMonthlySpending(double weeklySpending) {
        return weeklySpending * 52 / 12; // 52 weeks per year, 12 months per year
    }
    
    private boolean willExceedMonthlyBudget(double budget, double projectedSpending) {
        return projectedSpending > budget;
    }
}
