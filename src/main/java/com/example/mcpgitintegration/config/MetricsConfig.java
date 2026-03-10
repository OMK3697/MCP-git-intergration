package com.example.mcpgitintegration.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Metrics configuration for Micrometer / Actuator instrumentation.
 * <p>
 * Adds common tags to all metrics so they can be identified
 * when multiple applications report to the same monitoring backend.
 */
@Configuration
public class MetricsConfig {

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> commonTags() {
        return registry -> registry.config()
                .commonTags("application", "mcp-git-integration");
    }
}
