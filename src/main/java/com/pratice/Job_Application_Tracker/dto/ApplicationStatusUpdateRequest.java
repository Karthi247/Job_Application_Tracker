package com.pratice.Job_Application_Tracker.dto;

import com.pratice.Job_Application_Tracker.enums.ApplicationStatus;
import jakarta.validation.constraints.NotNull;

public record ApplicationStatusUpdateRequest(@NotNull ApplicationStatus status) {}
