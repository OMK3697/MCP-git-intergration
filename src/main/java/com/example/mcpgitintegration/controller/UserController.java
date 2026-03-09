package com.example.mcpgitintegration.controller;

import com.example.mcpgitintegration.model.Response;
import com.example.mcpgitintegration.model.User;
import com.example.mcpgitintegration.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public Response<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return Response.success(users);
    }

    @GetMapping("/{id}")
    public Response<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            return Response.success(user.get());
        }
        return Response.error(404, "User not found with id: " + id);
    }
}
