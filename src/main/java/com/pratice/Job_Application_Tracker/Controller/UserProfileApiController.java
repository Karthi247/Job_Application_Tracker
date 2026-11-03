package com.pratice.Job_Application_Tracker.Controller;

import com.pratice.Job_Application_Tracker.dto.ChangePasswordRequest;
import com.pratice.Job_Application_Tracker.dto.ProfileUpdateRequest;
import com.pratice.Job_Application_Tracker.dto.UserResponse;
import com.pratice.Job_Application_Tracker.service.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserProfileApiController {
    private final UserProfileService profiles;
    public UserProfileApiController(UserProfileService profiles) { this.profiles = profiles; }
    @GetMapping("/profile")
    public UserResponse profile(Authentication authentication) { return profiles.getProfile(authentication.getName()); }
    @PutMapping("/profile")
    public UserResponse updateProfile(Authentication authentication, @Valid @RequestBody ProfileUpdateRequest request) {
        return profiles.updateProfile(authentication.getName(), request);
    }
    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(Authentication authentication,
        @Valid @RequestBody ChangePasswordRequest request) {
        profiles.changePassword(authentication.getName(), request);
        return ResponseEntity.noContent().build();
    }
}
