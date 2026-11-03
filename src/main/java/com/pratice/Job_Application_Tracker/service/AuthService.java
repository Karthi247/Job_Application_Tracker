package com.pratice.Job_Application_Tracker.service;

import com.pratice.Job_Application_Tracker.dto.AuthResponse;
import com.pratice.Job_Application_Tracker.dto.LoginRequest;
import com.pratice.Job_Application_Tracker.dto.SignupRequest;
import com.pratice.Job_Application_Tracker.dto.UserResponse;

public interface AuthService {
    AuthResponse signup(SignupRequest request);
    AuthResponse login(LoginRequest request);
    UserResponse currentUser(String email);
}
