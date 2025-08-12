package com.example.accountservice.service;

import com.example.accountservice.payload.UpdateAccountRequest;
import com.example.accountservice.payload.UserResponse;

public interface AccountService {
    UserResponse updateMe(String username, UpdateAccountRequest req);
}