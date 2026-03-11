package com.example.mcpgitintegration.service.impl;

import com.example.mcpgitintegration.model.health.ComponentHealth;
import com.example.mcpgitintegration.model.health.HealthCheckResponse;
import com.example.mcpgitintegration.service.HealthCheckService;
import com.example.mcpgitintegration.service.health.DatabaseHealthChecker;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Aggregates health checks from all registered {@link DatabaseHealthChecker} instances.
 * <p>
 * Follows the Open/Closed principle: new database checkers can be added by
 * simply creating a new {@link DatabaseHealthChecker} bean — no modification
 * to this class is required.
 * <p>
 * Each database check is isolated: one failing check does not prevent
 * the other from executing.
 */
@Service
public class HealthCheckServiceImpl implements HealthCheckService {

    private final List<DatabaseHealthChecker> healthCheckers;

    public HealthCheckServiceImpl(List<DatabaseHealthChecker> healthCheckers) {
        this.healthCheckers = healthCheckers;
    }

    @Override
    public HealthCheckResponse checkHealth() {
        HealthCheckResponse response = new HealthCheckResponse();

        for (DatabaseHealthChecker checker : healthCheckers) {
            ComponentHealth componentHealth = checker.check();
            response.addComponent(checker.getComponentName(), componentHealth);
        }

        return response;
    }
}
