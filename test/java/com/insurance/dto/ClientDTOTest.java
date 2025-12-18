package com.insurance.dto;

import com.insurance.model.ClientStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ClientDTOTest {

    private ClientDTO clientDTO;

    @BeforeEach
    void setUp() {
        clientDTO = new ClientDTO();
    }

    @Test
    void testClientDTOCreation() {
        clientDTO.setId(1L);
        clientDTO.setFullName("Иванов Иван Иванович");
        clientDTO.setBirthDate(LocalDate.of(1985, 5, 15));
        clientDTO.setPassportData("1234 567890");
        clientDTO.setAddress("г. Москва, ул. Ленина, д. 10");
        clientDTO.setRegion("Москва");
        clientDTO.setPhone("+7 (999) 123-45-67");
        clientDTO.setEmail("ivanov@example.com");
        clientDTO.setStatus(ClientStatus.ACTIVE);

        assertEquals(1L, clientDTO.getId());
        assertEquals("Иванов Иван Иванович", clientDTO.getFullName());
        assertEquals(LocalDate.of(1985, 5, 15), clientDTO.getBirthDate());
        assertEquals("1234 567890", clientDTO.getPassportData());
        assertEquals("г. Москва, ул. Ленина, д. 10", clientDTO.getAddress());
        assertEquals("Москва", clientDTO.getRegion());
        assertEquals("+7 (999) 123-45-67", clientDTO.getPhone());
        assertEquals("ivanov@example.com", clientDTO.getEmail());
        assertEquals(ClientStatus.ACTIVE, clientDTO.getStatus());
    }

    @Test
    void testAllClientStatuses() {
        clientDTO.setStatus(ClientStatus.ACTIVE);
        assertEquals(ClientStatus.ACTIVE, clientDTO.getStatus());

        clientDTO.setStatus(ClientStatus.INACTIVE);
        assertEquals(ClientStatus.INACTIVE, clientDTO.getStatus());

        clientDTO.setStatus(ClientStatus.BLOCKED);
        assertEquals(ClientStatus.BLOCKED, clientDTO.getStatus());
    }

    @Test
    void testNullValues() {
        assertNull(clientDTO.getId());
        assertNull(clientDTO.getFullName());
        assertNull(clientDTO.getBirthDate());
        assertNull(clientDTO.getPassportData());
        assertNull(clientDTO.getAddress());
        assertNull(clientDTO.getRegion());
        assertNull(clientDTO.getPhone());
        assertNull(clientDTO.getEmail());
        assertNull(clientDTO.getStatus());
    }

    @Test
    void testToString() {
        clientDTO.setFullName("Тест");
        clientDTO.setEmail("test@example.com");
        
        String result = clientDTO.toString();
        assertNotNull(result);
        assertTrue(result.contains("ClientDTO"));
    }

    @Test
    void testEqualsAndHashCode() {
        ClientDTO client1 = new ClientDTO();
        client1.setId(1L);
        client1.setFullName("Иванов Иван");
        client1.setEmail("ivanov@example.com");
        
        ClientDTO client2 = new ClientDTO();
        client2.setId(1L);
        client2.setFullName("Иванов Иван");
        client2.setEmail("ivanov@example.com");
        
        assertEquals(client1, client2);
        assertEquals(client1.hashCode(), client2.hashCode());
    }

    @Test
    void testPhoneNumberFormats() {
        String[] phoneNumbers = {
            "+7 (999) 123-45-67",
            "+7 999 123 45 67",
            "89991234567",
            "+79991234567"
        };

        for (String phone : phoneNumbers) {
            clientDTO.setPhone(phone);
            assertEquals(phone, clientDTO.getPhone());
        }
    }

    @Test
    void testEmailValidation() {
        String[] emails = {
            "test@example.com",
            "user.name@domain.ru",
            "admin@insurance.com"
        };

        for (String email : emails) {
            clientDTO.setEmail(email);
            assertEquals(email, clientDTO.getEmail());
            assertTrue(clientDTO.getEmail().contains("@"));
        }
    }
}
