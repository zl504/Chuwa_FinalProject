package com.example.accountservice.service;

import com.example.accountservice.payload.AuthResponse;
import com.example.accountservice.payload.LoginRequest;
import com.example.accountservice.payload.RegisterRequest;

public interface AuthService {
    void register(RegisterRequest req);
    AuthResponse login(LoginRequest req);
}
