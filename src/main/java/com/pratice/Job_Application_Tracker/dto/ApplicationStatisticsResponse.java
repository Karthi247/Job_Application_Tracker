package com.pratice.Job_Application_Tracker.dto;

import java.util.List;

public record ApplicationStatisticsResponse(long totalApplications, long applicationsWithInterviews,
    double interviewConversionRatePercent, long offers, double offerRatePercent,
    long rejections, double rejectionRatePercent,
    List<StatusCountResponse> statusDistribution, List<MonthlyApplicationCountResponse> monthlyApplications) {}
