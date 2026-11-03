package com.pratice.Job_Application_Tracker.dto;

import com.pratice.Job_Application_Tracker.enums.ApplicationStatus;

public interface StatusCountProjection {
    ApplicationStatus getStatus();
    Long getCount();
}
