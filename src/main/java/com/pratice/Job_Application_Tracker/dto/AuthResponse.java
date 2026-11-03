package com.pratice.Job_Application_Tracker.dto;

public record AuthResponse(String accessToken, String tokenType, long expiresIn, UserResponse user) {}
