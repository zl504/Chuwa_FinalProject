package com.example.accountservice.service.impl;

import com.example.accountservice.dao.UserRepository;
import com.example.accountservice.entity.User;
import com.example.accountservice.payload.AuthResponse;
import com.example.accountservice.payload.LoginRequest;
import com.example.accountservice.payload.RegisterRequest;
import com.example.accountservice.security.JwtService;
import com.example.accountservice.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public AuthServiceImpl(UserRepository userRepo, PasswordEncoder encoder,
                           AuthenticationManager authManager, JwtService jwtService) {
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    @Override
    public void register(RegisterRequest req) {
        if (userRepo.existsByUsername(req.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        String hash = encoder.encode(req.getPassword());
        userRepo.save(new User(req.getUsername(), hash));
    }

    @Override
    public AuthResponse login(LoginRequest req) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );
        String token = jwtService.generateToken(auth);
        return new AuthResponse(token);
    }
}
