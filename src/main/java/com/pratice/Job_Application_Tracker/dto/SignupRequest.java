package com.pratice.Job_Application_Tracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(@NotBlank @Size(max = 160) String fullName,
    @NotBlank @Email @Size(max = 254) String email,
    @NotBlank @Size(max = 32) String mobile,
    @NotBlank @Size(min = 8, max = 72) String password) {}
