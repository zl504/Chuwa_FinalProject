package com.example.accountservice.service.impl;



import com.example.accountservice.dao.UserRepository;
import com.example.accountservice.entity.User;
import com.example.accountservice.payload.AuthResponse;
import com.example.accountservice.payload.LoginRequest;
import com.example.accountservice.payload.RegisterRequest;
import com.example.accountservice.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    UserRepository userRepository;
    @Mock
    JwtService jwtService;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    AuthServiceImpl authService;

    @Test
    void login_authenticates_andReturnsJwt() {
        // Arrange
        var req = new LoginRequest();
        req.setUsername("alice");
        req.setPassword("secret");

        Authentication mockAuth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenReturn(mockAuth);
        when(jwtService.generateToken(mockAuth)).thenReturn("jwt-token-123");

        // Act
        AuthResponse resp = authService.login(req);

        // Assert
        ArgumentCaptor<UsernamePasswordAuthenticationToken> captor =
                ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
        verify(authenticationManager).authenticate(captor.capture());
        assertThat(captor.getValue().getPrincipal()).isEqualTo("alice");
        assertThat(captor.getValue().getCredentials()).isEqualTo("secret");

        verify(jwtService).generateToken(mockAuth);
        assertThat(resp).isNotNull();
        assertThat(resp.getToken()).isEqualTo("jwt-token-123");
    }

    @Test
    void register_hashesPassword_andSavesUser() {
        // If your method name/signature differs (e.g., returns AuthResponse),
        // adjust the call + assertions below.
        var req = new RegisterRequest();
        req.setEmail("a@b.com");
        req.setUsername("alice");
        req.setPassword("plain");
        req.setShippingAddress("S1");
        req.setBillingAddress("B1");
        req.setPaymentMethod("VISA");

        when(passwordEncoder.encode("plain")).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        // Assuming: public void register(RegisterRequest req)
        authService.register(req);

        // Assert
        ArgumentCaptor<User> userCap = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCap.capture());
        User saved = userCap.getValue();

        assertThat(saved.getEmail()).isEqualTo("a@b.com");
        assertThat(saved.getUsername()).isEqualTo("alice");
        assertThat(saved.getPasswordHash()).isEqualTo("hashed"); // encoded
        assertThat(saved.getShippingAddress()).isEqualTo("S1");
        assertThat(saved.getBillingAddress()).isEqualTo("B1");
        assertThat(saved.getPaymentMethod()).isEqualTo("VISA");

        verify(passwordEncoder).encode("plain");
        verifyNoMoreInteractions(jwtService); // register shouldn’t JWT unless your impl does
    }
}