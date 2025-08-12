package com.example.accountservice.controller;


import com.example.accountservice.dao.UserRepository;
import com.example.accountservice.payload.UpdateAccountRequest;
import com.example.accountservice.payload.UserResponse;
import com.example.accountservice.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;
    private final UserRepository userRepo;

    public AccountController(AccountService accountService, UserRepository userRepo) {
        this.accountService = accountService;
        this.userRepo = userRepo;
    }

    // Partial update of the authenticated user's profile
    @PatchMapping("/update")
    public ResponseEntity<UserResponse> updateMe(@Valid @RequestBody UpdateAccountRequest req,
                                                 Authentication auth) {
        if (auth == null) return ResponseEntity.status(401).build();
        UserResponse resp = accountService.updateMe(auth.getName(), req);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(Authentication auth) {
        if (auth == null) return ResponseEntity.status(401).build();
        var user = userRepo.findByUsername(auth.getName()).orElse(null);
        if (user == null) return ResponseEntity.status(404).build();
        return ResponseEntity.ok(new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getShippingAddress(),
                user.getBillingAddress(),
                user.getPaymentMethod()
        ));
    }
}
