package com.pratice.Job_Application_Tracker.dto;

import com.pratice.Job_Application_Tracker.enums.ReminderType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record ReminderRequest(@NotNull ReminderType reminderType, @NotBlank @Size(max = 180) String title,
    @NotNull LocalDateTime remindAt, @Positive Long applicationId, @Positive Long interviewId) {}
