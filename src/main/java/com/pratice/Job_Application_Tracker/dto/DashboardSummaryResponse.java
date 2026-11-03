package com.pratice.Job_Application_Tracker.dto;

public record DashboardSummaryResponse(long totalApplications, long appliedApplications,
    long shortlistedApplications, long applicationsWithInterviews, long offers, long rejections,
    double successRatePercent) {}
