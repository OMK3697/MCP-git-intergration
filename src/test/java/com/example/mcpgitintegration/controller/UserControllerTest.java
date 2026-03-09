package com.example.mcpgitintegration.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getUserByNameShouldReturnUserWhenFound() throws Exception {
        mockMvc.perform(get("/api/users/search").param("name", "John Doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.name").value("John Doe"));
    }

    @Test
    void getUserByNameShouldReturnUserWhenNameCaseDiffers() throws Exception {
        mockMvc.perform(get("/api/users/search").param("name", "john doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.name").value("John Doe"));
    }

    @Test
    void getUserByNameShouldReturnNotFoundWhenMissing() throws Exception {
        mockMvc.perform(get("/api/users/search").param("name", "Unknown User"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("User not found with name: Unknown User"));
    }

}
