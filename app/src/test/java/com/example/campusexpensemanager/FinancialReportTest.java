package com.example.campusexpensemanager;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Test cases for Financial Report and Statistics
 */
public class FinancialReportTest {
    
    private List<MockExpenseData> monthlyExpenses;
    private Map<String, List<Double>> categoryExpenses;
    
    @Before
    public void setUp() {
        monthlyExpenses = Arrays.asList(
            new MockExpenseData("2024-01", 800.0),
            new MockExpenseData("2024-02", 750.0),
            new MockExpenseData("2024-03", 900.0),
            new MockExpenseData("2024-04", 650.0),
            new MockExpenseData("2024-05", 1000.0)
        );
        
        categoryExpenses = new HashMap<>();
        categoryExpenses.put("Food", Arrays.asList(200.0, 180.0, 220.0, 150.0, 250.0));
        categoryExpenses.put("Transportation", Arrays.asList(100.0, 120.0, 150.0, 80.0, 200.0));
        categoryExpenses.put("Entertainment", Arrays.asList(150.0, 100.0, 200.0, 120.0, 180.0));
    }
    
    @Test
    public void testMonthlyAverageCalculation() {
        double average = calculateMonthlyAverage(monthlyExpenses);
        assertEquals(820.0, average, 0.01);
    }
    
    @Test
    public void testHighestSpendingMonth() {
        MockExpenseData highest = findHighestSpendingMonth(monthlyExpenses);
        assertEquals("2024-05", highest.getMonth());
        assertEquals(1000.0, highest.getAmount(), 0.01);
    }
    
    @Test
    public void testLowestSpendingMonth() {
        MockExpenseData lowest = findLowestSpendingMonth(monthlyExpenses);
        assertEquals("2024-04", lowest.getMonth());
        assertEquals(650.0, lowest.getAmount(), 0.01);
    }
    
    @Test
    public void testSpendingTrend() {
        String trend = calculateSpendingTrend(monthlyExpenses);
        assertNotNull(trend);
        assertTrue(Arrays.asList("INCREASING", "DECREASING", "STABLE").contains(trend));
    }
    
    @Test
    public void testTopSpendingCategories() {
        Map<String, Double> categoryTotals = calculateCategoryTotals(categoryExpenses);
        List<String> topCategories = getTopSpendingCategories(categoryTotals, 2);
        
        assertEquals(2, topCategories.size());
        assertTrue(topCategories.contains("Food"));
        assertTrue(topCategories.contains("Entertainment"));
    }
    
    @Test
    public void testCategoryPercentageDistribution() {
        Map<String, Double> categoryTotals = calculateCategoryTotals(categoryExpenses);
        Map<String, Double> percentages = calculateCategoryPercentages(categoryTotals);
        
        double totalPercentage = percentages.values().stream().mapToDouble(Double::doubleValue).sum();
        assertEquals(100.0, totalPercentage, 0.01);
        
        assertTrue(percentages.get("Food") > 0);
        assertTrue(percentages.get("Transportation") > 0);
        assertTrue(percentages.get("Entertainment") > 0);
    }
    
    @Test
    public void testMonthOverMonthGrowth() {
        double growth = calculateMonthOverMonthGrowth(
            monthlyExpenses.get(0).getAmount(), 
            monthlyExpenses.get(1).getAmount()
        );
        assertEquals(-6.25, growth, 0.01); // (750-800)/800 * 100
        
        growth = calculateMonthOverMonthGrowth(
            monthlyExpenses.get(3).getAmount(), 
            monthlyExpenses.get(4).getAmount()
        );
        assertEquals(53.85, growth, 0.01); // (1000-650)/650 * 100
    }
    
