package com.example.campusexpensemanager;

import com.example.campusexpensemanager.main.Model.Category_Expense_Model;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.time.LocalDateTime;

/**
 * Test cases for Category_Expense_Model
 */
public class CategoryExpenseModelTest {
    
    private Category_Expense_Model category;
    private LocalDateTime testDate;
    
    @Before
    public void setUp() {
        testDate = LocalDateTime.now();
        category = new Category_Expense_Model(1, "Food", 500, "Monthly food budget", testDate, testDate, 1);
    }
    
    @Test
    public void testCategoryExpenseModelConstructor() {
        assertEquals(1, category.getId());
        assertEquals("Food", category.getName());
        assertEquals(500, category.getBudget());
        assertEquals("Monthly food budget", category.getDescription());
        assertEquals(testDate, category.getCreate_at());
        assertEquals(1, category.getUserID());
    }
    
    @Test
    public void testCategoryExpenseModelSetters() {
        category.setId(2);
        category.setName("Transportation");
        category.setBudget(300);
        category.setDescription("Monthly transport budget");
        category.setUserID(2);
        
        assertEquals(2, category.getId());
        assertEquals("Transportation", category.getName());
        assertEquals(300, category.getBudget());
        assertEquals("Monthly transport budget", category.getDescription());
        assertEquals(2, category.getUserID());
    }
    
    @Test
    public void testBudgetValidation() {
        // Test zero budget
        category.setBudget(0);
        assertEquals(0, category.getBudget());
        
        // Test negative budget
        category.setBudget(-100);
        assertEquals(-100, category.getBudget());
        
        // Test large budget
        category.setBudget(1000000);
        assertEquals(1000000, category.getBudget());
    }
    
    @Test
    public void testCategoryNameValidation() {
        // Test empty category name
        category.setName("");
        assertEquals("", category.getName());
        
        // Test null category name
        category.setName(null);
        assertNull(category.getName());
        
        // Test long category name
        String longName = "Very Long Category Name That Exceeds Normal Length";
        category.setName(longName);
        assertEquals(longName, category.getName());
    }
}
