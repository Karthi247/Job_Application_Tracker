package com.pratice.Job_Application_Tracker.service;

import com.pratice.Job_Application_Tracker.dto.ApplicationCreateRequest;
import com.pratice.Job_Application_Tracker.dto.ApplicationResponse;
import com.pratice.Job_Application_Tracker.dto.PageResponse;
import com.pratice.Job_Application_Tracker.enums.ApplicationPriority;
import com.pratice.Job_Application_Tracker.enums.ApplicationStatus;
import java.time.LocalDate;

public interface ApplicationService {
    ApplicationResponse create(String email, ApplicationCreateRequest request);
    PageResponse<ApplicationResponse> list(String email, int page, int size, String sortBy, String direction,
        String search, ApplicationStatus status, ApplicationPriority priority, LocalDate appliedFrom, LocalDate appliedTo);
    ApplicationResponse get(String email, Long id);
    ApplicationResponse update(String email, Long id, ApplicationCreateRequest request);
    ApplicationResponse updateStatus(String email, Long id, ApplicationStatus status);
    void delete(String email, Long id);
}
