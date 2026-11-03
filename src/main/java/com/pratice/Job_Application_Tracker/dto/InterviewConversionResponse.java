package com.pratice.Job_Application_Tracker.dto;

public record InterviewConversionResponse(long totalApplications, long applicationsWithInterviews,
    double conversionRatePercent) {}
