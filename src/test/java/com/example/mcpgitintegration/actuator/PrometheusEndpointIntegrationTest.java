package com.example.mcpgitintegration.actuator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.metrics.AutoConfigureMetrics;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Integration tests for the Prometheus metrics endpoint.
 * <p>
 * Uses a real servlet container (RANDOM_PORT) and {@code @AutoConfigureMetrics}
 * to ensure metric export auto-configurations (including Prometheus) are active
 * during the test.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMetrics
class PrometheusEndpointIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void prometheusShouldReturnMetricsInTextFormat() {
        ResponseEntity<String> response = restTemplate.getForEntity("/actuator/prometheus", String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), notNullValue());
        assertThat(response.getBody(), containsString("jvm_memory_used_bytes"));
        assertThat(response.getBody(), containsString("jvm_memory_max_bytes"));
        assertThat(response.getBody(), containsString("system_cpu_usage"));
        assertThat(response.getBody(), containsString("process_cpu_usage"));
    }

    @Test
    void prometheusShouldIncludeApplicationTag() {
        ResponseEntity<String> response = restTemplate.getForEntity("/actuator/prometheus", String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), notNullValue());
        assertThat(response.getBody(), containsString("application=\"mcp-git-integration\""));
    }

    @Test
    void prometheusShouldIncludeJvmGcMetrics() {
        ResponseEntity<String> response = restTemplate.getForEntity("/actuator/prometheus", String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), notNullValue());
        assertThat(response.getBody(), containsString("jvm_gc_"));
    }

    @Test
    void prometheusShouldIncludeJvmThreadMetrics() {
        ResponseEntity<String> response = restTemplate.getForEntity("/actuator/prometheus", String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), notNullValue());
        assertThat(response.getBody(), containsString("jvm_threads_live_threads"));
        assertThat(response.getBody(), containsString("jvm_threads_peak_threads"));
    }
}
