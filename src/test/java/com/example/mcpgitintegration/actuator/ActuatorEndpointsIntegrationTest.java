package com.example.mcpgitintegration.actuator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests that verify Spring Boot Actuator and Micrometer
 * endpoints are properly configured and return expected metrics.
 */
@SpringBootTest
@AutoConfigureMockMvc
class ActuatorEndpointsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    // -------------------------------------------------------
    // /actuator/health
    // -------------------------------------------------------

    @Test
    void actuatorHealthShouldReturnStatusUp() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("UP")));
    }

    @Test
    void actuatorHealthShouldShowComponentDetails() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.components").exists())
                .andExpect(jsonPath("$.components.diskSpace").exists())
                .andExpect(jsonPath("$.components.diskSpace.status", is("UP")));
    }

    // -------------------------------------------------------
    // /actuator/metrics
    // -------------------------------------------------------

    @Test
    void actuatorMetricsShouldListAvailableMetricNames() throws Exception {
        mockMvc.perform(get("/actuator/metrics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.names").isArray())
                .andExpect(jsonPath("$.names").isNotEmpty());
    }

    // -------------------------------------------------------
    // Memory Metrics
    // -------------------------------------------------------

    @Test
    void shouldExposeJvmMemoryUsedMetric() throws Exception {
        mockMvc.perform(get("/actuator/metrics/jvm.memory.used"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("jvm.memory.used")))
                .andExpect(jsonPath("$.measurements[0].value").isNumber());
    }

    @Test
    void shouldExposeJvmMemoryMaxMetric() throws Exception {
        mockMvc.perform(get("/actuator/metrics/jvm.memory.max"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("jvm.memory.max")))
                .andExpect(jsonPath("$.measurements[0].value").isNumber());
    }

    @Test
    void shouldExposeJvmMemoryCommittedMetric() throws Exception {
        mockMvc.perform(get("/actuator/metrics/jvm.memory.committed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("jvm.memory.committed")))
                .andExpect(jsonPath("$.measurements[0].value").isNumber());
    }

    // -------------------------------------------------------
    // CPU Metrics
    // -------------------------------------------------------

    @Test
    void shouldExposeProcessCpuUsageMetric() throws Exception {
        mockMvc.perform(get("/actuator/metrics/process.cpu.usage"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("process.cpu.usage")))
                .andExpect(jsonPath("$.measurements[0].value").isNumber());
    }

    @Test
    void shouldExposeSystemCpuUsageMetric() throws Exception {
        mockMvc.perform(get("/actuator/metrics/system.cpu.usage"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("system.cpu.usage")))
                .andExpect(jsonPath("$.measurements[0].value").isNumber());
    }

    @Test
    void shouldExposeSystemCpuCountMetric() throws Exception {
        mockMvc.perform(get("/actuator/metrics/system.cpu.count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("system.cpu.count")))
                .andExpect(jsonPath("$.measurements[0].value").isNumber());
    }

    // -------------------------------------------------------
    // JVM Thread Metrics
    // -------------------------------------------------------

    @Test
    void shouldExposeJvmThreadsLiveMetric() throws Exception {
        mockMvc.perform(get("/actuator/metrics/jvm.threads.live"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("jvm.threads.live")))
                .andExpect(jsonPath("$.measurements[0].value").isNumber());
    }

    @Test
    void shouldExposeJvmThreadsPeakMetric() throws Exception {
        mockMvc.perform(get("/actuator/metrics/jvm.threads.peak"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("jvm.threads.peak")))
                .andExpect(jsonPath("$.measurements[0].value").isNumber());
    }

    // -------------------------------------------------------
    // HTTP Metrics (trigger a request first, then verify)
    // -------------------------------------------------------

    @Test
    void shouldExposeHttpServerRequestsMetricAfterApiCall() throws Exception {
        // Trigger an HTTP request to generate metrics
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk());

        // Now verify the HTTP metric exists
        mockMvc.perform(get("/actuator/metrics/http.server.requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("http.server.requests")))
                .andExpect(jsonPath("$.measurements").isNotEmpty());
    }

    // -------------------------------------------------------
    // Custom Metrics
    // -------------------------------------------------------

    @Test
    void shouldExposeActiveUserCountGauge() throws Exception {
        mockMvc.perform(get("/actuator/metrics/api.users.active.count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("api.users.active.count")))
                .andExpect(jsonPath("$.measurements[0].value").isNumber());
    }

    @Test
    void shouldExposeGetAllUsersCounter() throws Exception {
        // Trigger the endpoint to register the counter
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/actuator/metrics/api.users.getAll.count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("api.users.getAll.count")))
                .andExpect(jsonPath("$.measurements[0].value").isNumber());
    }

    @Test
    void shouldExposeGetByIdUsersCounter() throws Exception {
        // Trigger the endpoint to register the counter
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/actuator/metrics/api.users.getById.count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("api.users.getById.count")))
                .andExpect(jsonPath("$.measurements[0].value").isNumber());
    }
}
