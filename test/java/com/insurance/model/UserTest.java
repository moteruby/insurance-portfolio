package com.insurance.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void testUserCreation() {
        user.setId(1L);
        user.setUsername("admin");
        user.setPassword("hashed_password");
        user.setFullName("Администратор Системы");
        user.setEmail("admin@insurance.com");
        user.setRole(UserRole.ADMIN);
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());

        assertEquals(1L, user.getId());
        assertEquals("admin", user.getUsername());
        assertEquals("hashed_password", user.getPassword());
        assertEquals("Администратор Системы", user.getFullName());
        assertEquals("admin@insurance.com", user.getEmail());
        assertEquals(UserRole.ADMIN, user.getRole());
        assertTrue(user.getActive());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getLastLogin());
    }

    @Test
    void testUserRoleEnum() {
        assertEquals(3, UserRole.values().length);
        assertEquals(UserRole.ADMIN, UserRole.valueOf("ADMIN"));
        assertEquals(UserRole.AGENT, UserRole.valueOf("AGENT"));
        assertEquals(UserRole.ANALYST, UserRole.valueOf("ANALYST"));
    }

    @Test
    void testAllUserRoles() {
        user.setRole(UserRole.ADMIN);
        assertEquals(UserRole.ADMIN, user.getRole());

        user.setRole(UserRole.AGENT);
        assertEquals(UserRole.AGENT, user.getRole());

        user.setRole(UserRole.ANALYST);
        assertEquals(UserRole.ANALYST, user.getRole());
    }

    @Test
    void testUserActiveStatus() {
        user.setActive(true);
        assertTrue(user.getActive());

        user.setActive(false);
        assertFalse(user.getActive());
    }

    @Test
    void testToString() {
        user.setUsername("testuser");
        String result = user.toString();
        assertNotNull(result);
        assertTrue(result.contains("User"));
    }

    @Test
    void testEqualsAndHashCode() {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("admin");
        
        User user2 = new User();
        user2.setId(1L);
        user2.setUsername("admin");
        
        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }
}
