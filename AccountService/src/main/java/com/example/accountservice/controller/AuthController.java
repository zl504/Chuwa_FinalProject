package com.example.accountservice.controller;


import com.example.accountservice.dao.UserRepository;
import com.example.accountservice.payload.AuthResponse;
import com.example.accountservice.payload.LoginRequest;
import com.example.accountservice.payload.RegisterRequest;
import com.example.accountservice.payload.UserResponse;
import com.example.accountservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final UserRepository userRepo;

//
//    @GetMapping("/ping")
//    public String ping() { return "pong"; }

    public AuthController(AuthService authService, UserRepository userRepo) {
        this.authService = authService;
        this.userRepo = userRepo;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest req) {
        authService.register(req);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(Authentication auth) {
        if (auth == null) return ResponseEntity.status(401).build();
        var user = userRepo.findByUsername(auth.getName()).orElse(null);
        if (user == null) return ResponseEntity.status(404).build();
        return ResponseEntity.ok(new UserResponse(user.getId(), user.getUsername()));
    }
}
