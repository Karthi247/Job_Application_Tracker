package com.pratice.Job_Application_Tracker.dto;

import com.pratice.Job_Application_Tracker.domain.entity.UserAccount;
import java.time.LocalDateTime;

public record UserResponse(Long id, String fullName, String email, String mobile,
    LocalDateTime createdAt, LocalDateTime updatedAt) {
    public static UserResponse from(UserAccount user) {
        return new UserResponse(user.getId(), user.getFullName(), user.getEmail(),
            user.getMobile(), user.getCreatedAt(), user.getUpdatedAt());
    }
}
