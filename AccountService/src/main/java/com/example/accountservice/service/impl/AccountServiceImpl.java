package com.example.accountservice.service.impl;

import com.example.accountservice.dao.UserRepository;
import com.example.accountservice.entity.User;
import com.example.accountservice.payload.UpdateAccountRequest;
import com.example.accountservice.payload.UserResponse;
import com.example.accountservice.service.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class AccountServiceImpl implements AccountService {
    private final UserRepository userRepo;

    public AccountServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    @Transactional
    public UserResponse updateMe(String username, UpdateAccountRequest req) {
        if ((req.getShippingAddress() == null || req.getShippingAddress().isBlank()) &&
                (req.getBillingAddress()  == null || req.getBillingAddress().isBlank())  &&
                (req.getPaymentMethod()   == null || req.getPaymentMethod().isBlank())) {
            throw new IllegalArgumentException("At least one field must be provided.");
        }

        User u = userRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (req.getShippingAddress() != null && !req.getShippingAddress().isBlank()) {
            u.setShippingAddress(req.getShippingAddress());
        }
        if (req.getBillingAddress() != null && !req.getBillingAddress().isBlank()) {
            u.setBillingAddress(req.getBillingAddress());
        }
        if (req.getPaymentMethod() != null && !req.getPaymentMethod().isBlank()) {
            u.setPaymentMethod(req.getPaymentMethod());
        }

        User saved = userRepo.save(u);
        return new UserResponse(
                saved.getId(),
                saved.getEmail(),
                saved.getUsername(),
                saved.getShippingAddress(),
                saved.getBillingAddress(),
                saved.getPaymentMethod()
        );
    }
}
