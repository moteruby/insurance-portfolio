package com.insurance.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AgentTest {

    private Agent agent;

    @BeforeEach
    void setUp() {
        agent = new Agent();
    }

    @Test
    void testAgentCreation() {
        agent.setId(1L);
        agent.setFullName("Петров Петр Петрович");
        agent.setAgentCode("AGT-001");
        agent.setBranch("Московский филиал");
        agent.setPhone("+7 (999) 987-65-43");
        agent.setEmail("petrov@insurance.com");
        agent.setHireDate(LocalDate.of(2020, 1, 15));
        agent.setStatus(AgentStatus.ACTIVE);

        assertEquals(1L, agent.getId());
        assertEquals("Петров Петр Петрович", agent.getFullName());
        assertEquals("AGT-001", agent.getAgentCode());
        assertEquals("Московский филиал", agent.getBranch());
        assertEquals("+7 (999) 987-65-43", agent.getPhone());
        assertEquals("petrov@insurance.com", agent.getEmail());
        assertEquals(LocalDate.of(2020, 1, 15), agent.getHireDate());
        assertEquals(AgentStatus.ACTIVE, agent.getStatus());
    }

    @Test
    void testAgentStatusEnum() {
        assertEquals(3, AgentStatus.values().length);
        assertEquals(AgentStatus.ACTIVE, AgentStatus.valueOf("ACTIVE"));
        assertEquals(AgentStatus.INACTIVE, AgentStatus.valueOf("INACTIVE"));
        assertEquals(AgentStatus.ON_LEAVE, AgentStatus.valueOf("ON_LEAVE"));
    }

    @Test
    void testAllAgentStatuses() {
        agent.setStatus(AgentStatus.ACTIVE);
        assertEquals(AgentStatus.ACTIVE, agent.getStatus());

        agent.setStatus(AgentStatus.INACTIVE);
        assertEquals(AgentStatus.INACTIVE, agent.getStatus());

        agent.setStatus(AgentStatus.ON_LEAVE);
        assertEquals(AgentStatus.ON_LEAVE, agent.getStatus());
    }

    @Test
    void testAgentExperience() {
        LocalDate hireDate = LocalDate.of(2020, 1, 15);
        agent.setHireDate(hireDate);
        
        int experience = LocalDate.now().getYear() - hireDate.getYear();
        assertTrue(experience >= 0);
    }

    @Test
    void testToString() {
        agent.setFullName("Тест Агент");
        String result = agent.toString();
        assertNotNull(result);
        assertTrue(result.contains("Agent"));
    }

    @Test
    void testEqualsAndHashCode() {
        Agent agent1 = new Agent();
        agent1.setId(1L);
        agent1.setAgentCode("AGT-001");
        
        Agent agent2 = new Agent();
        agent2.setId(1L);
        agent2.setAgentCode("AGT-001");
        
        assertEquals(agent1, agent2);
        assertEquals(agent1.hashCode(), agent2.hashCode());
    }
}
