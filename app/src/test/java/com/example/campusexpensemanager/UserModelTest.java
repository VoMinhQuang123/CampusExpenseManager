package com.example.campusexpensemanager;

import com.example.campusexpensemanager.main.Model.User_Model;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Test cases for User_Model
 */
public class UserModelTest {
    
    private User_Model user;
    
    @Before
    public void setUp() {
        user = new User_Model();
    }
    
    @Test
    public void testUserModelSettersAndGetters() {
        // Test setting and getting user ID
        user.setId(1);
        assertEquals(1, user.getId());
        
        // Test setting and getting username
        user.setUsername("testuser");
        assertEquals("testuser", user.getUsername());
        
        // Test setting and getting email
        user.setEmail("test@example.com");
        assertEquals("test@example.com", user.getEmail());
        
        // Test setting and getting password
        user.setPassword("password123");
        assertEquals("password123", user.getPassword());
        
        // Test setting and getting phone
        user.setPhone("0123456789");
        assertEquals("0123456789", user.getPhone());
    }
    
    @Test
    public void testUserModelInitialValues() {
        // Test that initial values are null/0
        assertEquals(0, user.getId());
        assertNull(user.getUsername());
        assertNull(user.getEmail());
        assertNull(user.getPassword());
        assertNull(user.getPhone());
    }
}
