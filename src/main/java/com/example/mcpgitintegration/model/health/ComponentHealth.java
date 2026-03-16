package com.example.mcpgitintegration.model.health;

/**
 * Represents the health status of an individual component (e.g., MySQL, MongoDB).
 */
public class ComponentHealth {

    private String status;
    private String details;

    public ComponentHealth() {
    }

    public ComponentHealth(String status, String details) {
        this.status = status;
        this.details = details;
    }

    public static ComponentHealth up(String details) {
        return new ComponentHealth("UP", details);
    }

    public static ComponentHealth down(String details) {
        return new ComponentHealth("DOWN", details);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
