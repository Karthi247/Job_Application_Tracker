package com.pratice.Job_Application_Tracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pratice.Job_Application_Tracker.Repository.UserAccountRepository;
import com.pratice.Job_Application_Tracker.domain.entity.UserAccount;
import com.pratice.Job_Application_Tracker.dto.AuthResponse;
import com.pratice.Job_Application_Tracker.dto.SignupRequest;
import com.pratice.Job_Application_Tracker.security.JwtTokenService;
import com.pratice.Job_Application_Tracker.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock private UserAccountRepository users;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private JwtTokenService tokenService;
    private AuthServiceImpl authService;

    @BeforeEach void setUp() { authService = new AuthServiceImpl(users, passwordEncoder, authenticationManager, tokenService); }

    @Test void signupNormalizesEmailHashesPasswordAndReturnsNoPasswordField() {
        when(users.existsByEmailIgnoreCase("person@example.com")).thenReturn(false);
        when(users.existsByMobile("5551234567")).thenReturn(false);
        when(passwordEncoder.encode("safe-password")).thenReturn("$2a$encoded-value");
        when(users.saveAndFlush(any(UserAccount.class))).thenAnswer(invocation -> {
            UserAccount account = invocation.getArgument(0);
            account.setId(7L);
            return account;
        });
        when(tokenService.issueToken("person@example.com")).thenReturn("signed.jwt.token");
        when(tokenService.getExpirationMillis()).thenReturn(900_000L);

        AuthResponse response = authService.signup(new SignupRequest("Person Example", " Person@Example.com ", " 5551234567 ", "safe-password"));

        assertEquals("signed.jwt.token", response.accessToken());
        assertEquals("person@example.com", response.user().email());
        assertEquals("Person Example", response.user().fullName());
        assertFalse(response.toString().contains("password"));
        ArgumentCaptor<UserAccount> saved = ArgumentCaptor.forClass(UserAccount.class);
        verify(users).saveAndFlush(saved.capture());
        assertEquals("$2a$encoded-value", saved.getValue().getPasswordHash());
        verify(passwordEncoder).encode("safe-password");
    }
}
