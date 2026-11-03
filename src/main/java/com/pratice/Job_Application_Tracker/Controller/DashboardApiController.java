package com.pratice.Job_Application_Tracker.Controller;

import com.pratice.Job_Application_Tracker.dto.ApplicationResponse;
import com.pratice.Job_Application_Tracker.dto.ApplicationStatisticsResponse;
import com.pratice.Job_Application_Tracker.dto.DashboardSummaryResponse;
import com.pratice.Job_Application_Tracker.dto.InterviewResponse;
import com.pratice.Job_Application_Tracker.dto.PageResponse;
import com.pratice.Job_Application_Tracker.service.AnalyticsService;
import com.pratice.Job_Application_Tracker.service.DashboardService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardApiController {
    private final DashboardService dashboard;
    private final AnalyticsService analytics;
    public DashboardApiController(DashboardService dashboard, AnalyticsService analytics) {
        this.dashboard = dashboard; this.analytics = analytics;
    }
    @GetMapping("/summary")
    public DashboardSummaryResponse summary(Authentication auth) { return dashboard.summary(auth.getName()); }
    @GetMapping("/recent-applications")
    public PageResponse<ApplicationResponse> recentApplications(Authentication auth) { return dashboard.recentApplications(auth.getName()); }
    @GetMapping("/upcoming-interviews")
    public PageResponse<InterviewResponse> upcomingInterviews(Authentication auth) { return dashboard.upcomingInterviews(auth.getName()); }
    @GetMapping("/upcoming-deadlines")
    public PageResponse<ApplicationResponse> upcomingDeadlines(Authentication auth) { return dashboard.upcomingDeadlines(auth.getName()); }
    @GetMapping("/application-statistics")
    public ApplicationStatisticsResponse statistics(Authentication auth) { return analytics.statistics(auth.getName()); }
}
