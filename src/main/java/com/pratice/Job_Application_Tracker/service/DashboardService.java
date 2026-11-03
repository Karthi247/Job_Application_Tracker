package com.pratice.Job_Application_Tracker.service;

import com.pratice.Job_Application_Tracker.dto.ApplicationResponse;
import com.pratice.Job_Application_Tracker.dto.DashboardSummaryResponse;
import com.pratice.Job_Application_Tracker.dto.InterviewResponse;
import com.pratice.Job_Application_Tracker.dto.PageResponse;

public interface DashboardService {
    DashboardSummaryResponse summary(String email);
    PageResponse<ApplicationResponse> recentApplications(String email);
    PageResponse<InterviewResponse> upcomingInterviews(String email);
    PageResponse<ApplicationResponse> upcomingDeadlines(String email);
}
