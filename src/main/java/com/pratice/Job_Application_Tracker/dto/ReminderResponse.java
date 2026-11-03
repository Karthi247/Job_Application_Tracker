package com.pratice.Job_Application_Tracker.dto;

import com.pratice.Job_Application_Tracker.domain.entity.Reminder;
import com.pratice.Job_Application_Tracker.enums.ReminderType;
import java.time.LocalDateTime;

public record ReminderResponse(Long id, Long applicationId, Long interviewId, ReminderType reminderType,
    String title, LocalDateTime remindAt, LocalDateTime completedAt, LocalDateTime createdAt) {
    public static ReminderResponse from(Reminder r) {
        return new ReminderResponse(r.getId(), r.getApplication() == null ? null : r.getApplication().getId(),
            r.getInterview() == null ? null : r.getInterview().getId(), r.getReminderType(), r.getTitle(),
            r.getRemindAt(), r.getCompletedAt(), r.getCreatedAt());
    }
}
