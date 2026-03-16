package com.example.mcpgitintegration.service;

import com.example.mcpgitintegration.model.health.HealthCheckResponse;

/**
 * Service for performing health checks across all application dependencies.
 */
public interface HealthCheckService {

    /**
     * Runs all configured database connectivity checks and returns
     * an aggregated health check response.
     *
     * @return {@link HealthCheckResponse} containing overall and per-component status
     */
    HealthCheckResponse checkHealth();
}
