package com.insurance.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    private Client client;

    @BeforeEach
    void setUp() {
        client = new Client();
    }

    @Test
    void testClientCreation() {
        client.setId(1L);
        client.setFullName("Иванов Иван Иванович");
        client.setEmail("ivanov@example.com");
        client.setPhone("+7 (999) 123-45-67");
        client.setRegion("Москва");
        client.setStatus(ClientStatus.ACTIVE);
        client.setBirthDate(LocalDate.of(1985, 5, 15));
        client.setPassportData("1234 567890");
        client.setAddress("г. Москва, ул. Ленина, д. 10");
        client.setRegistrationDate(LocalDate.now());

        assertEquals(1L, client.getId());
        assertEquals("Иванов Иван Иванович", client.getFullName());
        assertEquals("ivanov@example.com", client.getEmail());
        assertEquals(ClientStatus.ACTIVE, client.getStatus());
        assertEquals("+7 (999) 123-45-67", client.getPhone());
        assertEquals("Москва", client.getRegion());
        assertNotNull(client.getRegistrationDate());
    }

    @Test
    void testClientStatusEnum() {
        assertEquals(3, ClientStatus.values().length);
        assertEquals(ClientStatus.ACTIVE, ClientStatus.valueOf("ACTIVE"));
        assertEquals(ClientStatus.INACTIVE, ClientStatus.valueOf("INACTIVE"));
        assertEquals(ClientStatus.BLOCKED, ClientStatus.valueOf("BLOCKED"));
    }

    @Test
    void testAllClientStatuses() {
        client.setStatus(ClientStatus.ACTIVE);
        assertEquals(ClientStatus.ACTIVE, client.getStatus());

        client.setStatus(ClientStatus.INACTIVE);
        assertEquals(ClientStatus.INACTIVE, client.getStatus());

        client.setStatus(ClientStatus.BLOCKED);
        assertEquals(ClientStatus.BLOCKED, client.getStatus());
    }

    @Test
    void testClientAge() {
        LocalDate birthDate = LocalDate.of(1985, 5, 15);
        client.setBirthDate(birthDate);
        
        int age = LocalDate.now().getYear() - birthDate.getYear();
        assertTrue(age > 0);
        assertTrue(age < 150);
    }

    @Test
    void testClientWithAllFields() {
        client.setId(1L);
        client.setFullName("Петров Петр Петрович");
        client.setBirthDate(LocalDate.of(1990, 8, 20));
        client.setPassportData("2345 678901");
        client.setAddress("г. Москва, ул. Пушкина, д. 25");
        client.setRegion("Москва");
        client.setPhone("+7 (999) 234-56-78");
        client.setEmail("petrov@example.com");
        client.setStatus(ClientStatus.ACTIVE);
        client.setRegistrationDate(LocalDate.of(2023, 1, 10));

        assertNotNull(client.getId());
        assertNotNull(client.getFullName());
        assertNotNull(client.getBirthDate());
        assertNotNull(client.getPassportData());
        assertNotNull(client.getAddress());
        assertNotNull(client.getRegion());
        assertNotNull(client.getPhone());
        assertNotNull(client.getEmail());
        assertNotNull(client.getStatus());
        assertNotNull(client.getRegistrationDate());
    }

    @Test
    void testToString() {
        client.setFullName("Тест");
        String result = client.toString();
        assertNotNull(result);
        assertTrue(result.contains("Client"));
    }

    @Test
    void testEqualsAndHashCode() {
        Client client1 = new Client();
        client1.setId(1L);
        client1.setFullName("Иванов");
        
        Client client2 = new Client();
        client2.setId(1L);
        client2.setFullName("Иванов");
        
        assertEquals(client1, client2);
        assertEquals(client1.hashCode(), client2.hashCode());
    }
}
