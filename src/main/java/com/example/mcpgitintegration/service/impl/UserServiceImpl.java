package com.example.mcpgitintegration.service.impl;

import com.example.mcpgitintegration.model.User;
import com.example.mcpgitintegration.service.UserService;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * In-memory implementation of {@link UserService}.
 * <p>
 * Registers a Micrometer gauge that tracks the current number of users.
 */
@Service
public class UserServiceImpl implements UserService {

    private final List<User> users = new ArrayList<>();
    private final MeterRegistry meterRegistry;

    public UserServiceImpl(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @PostConstruct
    public void init() {
        users.add(new User(1L, "John Doe", "john.doe@example.com"));
        users.add(new User(2L, "Jane Smith", "jane.smith@example.com"));
        users.add(new User(3L, "Bob Johnson", "bob.johnson@example.com"));

        // Register a gauge that always reflects the current user count
        Gauge.builder("api.users.active.count", users, List::size)
                .description("Current number of active users in the system")
                .register(meterRegistry);
    }

    @Override
    public List<User> getAllUsers() {
        return users;
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<User> getUserByName(String name) {
        if (name == null) return Optional.empty();
        return users.stream()
                .filter(user -> name.equalsIgnoreCase(user.getName()))
                .findFirst();
    }
}
