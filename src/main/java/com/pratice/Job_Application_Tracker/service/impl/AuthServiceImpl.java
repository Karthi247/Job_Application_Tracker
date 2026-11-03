package com.pratice.Job_Application_Tracker.service.impl;

import com.pratice.Job_Application_Tracker.Repository.UserAccountRepository;
import com.pratice.Job_Application_Tracker.domain.entity.UserAccount;
import com.pratice.Job_Application_Tracker.dto.AuthResponse;
import com.pratice.Job_Application_Tracker.dto.LoginRequest;
import com.pratice.Job_Application_Tracker.dto.SignupRequest;
import com.pratice.Job_Application_Tracker.dto.UserResponse;
import com.pratice.Job_Application_Tracker.exception.EmailAlreadyRegisteredException;
import com.pratice.Job_Application_Tracker.exception.MobileAlreadyRegisteredException;
import com.pratice.Job_Application_Tracker.security.JwtTokenService;
import com.pratice.Job_Application_Tracker.service.AuthService;
import java.util.Locale;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserAccountRepository users;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService tokenService;

    public AuthServiceImpl(UserAccountRepository users, PasswordEncoder passwordEncoder,
        AuthenticationManager authenticationManager, JwtTokenService tokenService) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @Override
    @Transactional
    public AuthResponse signup(SignupRequest request) {
        String email = normalizeEmail(request.email());
        String mobile = request.mobile().trim();
        if (users.existsByEmailIgnoreCase(email)) throw new EmailAlreadyRegisteredException();
        if (users.existsByMobile(mobile)) throw new MobileAlreadyRegisteredException();
        UserAccount user = new UserAccount();
        user.setFullName(request.fullName().trim());
        user.setEmail(email);
        user.setMobile(mobile);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user = users.saveAndFlush(user);
        return response(user);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String email = normalizeEmail(request.email());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, request.password()));
        UserAccount user = users.findByEmailIgnoreCase(email).orElseThrow();
        return response(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse currentUser(String email) {
        return users.findByEmailIgnoreCase(normalizeEmail(email)).map(UserResponse::from).orElseThrow();
    }

    private AuthResponse response(UserAccount user) {
        return new AuthResponse(tokenService.issueToken(user.getEmail()), "Bearer",
            tokenService.getExpirationMillis() / 1000, UserResponse.from(user));
    }

    private String normalizeEmail(String email) { return email.trim().toLowerCase(Locale.ROOT); }
}
