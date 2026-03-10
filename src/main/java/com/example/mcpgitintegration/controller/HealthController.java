package com.example.mcpgitintegration.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Custom health endpoint.
 *
 * @deprecated This controller is superseded by Spring Boot Actuator's
 *             {@code /actuator/health} endpoint (KAN-1). Use
 *             {@code /actuator/health} for production monitoring instead.
 *             This endpoint is retained temporarily for backward compatibility
 *             and will be removed in a future release.
 */
@Deprecated
@RestController
@RequestMapping("/api")
public class HealthController {

    private final long startTimeMillis;
    private final String applicationName;
    private final String applicationVersion;

    public HealthController(
            @Value("${spring.application.name:application}") String applicationName,
            @Value("${app.version:unknown}") String applicationVersion
    ) {
        this.startTimeMillis = System.currentTimeMillis();
        this.applicationName = applicationName;
        this.applicationVersion = applicationVersion;
    }

    /**
     * @deprecated Use {@code /actuator/health} instead.
     */
    @Deprecated
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> getHealth() {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("appName", applicationName);
        payload.put("version", applicationVersion);
        payload.put("status", "UP");
        payload.put("uptime", System.currentTimeMillis() - startTimeMillis);

        return ResponseEntity.ok(payload);
    }
}
