package com.pratice.Job_Application_Tracker.service;

import com.pratice.Job_Application_Tracker.enums.ReminderType;
import java.time.LocalDateTime;

public record ReminderDueEvent(Long reminderId, Long userId, ReminderType type, LocalDateTime remindAt) {}
