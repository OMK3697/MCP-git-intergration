package com.example.mcpgitintegration.service.health.impl;

import com.example.mcpgitintegration.model.health.ComponentHealth;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link MongoHealthChecker}.
 */
class MongoHealthCheckerTest {

    private MongoTemplate mongoTemplate;
    private MongoDatabase mongoDatabase;
    private MongoHealthChecker checker;

    @BeforeEach
    void setUp() {
        mongoTemplate = mock(MongoTemplate.class);
        mongoDatabase = mock(MongoDatabase.class);

        when(mongoTemplate.getDb()).thenReturn(mongoDatabase);

        checker = new MongoHealthChecker(mongoTemplate);
    }

    @Test
    @DisplayName("getComponentName returns 'mongo'")
    void getComponentName_returnsMongo() {
        assertEquals("mongo", checker.getComponentName());
    }

    @Test
    @DisplayName("check returns UP when MongoDB ping succeeds")
    void check_pingSucceeds_returnsUp() {
        when(mongoDatabase.runCommand(any(Document.class))).thenReturn(new Document("ok", 1.0));

        ComponentHealth health = checker.check();

        assertEquals("UP", health.getStatus());
        assertEquals("MongoDB connection is healthy", health.getDetails());
        verify(mongoDatabase).runCommand(any(Document.class));
    }

    @Test
    @DisplayName("check returns DOWN when MongoDB ping fails")
    void check_pingFails_returnsDown() {
        when(mongoDatabase.runCommand(any(Document.class)))
                .thenThrow(new RuntimeException("Connection refused"));

        ComponentHealth health = checker.check();

        assertEquals("DOWN", health.getStatus());
        assertEquals("MongoDB connection failed: Connection refused", health.getDetails());
    }

    @Test
    @DisplayName("check returns DOWN when MongoTemplate.getDb() throws")
    void check_getDbThrows_returnsDown() {
        when(mongoTemplate.getDb()).thenThrow(new RuntimeException("Cannot connect to MongoDB"));

        ComponentHealth health = checker.check();

        assertEquals("DOWN", health.getStatus());
        assertEquals("MongoDB connection failed: Cannot connect to MongoDB", health.getDetails());
    }
}
