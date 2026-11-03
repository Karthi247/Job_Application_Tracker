package com.pratice.Job_Application_Tracker.dto;

import com.pratice.Job_Application_Tracker.enums.ApplicationPriority;
import com.pratice.Job_Application_Tracker.enums.ApplicationStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record ApplicationCreateRequest(
    @NotBlank @Size(max = 180) String companyName,
    @NotBlank @Size(max = 180) String jobTitle,
    String jobDescription,
    @Size(max = 180) String location,
    @Size(max = 32) String employmentType,
    @Size(max = 32) String workMode,
    @Size(max = 120) String salary,
    @Size(max = 2048) String jobUrl,
    @Size(max = 160) String recruiterName,
    @Email @Size(max = 254) String recruiterEmail,
    @Size(max = 32) String recruiterPhone,
    @Size(max = 2048) String resumeUrl,
    String coverLetter,
    ApplicationStatus status,
    ApplicationPriority priority,
    LocalDate appliedDate,
    LocalDate deadline,
    String notes
) {}
