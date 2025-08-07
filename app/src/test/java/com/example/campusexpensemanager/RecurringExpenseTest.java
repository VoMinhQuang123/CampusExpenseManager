package com.example.campusexpensemanager;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Test cases for Recurring Expense Logic
 */
public class RecurringExpenseTest {
    
    private LocalDateTime baseDate;
    
    @Before
    public void setUp() {
        baseDate = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
    }
    
    @Test
    public void testDailyRecurringExpense() {
        LocalDateTime nextDaily = calculateNextRecurrence(baseDate, "DAILY");
        LocalDateTime expectedDaily = baseDate.plusDays(1);
        assertEquals(expectedDaily, nextDaily);
    }
    
    @Test
    public void testWeeklyRecurringExpense() {
        LocalDateTime nextWeekly = calculateNextRecurrence(baseDate, "WEEKLY");
        LocalDateTime expectedWeekly = baseDate.plusWeeks(1);
        assertEquals(expectedWeekly, nextWeekly);
    }
    
    @Test
    public void testMonthlyRecurringExpense() {
        LocalDateTime nextMonthly = calculateNextRecurrence(baseDate, "MONTHLY");
        LocalDateTime expectedMonthly = baseDate.plusMonths(1);
        assertEquals(expectedMonthly, nextMonthly);
    }
    
    @Test
    public void testYearlyRecurringExpense() {
        LocalDateTime nextYearly = calculateNextRecurrence(baseDate, "YEARLY");
        LocalDateTime expectedYearly = baseDate.plusYears(1);
        assertEquals(expectedYearly, nextYearly);
    }
    
    @Test
    public void testInvalidRecurrenceType() {
        LocalDateTime result = calculateNextRecurrence(baseDate, "INVALID");
        assertNull(result);
        
        result = calculateNextRecurrence(baseDate, null);
        assertNull(result);
        
        result = calculateNextRecurrence(baseDate, "");
        assertNull(result);
    }
    
    @Test
    public void testRecurringExpenseStatus() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime futureDate = now.plusDays(1);
        LocalDateTime pastDate = now.minusDays(1);
        
        assertTrue(isRecurrenceActive(futureDate));
        assertFalse(isRecurrenceActive(pastDate));
        assertFalse(isRecurrenceActive(now)); // Exact time might be considered not active
    }
    
    @Test
    public void testRecurringExpenseFrequencyValidation() {
        String[] validFrequencies = {"DAILY", "WEEKLY", "MONTHLY", "YEARLY"};
        String[] invalidFrequencies = {"HOURLY", "BIWEEKLY", "QUARTERLY", "", null};
        
        for (String frequency : validFrequencies) {
            assertTrue("Should be valid frequency: " + frequency, isValidFrequency(frequency));
        }
        
        for (String frequency : invalidFrequencies) {
            assertFalse("Should be invalid frequency: " + frequency, isValidFrequency(frequency));
        }
    }
    
    @Test
    public void testRecurringExpenseAmount() {
        // Test valid amounts
        assertTrue(isValidRecurringAmount(100.0));
        assertTrue(isValidRecurringAmount(0.01));
        assertTrue(isValidRecurringAmount(999999.99));
        
        // Test invalid amounts
        assertFalse(isValidRecurringAmount(0.0));
        assertFalse(isValidRecurringAmount(-100.0));
        assertFalse(isValidRecurringAmount(-0.01));
    }
    
    // Helper methods
    private LocalDateTime calculateNextRecurrence(LocalDateTime baseDate, String frequency) {
        if (baseDate == null || frequency == null) return null;
        
        switch (frequency.toUpperCase()) {
            case "DAILY":
                return baseDate.plusDays(1);
            case "WEEKLY":
                return baseDate.plusWeeks(1);
            case "MONTHLY":
                return baseDate.plusMonths(1);
            case "YEARLY":
                return baseDate.plusYears(1);
            default:
                return null;
        }
    }
    
    private boolean isRecurrenceActive(LocalDateTime nextRecurrence) {
        return nextRecurrence != null && nextRecurrence.isAfter(LocalDateTime.now());
    }
    
    private boolean isValidFrequency(String frequency) {
        if (frequency == null || frequency.trim().isEmpty()) return false;
        return Arrays.asList("DAILY", "WEEKLY", "MONTHLY", "YEARLY")
                .contains(frequency.toUpperCase());
    }
    
    private boolean isValidRecurringAmount(double amount) {
        return amount > 0;
    }
}
