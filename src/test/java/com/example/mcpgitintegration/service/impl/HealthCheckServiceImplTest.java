package com.example.mcpgitintegration.service.impl;

import com.example.mcpgitintegration.model.health.ComponentHealth;
import com.example.mcpgitintegration.model.health.HealthCheckResponse;
import com.example.mcpgitintegration.service.health.DatabaseHealthChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for {@link HealthCheckServiceImpl}.
 * Uses simple stub implementations of {@link DatabaseHealthChecker} —
 * no Spring context or mocking framework required.
 */
class HealthCheckServiceImplTest {

    private StubHealthChecker mysqlChecker;
    private StubHealthChecker mongoChecker;
    private HealthCheckServiceImpl healthCheckService;

    @BeforeEach
    void setUp() {
        mysqlChecker = new StubHealthChecker("mysql");
        mongoChecker = new StubHealthChecker("mongo");
        healthCheckService = new HealthCheckServiceImpl(Arrays.asList(mysqlChecker, mongoChecker));
    }

    @Test
    @DisplayName("checkHealth returns UP when all databases are healthy")
    void checkHealth_allHealthy_returnsUp() {
        mysqlChecker.setHealth(ComponentHealth.up("MySQL connection is healthy"));
        mongoChecker.setHealth(ComponentHealth.up("MongoDB connection is healthy"));

        HealthCheckResponse response = healthCheckService.checkHealth();

        assertNotNull(response);
        assertEquals("UP", response.getStatus());
        assertEquals("UP", response.getComponents().get("mysql").getStatus());
        assertEquals("MySQL connection is healthy", response.getComponents().get("mysql").getDetails());
        assertEquals("UP", response.getComponents().get("mongo").getStatus());
        assertEquals("MongoDB connection is healthy", response.getComponents().get("mongo").getDetails());
    }

    @Test
    @DisplayName("checkHealth returns DOWN when MySQL is down")
    void checkHealth_mysqlDown_returnsDown() {
        mysqlChecker.setHealth(ComponentHealth.down("MySQL connection failed: Connection refused"));
        mongoChecker.setHealth(ComponentHealth.up("MongoDB connection is healthy"));

        HealthCheckResponse response = healthCheckService.checkHealth();

        assertEquals("DOWN", response.getStatus());
        assertEquals("DOWN", response.getComponents().get("mysql").getStatus());
        assertEquals("UP", response.getComponents().get("mongo").getStatus());
    }

    @Test
    @DisplayName("checkHealth returns DOWN when MongoDB is down")
    void checkHealth_mongoDown_returnsDown() {
        mysqlChecker.setHealth(ComponentHealth.up("MySQL connection is healthy"));
        mongoChecker.setHealth(ComponentHealth.down("MongoDB connection failed: Connection refused"));

        HealthCheckResponse response = healthCheckService.checkHealth();

        assertEquals("DOWN", response.getStatus());
        assertEquals("UP", response.getComponents().get("mysql").getStatus());
        assertEquals("DOWN", response.getComponents().get("mongo").getStatus());
    }

    @Test
    @DisplayName("checkHealth returns DOWN when both databases are down")
    void checkHealth_bothDown_returnsDown() {
        mysqlChecker.setHealth(ComponentHealth.down("MySQL connection failed: Connection refused"));
        mongoChecker.setHealth(ComponentHealth.down("MongoDB connection failed: Connection refused"));

        HealthCheckResponse response = healthCheckService.checkHealth();

        assertEquals("DOWN", response.getStatus());
        assertEquals("DOWN", response.getComponents().get("mysql").getStatus());
        assertEquals("DOWN", response.getComponents().get("mongo").getStatus());
    }

    /**
     * Simple stub implementation of {@link DatabaseHealthChecker} for testing.
     */
    private static class StubHealthChecker implements DatabaseHealthChecker {

        private final String componentName;
        private ComponentHealth health;

        StubHealthChecker(String componentName) {
            this.componentName = componentName;
            this.health = ComponentHealth.up("Default healthy");
        }

        void setHealth(ComponentHealth health) {
            this.health = health;
        }

        @Override
        public String getComponentName() {
            return componentName;
        }

        @Override
        public ComponentHealth check() {
            return health;
        }
    }
}
