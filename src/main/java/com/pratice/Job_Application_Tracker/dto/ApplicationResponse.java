package com.pratice.Job_Application_Tracker.dto;

import com.pratice.Job_Application_Tracker.domain.entity.JobApplication;
import com.pratice.Job_Application_Tracker.enums.ApplicationPriority;
import com.pratice.Job_Application_Tracker.enums.ApplicationStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ApplicationResponse(Long id, String companyName, String jobTitle, String jobDescription,
    String location, String employmentType, String workMode, String salary, String jobUrl,
    String recruiterName, String recruiterEmail, String recruiterPhone, String resumeUrl,
    String coverLetter, ApplicationStatus status, ApplicationPriority priority, LocalDate appliedDate,
    LocalDate deadline, String notes, LocalDateTime createdAt, LocalDateTime updatedAt) {
    public static ApplicationResponse from(JobApplication a) {
        return new ApplicationResponse(a.getId(), a.getCompanyName(), a.getJobTitle(), a.getJobDescription(),
            a.getLocation(), a.getEmploymentType(), a.getWorkMode(), a.getSalary(), a.getJobUrl(),
            a.getRecruiterName(), a.getRecruiterEmail(), a.getRecruiterPhone(), a.getResumeUrl(),
            a.getCoverLetter(), a.getStatus(), a.getPriority(), a.getAppliedDate(), a.getDeadline(),
            a.getNotes(), a.getCreatedAt(), a.getUpdatedAt());
    }
}
