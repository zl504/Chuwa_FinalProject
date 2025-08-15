package com.example.accountservice.service.impl;



import com.example.accountservice.dao.UserRepository;
import com.example.accountservice.entity.User;
import com.example.accountservice.payload.UpdateAccountRequest;
import com.example.accountservice.payload.UserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock UserRepository userRepository;

    @InjectMocks AccountServiceImpl accountService;

    @Test
    void updateAccount_updatesOnlyNonNullFields() {
        // existing user in DB
        User existing = new User();
        existing.setId(42L);
        existing.setUsername("alice");
        existing.setShippingAddress("old-ship");
        existing.setBillingAddress("old-bill");
        existing.setPaymentMethod("OLD-PM");

        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        // change shipping + paymentMethod; leave billingAddress null (should stay unchanged)
        UpdateAccountRequest req = new UpdateAccountRequest();
        req.setShippingAddress("new-ship");
        req.setPaymentMethod("NEW-PM");

        UserResponse resp = accountService.updateMe("alice", req);

        ArgumentCaptor<User> cap = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(cap.capture());
        User saved = cap.getValue();

        assertThat(saved.getUsername()).isEqualTo("alice");
        assertThat(saved.getShippingAddress()).isEqualTo("new-ship");
        assertThat(saved.getPaymentMethod()).isEqualTo("NEW-PM");
        // unchanged because request had null
        assertThat(saved.getBillingAddress()).isEqualTo("old-bill");

        // response mapping
        assertThat(resp.getUsername()).isEqualTo("alice");
        assertThat(resp.getShippingAddress()).isEqualTo("new-ship");
        assertThat(resp.getBillingAddress()).isEqualTo("old-bill");
        assertThat(resp.getPaymentMethod()).isEqualTo("NEW-PM");
    }


    @Test
    void updateAccount_userNotFound_throws() {
        when(userRepository.findByUsername("nope")).thenReturn(Optional.empty());

        UpdateAccountRequest req = new UpdateAccountRequest();
        req.setShippingAddress("x");

        assertThrows(RuntimeException.class,
                () -> accountService.updateMe("nope", req));

        verify(userRepository, never()).save(any());
    }
}
