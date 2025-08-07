package com.example.campusexpensemanager;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Test cases for Expense Search and Filter Logic
 */
public class ExpenseSearchFilterTest {
    
    private List<MockExpense> sampleExpenses;
    
    @Before
    public void setUp() {
        sampleExpenses = Arrays.asList(
            new MockExpense(1, "Coffee", 5.50, "Food"),
            new MockExpense(2, "Bus ticket", 2.00, "Transportation"),
            new MockExpense(3, "Lunch", 12.00, "Food"),
            new MockExpense(4, "Movie ticket", 10.00, "Entertainment"),
            new MockExpense(5, "Groceries", 45.00, "Food"),
            new MockExpense(6, "Gas", 30.00, "Transportation")
        );
    }
    
    @Test
    public void testSearchByName() {
        List<MockExpense> coffeeResults = searchExpensesByName(sampleExpenses, "Coffee");
        assertEquals(1, coffeeResults.size());
        assertEquals("Coffee", coffeeResults.get(0).getName());
        
        List<MockExpense> ticketResults = searchExpensesByName(sampleExpenses, "ticket");
        assertEquals(2, ticketResults.size());
    }
    
    @Test
    public void testFilterByCategory() {
        List<MockExpense> foodExpenses = filterExpensesByCategory(sampleExpenses, "Food");
        assertEquals(3, foodExpenses.size());
        
        List<MockExpense> transportExpenses = filterExpensesByCategory(sampleExpenses, "Transportation");
        assertEquals(2, transportExpenses.size());
        
        List<MockExpense> nonExistentCategory = filterExpensesByCategory(sampleExpenses, "NonExistent");
        assertEquals(0, nonExistentCategory.size());
    }
    
    @Test
    public void testFilterByAmountRange() {
        List<MockExpense> smallExpenses = filterExpensesByAmountRange(sampleExpenses, 0.0, 10.0);
        assertEquals(3, smallExpenses.size());
        
        List<MockExpense> largeExpenses = filterExpensesByAmountRange(sampleExpenses, 20.0, 100.0);
        assertEquals(2, largeExpenses.size());
        
        List<MockExpense> noExpenses = filterExpensesByAmountRange(sampleExpenses, 100.0, 200.0);
        assertEquals(0, noExpenses.size());
    }
    
    @Test
    public void testCaseInsensitiveSearch() {
        List<MockExpense> results = searchExpensesByName(sampleExpenses, "COFFEE");
        assertEquals(1, results.size());
        
        results = searchExpensesByName(sampleExpenses, "coffee");
        assertEquals(1, results.size());
        
        results = searchExpensesByName(sampleExpenses, "CoFfEe");
        assertEquals(1, results.size());
    }
    
    @Test
    public void testPartialNameSearch() {
        List<MockExpense> results = searchExpensesByName(sampleExpenses, "Cof");
        assertEquals(1, results.size());
        
        results = searchExpensesByName(sampleExpenses, "tic");
        assertEquals(2, results.size());
    }
    
    @Test
    public void testEmptySearchResults() {
        List<MockExpense> results = searchExpensesByName(sampleExpenses, "NonExistent");
        assertTrue(results.isEmpty());
        
        results = searchExpensesByName(sampleExpenses, "");
        assertEquals(sampleExpenses.size(), results.size());
    }
    
    @Test
    public void testCombinedFilters() {
        // Search for food expenses under $10
        List<MockExpense> filteredResults = sampleExpenses.stream()
                .filter(expense -> expense.getCategory().equals("Food"))
                .filter(expense -> expense.getAmount() < 10.0)
                .collect(java.util.stream.Collectors.toList());
        
        assertEquals(1, filteredResults.size());
        assertEquals("Coffee", filteredResults.get(0).getName());
    }
    
    // Mock Expense class for testing
    private static class MockExpense {
        private int id;
        private String name;
        private double amount;
        private String category;
        
        public MockExpense(int id, String name, double amount, String category) {
            this.id = id;
            this.name = name;
            this.amount = amount;
            this.category = category;
        }
        
        public int getId() { return id; }
        public String getName() { return name; }
        public double getAmount() { return amount; }
        public String getCategory() { return category; }
    }
    
    // Helper methods
    private List<MockExpense> searchExpensesByName(List<MockExpense> expenses, String searchTerm) {
        return expenses.stream()
                .filter(expense -> expense.getName().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(java.util.stream.Collectors.toList());
    }
    
    private List<MockExpense> filterExpensesByCategory(List<MockExpense> expenses, String category) {
        return expenses.stream()
                .filter(expense -> expense.getCategory().equals(category))
                .collect(java.util.stream.Collectors.toList());
    }
    
    private List<MockExpense> filterExpensesByAmountRange(List<MockExpense> expenses, double min, double max) {
        return expenses.stream()
                .filter(expense -> expense.getAmount() >= min && expense.getAmount() <= max)
                .collect(java.util.stream.Collectors.toList());
    }
}
