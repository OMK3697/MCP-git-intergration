package com.example.mcpgitintegration.service.health.impl;

import com.example.mcpgitintegration.model.health.ComponentHealth;
import com.example.mcpgitintegration.service.health.DatabaseHealthChecker;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

/**
 * Checks MySQL connectivity by executing {@code SELECT 1}.
 * <p>
 * Errors are caught gracefully and reported as a DOWN status
 * with a descriptive error message.
 */
@Component
public class MySqlHealthChecker implements DatabaseHealthChecker {

    private static final String COMPONENT_NAME = "mysql";
    private static final String CHECK_QUERY = "SELECT 1";
    private static final int TIMEOUT_SECONDS = 5;

    private final DataSource dataSource;

    public MySqlHealthChecker(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String getComponentName() {
        return COMPONENT_NAME;
    }

    @Override
    public ComponentHealth check() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            statement.setQueryTimeout(TIMEOUT_SECONDS);
            statement.execute(CHECK_QUERY);

            return ComponentHealth.up("MySQL connection is healthy");
        } catch (Exception e) {
            return ComponentHealth.down("MySQL connection failed: " + e.getMessage());
        }
    }
}
