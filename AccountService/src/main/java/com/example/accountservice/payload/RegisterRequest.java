package com.example.accountservice.payload;

import jakarta.validation.constraints.*;

public class RegisterRequest {
    @NotBlank
    @Size(min = 3, max = 50)
    private String username;
    @NotBlank
    @Size(min = 6, max = 128)
    private String password;

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
}
