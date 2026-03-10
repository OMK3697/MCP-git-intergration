package com.example.mcpgitintegration.model;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Response<T> {

    private int status;
    private String message;
    private T data;

    public Response() {
    }

    public Response(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseEntity<Response<T>> success(T data) {
        Response<T> response = new Response<>(200, "Success", data);
        return ResponseEntity.ok(response);
    }

    public static <T> ResponseEntity<Response<T>> error(HttpStatus httpStatus, String message) {
        Response<T> response = new Response<>(httpStatus.value(), message, null);
        return ResponseEntity.status(httpStatus).body(response);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
