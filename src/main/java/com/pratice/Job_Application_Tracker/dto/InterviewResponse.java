package com.pratice.Job_Application_Tracker.dto;

import com.pratice.Job_Application_Tracker.domain.entity.Interview;
import com.pratice.Job_Application_Tracker.enums.InterviewStatus;
import com.pratice.Job_Application_Tracker.enums.InterviewType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record InterviewResponse(Long id, Long applicationId, String companyName, String jobTitle,
    String interviewRound, InterviewType interviewType, LocalDate interviewDate, LocalTime interviewTime,
    String meetingLink, String interviewerName, String interviewerEmail, String location, String notes,
    InterviewStatus status, LocalDateTime createdAt) {
    public static InterviewResponse from(Interview i) {
        return new InterviewResponse(i.getId(), i.getApplication().getId(), i.getApplication().getCompanyName(),
            i.getApplication().getJobTitle(), i.getInterviewRound(), i.getInterviewType(), i.getInterviewDate(),
            i.getInterviewTime(), i.getMeetingLink(), i.getInterviewerName(), i.getInterviewerEmail(),
            i.getLocation(), i.getNotes(), i.getStatus(), i.getCreatedAt());
    }
}
