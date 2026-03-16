package com.example.mcpgitintegration.controller;

import com.example.mcpgitintegration.model.health.ComponentHealth;
import com.example.mcpgitintegration.model.health.HealthCheckResponse;
import com.example.mcpgitintegration.service.HealthCheckService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.bean.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for {@link HealthController}.
 * Uses {@code @WebMvcTest} to test only the web layer without starting the full context.
 */
@WebMvcTest(HealthController.class)
class HealthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HealthCheckService healthCheckService;

    @Test
    @DisplayName("GET /api/health returns 200 when all components are UP")
    void getHealth_allComponentsUp_returns200() throws Exception {
        HealthCheckResponse response = new HealthCheckResponse();
        response.addComponent("mysql", ComponentHealth.up("MySQL connection is healthy"));
        response.addComponent("mongo", ComponentHealth.up("MongoDB connection is healthy"));

        when(healthCheckService.checkHealth()).thenReturn(response);

        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.components.mysql.status").value("UP"))
                .andExpect(jsonPath("$.components.mysql.details").value("MySQL connection is healthy"))
                .andExpect(jsonPath("$.components.mongo.status").value("UP"))
                .andExpect(jsonPath("$.components.mongo.details").value("MongoDB connection is healthy"));
    }

    @Test
    @DisplayName("GET /api/health returns 503 when MySQL is DOWN")
    void getHealth_mysqlDown_returns503() throws Exception {
        HealthCheckResponse response = new HealthCheckResponse();
        response.addComponent("mysql", ComponentHealth.down("MySQL connection failed: Connection refused"));
        response.addComponent("mongo", ComponentHealth.up("MongoDB connection is healthy"));

        when(healthCheckService.checkHealth()).thenReturn(response);

        mockMvc.perform(get("/api/health"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.status").value("DOWN"))
                .andExpect(jsonPath("$.components.mysql.status").value("DOWN"))
                .andExpect(jsonPath("$.components.mongo.status").value("UP"));
    }

    @Test
    @DisplayName("GET /api/health returns 503 when MongoDB is DOWN")
    void getHealth_mongoDown_returns503() throws Exception {
        HealthCheckResponse response = new HealthCheckResponse();
        response.addComponent("mysql", ComponentHealth.up("MySQL connection is healthy"));
        response.addComponent("mongo", ComponentHealth.down("MongoDB connection failed: Connection refused"));

        when(healthCheckService.checkHealth()).thenReturn(response);

        mockMvc.perform(get("/api/health"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.status").value("DOWN"))
                .andExpect(jsonPath("$.components.mysql.status").value("UP"))
                .andExpect(jsonPath("$.components.mongo.status").value("DOWN"));
    }

    @Test
    @DisplayName("GET /api/health returns 503 when both databases are DOWN")
    void getHealth_bothDown_returns503() throws Exception {
        HealthCheckResponse response = new HealthCheckResponse();
        response.addComponent("mysql", ComponentHealth.down("MySQL connection failed: Connection refused"));
        response.addComponent("mongo", ComponentHealth.down("MongoDB connection failed: Connection refused"));

        when(healthCheckService.checkHealth()).thenReturn(response);

        mockMvc.perform(get("/api/health"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.status").value("DOWN"))
                .andExpect(jsonPath("$.components.mysql.status").value("DOWN"))
                .andExpect(jsonPath("$.components.mongo.status").value("DOWN"));
    }
}
