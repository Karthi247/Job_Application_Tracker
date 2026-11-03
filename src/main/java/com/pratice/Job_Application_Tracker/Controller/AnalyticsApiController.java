package com.pratice.Job_Application_Tracker.Controller;

import com.pratice.Job_Application_Tracker.dto.ApplicationStatisticsResponse;
import com.pratice.Job_Application_Tracker.dto.CompanyApplicationCountResponse;
import com.pratice.Job_Application_Tracker.dto.InterviewConversionResponse;
import com.pratice.Job_Application_Tracker.dto.MonthlyApplicationCountResponse;
import com.pratice.Job_Application_Tracker.dto.OutcomeStatisticsResponse;
import com.pratice.Job_Application_Tracker.dto.StatusCountResponse;
import com.pratice.Job_Application_Tracker.service.AnalyticsService;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsApiController {
    private final AnalyticsService analytics;
    public AnalyticsApiController(AnalyticsService analytics) { this.analytics = analytics; }

    @GetMapping("/application-statistics")
    public ApplicationStatisticsResponse statistics(Authentication auth) { return analytics.statistics(auth.getName()); }
    @GetMapping("/status-distribution")
    public List<StatusCountResponse> statusDistribution(Authentication auth) { return analytics.statusDistribution(auth.getName()); }
    @GetMapping("/monthly-trends")
    public List<MonthlyApplicationCountResponse> monthlyTrends(Authentication auth) { return analytics.monthlyTrends(auth.getName()); }
    @GetMapping("/interview-conversion")
    public InterviewConversionResponse interviewConversion(Authentication auth) { return analytics.interviewConversion(auth.getName()); }
    @GetMapping("/companies")
    public List<CompanyApplicationCountResponse> companyCounts(Authentication auth) { return analytics.companyCounts(auth.getName()); }
    @GetMapping("/outcomes")
    public OutcomeStatisticsResponse outcomes(Authentication auth) { return analytics.outcomes(auth.getName()); }
}
