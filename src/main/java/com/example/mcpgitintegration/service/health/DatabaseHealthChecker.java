package com.example.mcpgitintegration.service.health;

import com.example.mcpgitintegration.model.health.ComponentHealth;

/**
 * Abstraction for individual database connectivity checks.
 * <p>
 * Each implementation is responsible for checking one database type.
 * Follows Interface Segregation — each checker has a single focused contract.
 */
public interface DatabaseHealthChecker {

    /**
     * Returns the component name (e.g., "mysql", "mongo").
     */
    String getComponentName();

    /**
     * Performs a connectivity check against the database.
     * Must never throw — errors should be caught and returned as a DOWN status.
     */
    ComponentHealth check();
}
