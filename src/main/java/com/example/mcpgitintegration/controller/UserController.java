package com.example.mcpgitintegration.controller;

import com.example.mcpgitintegration.model.Response;
import com.example.mcpgitintegration.model.User;
import com.example.mcpgitintegration.service.UserService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for User-related endpoints.
 * <p>
 * Custom Micrometer counters track the number of calls to each endpoint.
 * HTTP latency / percentile metrics are automatically captured by Actuator's
 * {@code http.server.requests} meter.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final Counter getAllUsersCounter;
    private final Counter getUserByIdCounter;

    public UserController(UserService userService, MeterRegistry meterRegistry) {
        this.userService = userService;
        this.getAllUsersCounter = Counter.builder("api.users.getAll.count")
                .description("Number of calls to GET /api/users")
                .tag("endpoint", "/api/users")
                .register(meterRegistry);
        this.getUserByIdCounter = Counter.builder("api.users.getById.count")
                .description("Number of calls to GET /api/users/{id}")
                .tag("endpoint", "/api/users/{id}")
                .register(meterRegistry);
    }

    @GetMapping
    public Response<List<User>> getAllUsers() {
        getAllUsersCounter.increment();
        List<User> users = userService.getAllUsers();
        return Response.success(users);
    }

    @GetMapping("/{id}")
    public Response<User> getUserById(@PathVariable Long id) {
        getUserByIdCounter.increment();
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            return Response.success(user.get());
        }
        return Response.error(HttpStatus.NOT_FOUND, "User not found with id: " + id);
    }

    @GetMapping("/search")
    public ResponseEntity<Response<User>> getUserByName(@RequestParam String name) {
        Optional<User> user = userService.getUserByName(name);
        if (user.isPresent()) {
            return Response.success(user.get());
        }
        return Response.error(HttpStatus.NOT_FOUND, "User not found with name: " + name);
    }
}
