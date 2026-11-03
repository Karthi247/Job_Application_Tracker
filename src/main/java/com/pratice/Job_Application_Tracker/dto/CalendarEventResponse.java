package com.pratice.Job_Application_Tracker.dto;

import java.time.LocalDateTime;

public record CalendarEventResponse(String id, String eventType, String title, LocalDateTime start,
    Long applicationId, Long interviewId, Long reminderId, String companyName, String jobTitle) {}
