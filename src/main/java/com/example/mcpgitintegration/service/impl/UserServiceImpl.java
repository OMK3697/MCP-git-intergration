package com.example.mcpgitintegration.service.impl;

import com.example.mcpgitintegration.model.User;
import com.example.mcpgitintegration.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final List<User> users = new ArrayList<>();

    @PostConstruct
    public void init() {
        users.add(new User(1L, "John Doe", "john.doe@example.com"));
        users.add(new User(2L, "Jane Smith", "jane.smith@example.com"));
        users.add(new User(3L, "Bob Johnson", "bob.johnson@example.com"));
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
        return users.stream()
                .filter(user -> user.getName().equalsIgnoreCase(name))
                .findFirst();
    }
}
