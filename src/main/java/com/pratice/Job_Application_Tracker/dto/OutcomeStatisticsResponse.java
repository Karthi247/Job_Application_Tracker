package com.pratice.Job_Application_Tracker.dto;

public record OutcomeStatisticsResponse(long totalApplications, long offers, double offerRatePercent,
    long rejections, double rejectionRatePercent) {}
