package com.example.mcpgitintegration.controller;

import com.example.mcpgitintegration.model.Response;
import com.example.mcpgitintegration.model.User;
import com.example.mcpgitintegration.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<Response<List<User>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return Response.success(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<User>> getUserById(@PathVariable Long id) {
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
