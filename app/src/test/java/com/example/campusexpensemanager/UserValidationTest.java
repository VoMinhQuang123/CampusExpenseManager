package com.example.campusexpensemanager;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Test cases for User Input Validation
 */
public class UserValidationTest {
    
    @Test
    public void testEmailValidation() {
        // Valid email formats
        assertTrue(isValidEmail("user@example.com"));
        assertTrue(isValidEmail("test.email@domain.org"));
        assertTrue(isValidEmail("user123@test-domain.com"));
        
        // Invalid email formats
        assertFalse(isValidEmail("invalid-email"));
        assertFalse(isValidEmail("@domain.com"));
        assertFalse(isValidEmail("user@"));
        assertFalse(isValidEmail(""));
        assertFalse(isValidEmail(null));
    }
    
    @Test
    public void testPasswordValidation() {
        // Valid passwords (assuming minimum 6 characters)
        assertTrue(isValidPassword("password123"));
        assertTrue(isValidPassword("123456"));
        assertTrue(isValidPassword("Pa$$w0rd"));
        
        // Invalid passwords
        assertFalse(isValidPassword("12345")); // Too short
        assertFalse(isValidPassword("")); // Empty
        assertFalse(isValidPassword(null)); // Null
        assertFalse(isValidPassword("     ")); // Only spaces
    }

    @Test
    public void testUsernameValidation() {
        // Valid usernames
        assertTrue(isValidUsername("user123"));
        assertTrue(isValidUsername("testuser"));
        assertTrue(isValidUsername("user_name"));
        
        // Invalid usernames
        assertFalse(isValidUsername("us")); // Too short
        assertFalse(isValidUsername("")); // Empty
        assertFalse(isValidUsername(null)); // Null
        assertFalse(isValidUsername("user@name")); // Special characters
        assertFalse(isValidUsername("   ")); // Only spaces
    }
    
    // Helper methods for validation (would normally be in a utility class)
    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
    
    private boolean isValidPassword(String password) {
        if (password == null || password.trim().isEmpty()) return false;
        return password.length() >= 6;
    }

    private boolean isValidUsername(String username) {
        if (username == null || username.trim().isEmpty()) return false;
        return username.length() >= 3 && username.matches("^[a-zA-Z0-9_]+$");
    }
}
