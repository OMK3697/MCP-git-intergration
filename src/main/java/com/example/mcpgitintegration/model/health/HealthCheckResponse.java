package com.example.mcpgitintegration.model.health;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents the overall health check response including all component statuses.
 */
public class HealthCheckResponse {

    private String status;
    private Map<String, ComponentHealth> components;

    public HealthCheckResponse() {
        this.components = new LinkedHashMap<>();
    }

    public HealthCheckResponse(String status, Map<String, ComponentHealth> components) {
        this.status = status;
        this.components = components;
    }

    /**
     * Adds a component health entry and recalculates the overall status.
     * Overall status is UP only when all components are UP.
     */
    public void addComponent(String name, ComponentHealth componentHealth) {
        this.components.put(name, componentHealth);
        recalculateOverallStatus();
    }

    private void recalculateOverallStatus() {
        boolean allUp = components.values().stream()
                .allMatch(c -> "UP".equals(c.getStatus()));
        this.status = allUp ? "UP" : "DOWN";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, ComponentHealth> getComponents() {
        return components;
    }

    public void setComponents(Map<String, ComponentHealth> components) {
        this.components = components;
    }
}
