package com.example.mcpgitintegration.controller;

import com.example.mcpgitintegration.model.health.HealthCheckResponse;
import com.example.mcpgitintegration.service.HealthCheckService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller that exposes the application health check endpoint.
 * <p>
 * Returns 200 OK when all database connections are healthy,
 * or 503 Service Unavailable when any connection is down.
 */
@RestController
@RequestMapping("/api")
public class HealthController {

    private final HealthCheckService healthCheckService;

    public HealthController(HealthCheckService healthCheckService) {
        this.healthCheckService = healthCheckService;
    }

    /**
     * Health check endpoint that validates MySQL and MongoDB connectivity.
     *
     * @return 200 OK if all components are UP; 503 if any component is DOWN
     */
    @GetMapping("/health")
    public ResponseEntity<HealthCheckResponse> getHealth() {
        HealthCheckResponse response = healthCheckService.checkHealth();

        if ("UP".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
}
