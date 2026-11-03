package com.pratice.Job_Application_Tracker.service.impl;

import com.pratice.Job_Application_Tracker.Repository.UserAccountRepository;
import com.pratice.Job_Application_Tracker.domain.entity.UserAccount;
import com.pratice.Job_Application_Tracker.dto.ChangePasswordRequest;
import com.pratice.Job_Application_Tracker.dto.ProfileUpdateRequest;
import com.pratice.Job_Application_Tracker.dto.UserResponse;
import com.pratice.Job_Application_Tracker.exception.EmailAlreadyRegisteredException;
import com.pratice.Job_Application_Tracker.exception.InvalidCurrentPasswordException;
import com.pratice.Job_Application_Tracker.exception.MobileAlreadyRegisteredException;
import com.pratice.Job_Application_Tracker.exception.ResourceNotFoundException;
import com.pratice.Job_Application_Tracker.service.UserProfileService;
import java.util.Locale;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserProfileServiceImpl implements UserProfileService {
    private final UserAccountRepository users;
    private final PasswordEncoder passwordEncoder;
    public UserProfileServiceImpl(UserAccountRepository users, PasswordEncoder passwordEncoder) {
        this.users = users; this.passwordEncoder = passwordEncoder;
    }

    @Override @Transactional(readOnly = true)
    public UserResponse getProfile(String email) { return UserResponse.from(findUser(email)); }

    @Override @Transactional
    public UserResponse updateProfile(String email, ProfileUpdateRequest request) {
        UserAccount user = findUser(email);
        String updatedEmail = request.email().trim().toLowerCase(Locale.ROOT);
        String updatedMobile = request.mobile().trim();
        if (users.existsByEmailIgnoreCaseAndIdNot(updatedEmail, user.getId())) throw new EmailAlreadyRegisteredException();
        if (users.existsByMobileAndIdNot(updatedMobile, user.getId())) throw new MobileAlreadyRegisteredException();
        user.setFullName(request.fullName().trim());
        user.setEmail(updatedEmail);
        user.setMobile(updatedMobile);
        return UserResponse.from(users.save(user));
    }

    @Override @Transactional
    public void changePassword(String email, ChangePasswordRequest request) {
        UserAccount user = findUser(email);
        if (!passwordEncoder.matches(request.currentPassword(), user.getPasswordHash()))
            throw new InvalidCurrentPasswordException();
        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        users.save(user);
    }

    private UserAccount findUser(String email) {
        return users.findByEmailIgnoreCase(email).orElseThrow(() -> new ResourceNotFoundException("User"));
    }
}
