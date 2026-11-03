package com.pratice.Job_Application_Tracker.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pratice.Job_Application_Tracker.Repository.JobApplicationRepository;
import com.pratice.Job_Application_Tracker.Repository.UserAccountRepository;
import com.pratice.Job_Application_Tracker.domain.entity.UserAccount;
import com.pratice.Job_Application_Tracker.exception.ResourceNotFoundException;
import com.pratice.Job_Application_Tracker.service.impl.ApplicationServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceImplTest {
    @Mock private JobApplicationRepository applications;
    @Mock private UserAccountRepository users;
    @InjectMocks private ApplicationServiceImpl applicationService;

    @Test void getUsesAuthenticatedOwnerAndHidesAnotherUsersApplication() {
        UserAccount authenticatedUser = new UserAccount();
        authenticatedUser.setId(25L);
        when(users.findByEmailIgnoreCase("owner@example.com")).thenReturn(Optional.of(authenticatedUser));
        when(applications.findByIdAndUser_Id(42L, 25L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> applicationService.get("owner@example.com", 42L));

        verify(applications).findByIdAndUser_Id(42L, 25L);
        verify(applications, never()).findById(42L);
    }
}
