package com.pratice.Job_Application_Tracker.dto;

import com.pratice.Job_Application_Tracker.enums.InterviewStatus;
import com.pratice.Job_Application_Tracker.enums.InterviewType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;

public record InterviewRequest(@NotNull @Positive Long applicationId,
    @NotBlank @Size(max = 120) String interviewRound, @NotNull InterviewType interviewType,
    @NotNull LocalDate interviewDate, LocalTime interviewTime, @Size(max = 2048) String meetingLink,
    @Size(max = 160) String interviewerName, @Email @Size(max = 254) String interviewerEmail,
    @Size(max = 255) String location, String notes, InterviewStatus status) {}
