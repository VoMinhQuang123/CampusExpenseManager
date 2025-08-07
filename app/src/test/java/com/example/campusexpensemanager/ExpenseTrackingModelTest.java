package com.example.campusexpensemanager;

import com.example.campusexpensemanager.main.Model.Expense_Tracking_Model;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.time.LocalDateTime;

/**
 * Test cases for Expense_Tracking_Model
 */
public class ExpenseTrackingModelTest {
    
    private Expense_Tracking_Model expense;
    private LocalDateTime testDate;
    
    @Before
    public void setUp() {
        testDate = LocalDateTime.now();
        expense = new Expense_Tracking_Model(1, "Test Expense", 100.50, "Test note", testDate, testDate, 1, 1);
    }
    
    @Test
    public void testExpenseTrackingModelConstructor() {
        assertEquals(1, expense.getId());
        assertEquals("Test Expense", expense.getName());
        assertEquals(100.50, expense.getExpense(), 0.01);
        assertEquals("Test note", expense.getNote());
        assertEquals(testDate, expense.getCreate_at());
        assertEquals(1, expense.getCategoryId());
        assertEquals(1, expense.getUserID());
    }
    
    @Test
    public void testExpenseTrackingModelSetters() {
        expense.setId(2);
        expense.setName("Updated Expense");
        expense.setExpense(200.75);
        expense.setNote("Updated note");
        expense.setCategoryId(2);
        expense.setUserID(2);
        
        assertEquals(2, expense.getId());
        assertEquals("Updated Expense", expense.getName());
        assertEquals(200.75, expense.getExpense(), 0.01);
        assertEquals("Updated note", expense.getNote());
        assertEquals(2, expense.getCategoryId());
        assertEquals(2, expense.getUserID());
    }
    
    @Test
    public void testExpenseValidation() {
        // Test negative expense validation
        expense.setExpense(-50.0);
        assertTrue("Expense should allow negative values for refunds", expense.getExpense() < 0);
        
        // Test zero expense
        expense.setExpense(0.0);
        assertEquals(0.0, expense.getExpense(), 0.01);
        
        // Test large expense amount
        expense.setExpense(999999.99);
        assertEquals(999999.99, expense.getExpense(), 0.01);
    }
}
