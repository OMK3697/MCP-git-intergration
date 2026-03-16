package com.example.mcpgitintegration.service.health.impl;

import com.example.mcpgitintegration.model.health.ComponentHealth;
import com.example.mcpgitintegration.service.health.DatabaseHealthChecker;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

/**
 * Checks MongoDB connectivity by executing a {@code ping} command.
 * <p>
 * Errors are caught gracefully and reported as a DOWN status
 * with a descriptive error message.
 */
@Component
public class MongoHealthChecker implements DatabaseHealthChecker {

    private static final String COMPONENT_NAME = "mongo";

    private final MongoTemplate mongoTemplate;

    public MongoHealthChecker(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public String getComponentName() {
        return COMPONENT_NAME;
    }

    @Override
    public ComponentHealth check() {
        try {
            Document pingCommand = new Document("ping", 1);
            mongoTemplate.getDb().runCommand(pingCommand);

            return ComponentHealth.up("MongoDB connection is healthy");
        } catch (Exception e) {
            return ComponentHealth.down("MongoDB connection failed: " + e.getMessage());
        }
    }
}