    @Test
    public void testBudgetVarianceAnalysis() {
        double budgetTarget = 800.0;
        Map<String, Double> variances = calculateBudgetVariances(monthlyExpenses, budgetTarget);
        
        assertEquals(0.0, variances.get("2024-01"), 0.01); // 800 vs 800
        assertEquals(6.25, variances.get("2024-02"), 0.01); // 750 vs 800 = -6.25% (under budget)
        assertEquals(-12.5, variances.get("2024-03"), 0.01); // 900 vs 800 = 12.5% (over budget)
    }
    
    @Test
    public void testSavingsCalculation() {
        double monthlyIncome = 1200.0;
        double averageExpense = calculateMonthlyAverage(monthlyExpenses);
        double savings = calculateMonthlySavings(monthlyIncome, averageExpense);
        
        assertEquals(380.0, savings, 0.01);
        
        double savingsRate = calculateSavingsRate(monthlyIncome, averageExpense);
        assertEquals(31.67, savingsRate, 0.01); // 380/1200 * 100
    }
    
    // Mock class for expense data
    private static class MockExpenseData {
        private String month;
        private double amount;
        
        public MockExpenseData(String month, double amount) {
            this.month = month;
            this.amount = amount;
        }
        
        public String getMonth() { return month; }
        public double getAmount() { return amount; }
    }
    
    // Helper methods
    private double calculateMonthlyAverage(List<MockExpenseData> expenses) {
        return expenses.stream().mapToDouble(MockExpenseData::getAmount).average().orElse(0.0);
    }
    
    private MockExpenseData findHighestSpendingMonth(List<MockExpenseData> expenses) {
        return expenses.stream().max((a, b) -> Double.compare(a.getAmount(), b.getAmount())).orElse(null);
    }
    
    private MockExpenseData findLowestSpendingMonth(List<MockExpenseData> expenses) {
        return expenses.stream().min((a, b) -> Double.compare(a.getAmount(), b.getAmount())).orElse(null);
    }
    
    private String calculateSpendingTrend(List<MockExpenseData> expenses) {
        if (expenses.size() < 2) return "STABLE";
        
        double first = expenses.get(0).getAmount();
        double last = expenses.get(expenses.size() - 1).getAmount();
        
        if (last > first * 1.1) return "INCREASING";
        if (last < first * 0.9) return "DECREASING";
        return "STABLE";
    }
    
    private Map<String, Double> calculateCategoryTotals(Map<String, List<Double>> categoryExpenses) {
        Map<String, Double> totals = new HashMap<>();
        for (Map.Entry<String, List<Double>> entry : categoryExpenses.entrySet()) {
            double total = entry.getValue().stream().mapToDouble(Double::doubleValue).sum();
            totals.put(entry.getKey(), total);
        }
        return totals;
    }
    
    private List<String> getTopSpendingCategories(Map<String, Double> categoryTotals, int limit) {
        return categoryTotals.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(java.util.stream.Collectors.toList());
    }
    
    private Map<String, Double> calculateCategoryPercentages(Map<String, Double> categoryTotals) {
        double total = categoryTotals.values().stream().mapToDouble(Double::doubleValue).sum();
        Map<String, Double> percentages = new HashMap<>();
        
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            double percentage = (entry.getValue() / total) * 100;
            percentages.put(entry.getKey(), percentage);
        }
        return percentages;
    }
    
    private double calculateMonthOverMonthGrowth(double previousMonth, double currentMonth) {
        if (previousMonth == 0) return 0.0;
        return ((currentMonth - previousMonth) / previousMonth) * 100;
    }
    
    private Map<String, Double> calculateBudgetVariances(List<MockExpenseData> expenses, double budget) {
        Map<String, Double> variances = new HashMap<>();
        for (MockExpenseData expense : expenses) {
            double variance = ((budget - expense.getAmount()) / budget) * 100;
            variances.put(expense.getMonth(), variance);
        }
        return variances;
    }
    
    private double calculateMonthlySavings(double income, double expenses) {
        return income - expenses;
    }
    
    private double calculateSavingsRate(double income, double expenses) {
        if (income == 0) return 0.0;
        return ((income - expenses) / income) * 100;
    }
}
