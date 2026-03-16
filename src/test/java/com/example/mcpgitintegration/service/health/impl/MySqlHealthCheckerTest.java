package com.example.mcpgitintegration.service.health.impl;

import com.example.mcpgitintegration.model.health.ComponentHealth;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link MySqlHealthChecker}.
 */
class MySqlHealthCheckerTest {

    private DataSource dataSource;
    private Connection connection;
    private Statement statement;
    private MySqlHealthChecker checker;

    @BeforeEach
    void setUp() throws Exception {
        dataSource = mock(DataSource.class);
        connection = mock(Connection.class);
        statement = mock(Statement.class);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);

        checker = new MySqlHealthChecker(dataSource);
    }

    @Test
    @DisplayName("getComponentName returns 'mysql'")
    void getComponentName_returnsMysql() {
        assertEquals("mysql", checker.getComponentName());
    }

    @Test
    @DisplayName("check returns UP when MySQL connection succeeds")
    void check_connectionSucceeds_returnsUp() throws Exception {
        when(statement.execute("SELECT 1")).thenReturn(true);

        ComponentHealth health = checker.check();

        assertEquals("UP", health.getStatus());
        assertEquals("MySQL connection is healthy", health.getDetails());
        verify(statement).setQueryTimeout(5);
        verify(statement).execute("SELECT 1");
    }

    @Test
    @DisplayName("check returns DOWN when MySQL connection fails")
    void check_connectionFails_returnsDown() throws Exception {
        when(dataSource.getConnection()).thenThrow(new SQLException("Connection refused"));

        ComponentHealth health = checker.check();

        assertEquals("DOWN", health.getStatus());
        assertEquals("MySQL connection failed: Connection refused", health.getDetails());
    }

    @Test
    @DisplayName("check returns DOWN when statement execution fails")
    void check_statementFails_returnsDown() throws Exception {
        when(statement.execute("SELECT 1")).thenThrow(new SQLException("Query timeout"));

        ComponentHealth health = checker.check();

        assertEquals("DOWN", health.getStatus());
        assertEquals("MySQL connection failed: Query timeout", health.getDetails());
    }
}
