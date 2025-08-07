package com.example.campusexpensemanager;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Test cases for Expense Calculation Logic
 */
public class ExpenseCalculationTest {
    
    private List<Double> sampleExpenses;
    
    @Before
    public void setUp() {
        sampleExpenses = Arrays.asList(100.0, 200.0, 150.0, 75.0, 300.0);
    }
    
    @Test
    public void testTotalExpenseCalculation() {
        double total = calculateTotalExpense(sampleExpenses);
        assertEquals(825.0, total, 0.01);
        
        // Test with empty list
        List<Double> emptyList = Arrays.asList();
        assertEquals(0.0, calculateTotalExpense(emptyList), 0.01);
        
        // Test with single expense
        List<Double> singleExpense = Arrays.asList(250.0);
        assertEquals(250.0, calculateTotalExpense(singleExpense), 0.01);
    }
    
    @Test
    public void testAverageExpenseCalculation() {
        double average = calculateAverageExpense(sampleExpenses);
        assertEquals(165.0, average, 0.01);
        
        // Test with single expense
        List<Double> singleExpense = Arrays.asList(100.0);
        assertEquals(100.0, calculateAverageExpense(singleExpense), 0.01);
    }
    
    @Test
    public void testBudgetRemainingCalculation() {
        double budget = 1000.0;
        double totalExpense = calculateTotalExpense(sampleExpenses);
        double remaining = calculateBudgetRemaining(budget, totalExpense);
        assertEquals(175.0, remaining, 0.01);
        
        // Test when expenses exceed budget
        double smallBudget = 500.0;
        double overBudget = calculateBudgetRemaining(smallBudget, totalExpense);
        assertEquals(-325.0, overBudget, 0.01);
    }
    
    @Test
    public void testBudgetPercentageUsed() {
        double budget = 1000.0;
        double totalExpense = calculateTotalExpense(sampleExpenses);
        double percentageUsed = calculateBudgetPercentageUsed(budget, totalExpense);
        assertEquals(82.5, percentageUsed, 0.01);
        
        // Test when expenses exceed budget
        double smallBudget = 500.0;
        double overPercentage = calculateBudgetPercentageUsed(smallBudget, totalExpense);
        assertEquals(165.0, overPercentage, 0.01);
    }
    
    @Test
    public void testExpenseRangeFilter() {
        List<Double> filteredExpenses = filterExpensesByRange(sampleExpenses, 100.0, 200.0);
        assertEquals(3, filteredExpenses.size());
        assertTrue(filteredExpenses.contains(100.0));
        assertTrue(filteredExpenses.contains(200.0));
        assertTrue(filteredExpenses.contains(150.0));
        assertFalse(filteredExpenses.contains(75.0));
        assertFalse(filteredExpenses.contains(300.0));
    }
    
    // Helper methods for calculation logic
    private double calculateTotalExpense(List<Double> expenses) {
        return expenses.stream().mapToDouble(Double::doubleValue).sum();
    }
    
    private double calculateAverageExpense(List<Double> expenses) {
        if (expenses.isEmpty()) return 0.0;
        return calculateTotalExpense(expenses) / expenses.size();
    }
    
    private double calculateBudgetRemaining(double budget, double totalExpense) {
        return budget - totalExpense;
    }
    
    private double calculateBudgetPercentageUsed(double budget, double totalExpense) {
        if (budget == 0) return 0.0;
        return (totalExpense / budget) * 100;
    }
    
    private List<Double> filterExpensesByRange(List<Double> expenses, double min, double max) {
        return expenses.stream()
                .filter(expense -> expense >= min && expense <= max)
                .collect(java.util.stream.Collectors.toList());
    }
}
