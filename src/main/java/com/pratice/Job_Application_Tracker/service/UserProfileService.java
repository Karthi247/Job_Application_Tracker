package com.pratice.Job_Application_Tracker.service;

import com.pratice.Job_Application_Tracker.dto.ChangePasswordRequest;
import com.pratice.Job_Application_Tracker.dto.ProfileUpdateRequest;
import com.pratice.Job_Application_Tracker.dto.UserResponse;

public interface UserProfileService {
    UserResponse getProfile(String email);
    UserResponse updateProfile(String email, ProfileUpdateRequest request);
    void changePassword(String email, ChangePasswordRequest request);
}
