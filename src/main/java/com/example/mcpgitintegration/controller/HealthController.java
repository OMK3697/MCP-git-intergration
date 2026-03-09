package com.example.mcpgitintegration.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

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
