package com.api.utils.Models;

public class LoginRequest {
    private  final String email;
    private final String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters (RestAssured/Jackson uses these for serialization)
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
}