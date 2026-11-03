package com.pratice.Job_Application_Tracker.service;

import com.pratice.Job_Application_Tracker.dto.ApplicationStatisticsResponse;
import com.pratice.Job_Application_Tracker.dto.CompanyApplicationCountResponse;
import com.pratice.Job_Application_Tracker.dto.InterviewConversionResponse;
import com.pratice.Job_Application_Tracker.dto.MonthlyApplicationCountResponse;
import com.pratice.Job_Application_Tracker.dto.OutcomeStatisticsResponse;
import com.pratice.Job_Application_Tracker.dto.StatusCountResponse;
import java.util.List;

public interface AnalyticsService {
    ApplicationStatisticsResponse statistics(String email);
    List<StatusCountResponse> statusDistribution(String email);
    List<MonthlyApplicationCountResponse> monthlyTrends(String email);
    InterviewConversionResponse interviewConversion(String email);
    List<CompanyApplicationCountResponse> companyCounts(String email);
    OutcomeStatisticsResponse outcomes(String email);
}
