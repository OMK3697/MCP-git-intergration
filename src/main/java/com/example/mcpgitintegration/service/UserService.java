package com.example.mcpgitintegration.service;

import com.example.mcpgitintegration.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAllUsers();

    Optional<User> getUserById(Long id);
}
