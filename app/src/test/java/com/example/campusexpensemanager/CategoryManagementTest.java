package com.example.campusexpensemanager;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;

/**
 * Test cases for Category Management Logic
 */
public class CategoryManagementTest {
    
    private List<String> validCategories;
    private List<String> invalidCategories;
    
    @Before
    public void setUp() {
        validCategories = Arrays.asList("Food", "Transportation", "Entertainment", "Education", "Healthcare");
        invalidCategories = Arrays.asList("", null, "   ", "A", "Very Long Category Name That Exceeds Normal Length Limit");
    }
    
    @Test
    public void testValidCategoryNames() {
        for (String category : validCategories) {
            assertTrue("Category should be valid: " + category, isValidCategoryName(category));
        }
    }
    
    @Test
    public void testInvalidCategoryNames() {
        for (String category : invalidCategories) {
            assertFalse("Category should be invalid: " + category, isValidCategoryName(category));
        }
    }
    
    @Test
    public void testCategoryNameLength() {
        // Test minimum length
        assertFalse(isValidCategoryName("A"));
        assertTrue(isValidCategoryName("AB"));
        
        // Test maximum length
        String maxLengthCategory = "A".repeat(50);
        assertTrue(isValidCategoryName(maxLengthCategory));
        
        String tooLongCategory = "A".repeat(51);
        assertFalse(isValidCategoryName(tooLongCategory));
    }
    
    @Test
    public void testCategoryBudgetValidation() {
        // Valid budgets
        assertTrue(isValidBudget(100));
        assertTrue(isValidBudget(0));
        assertTrue(isValidBudget(1000000));
        
        // Invalid budgets
        assertFalse(isValidBudget(-1));
        assertFalse(isValidBudget(-100));
    }
    
    @Test
    public void testCategoryDescriptionValidation() {
        // Valid descriptions
        assertTrue(isValidDescription("Monthly food budget"));
        assertTrue(isValidDescription(""));
        assertTrue(isValidDescription("Short"));
        
        // Invalid descriptions
        String tooLongDescription = "A".repeat(201);
        assertFalse(isValidDescription(tooLongDescription));
        assertFalse(isValidDescription(null));
    }
    
    @Test
    public void testCategoryDuplicateCheck() {
        List<String> existingCategories = Arrays.asList("Food", "Transportation", "Entertainment");
        
        // Test duplicate category
        assertFalse(isUniqueCategoryName("Food", existingCategories));
        assertFalse(isUniqueCategoryName("FOOD", existingCategories)); // Case insensitive
        
        // Test unique category
        assertTrue(isUniqueCategoryName("Healthcare", existingCategories));
        assertTrue(isUniqueCategoryName("Education", existingCategories));
    }
    
    // Helper methods
    private boolean isValidCategoryName(String name) {
        if (name == null || name.trim().isEmpty()) return false;
        return name.trim().length() >= 2 && name.trim().length() <= 50;
    }
    
    private boolean isValidBudget(int budget) {
        return budget >= 0;
    }
    
    private boolean isValidDescription(String description) {
        if (description == null) return false;
        return description.length() <= 200;
    }
    
    private boolean isUniqueCategoryName(String name, List<String> existingCategories) {
        return existingCategories.stream()
                .noneMatch(existing -> existing.equalsIgnoreCase(name));
    }
}
