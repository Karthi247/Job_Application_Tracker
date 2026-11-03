package com.pratice.Job_Application_Tracker.service.impl;

import com.pratice.Job_Application_Tracker.Repository.InterviewRepository;
import com.pratice.Job_Application_Tracker.Repository.JobApplicationRepository;
import com.pratice.Job_Application_Tracker.Repository.UserAccountRepository;
import com.pratice.Job_Application_Tracker.dto.ApplicationStatisticsResponse;
import com.pratice.Job_Application_Tracker.dto.CompanyApplicationCountResponse;
import com.pratice.Job_Application_Tracker.dto.InterviewConversionResponse;
import com.pratice.Job_Application_Tracker.dto.MonthlyApplicationCountResponse;
import com.pratice.Job_Application_Tracker.dto.OutcomeStatisticsResponse;
import com.pratice.Job_Application_Tracker.dto.StatusCountResponse;
import com.pratice.Job_Application_Tracker.enums.ApplicationStatus;
import com.pratice.Job_Application_Tracker.exception.ResourceNotFoundException;
import com.pratice.Job_Application_Tracker.service.AnalyticsService;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {
    private final JobApplicationRepository applications;
    private final InterviewRepository interviews;
    private final UserAccountRepository users;
    public AnalyticsServiceImpl(JobApplicationRepository applications, InterviewRepository interviews, UserAccountRepository users) {
        this.applications = applications; this.interviews = interviews; this.users = users;
    }

    @Override @Transactional(readOnly = true)
    public ApplicationStatisticsResponse statistics(String email) {
        Long userId = userId(email);
        long total = applications.countByUser_Id(userId);
        long offers = applications.countByUser_IdAndStatus(userId, ApplicationStatus.OFFER);
        long rejections = applications.countByUser_IdAndStatus(userId, ApplicationStatus.REJECTED);
        long interviewCount = interviews.countDistinctApplicationsWithInterviews(userId);
        return new ApplicationStatisticsResponse(total, interviewCount,
            DashboardServiceImpl.rate(interviewCount, total), offers, DashboardServiceImpl.rate(offers, total),
            rejections, DashboardServiceImpl.rate(rejections, total), statusesFor(userId), monthsFor(userId));
    }

    @Override @Transactional(readOnly = true)
    public List<StatusCountResponse> statusDistribution(String email) { return statusesFor(userId(email)); }

    @Override @Transactional(readOnly = true)
    public List<MonthlyApplicationCountResponse> monthlyTrends(String email) { return monthsFor(userId(email)); }

    @Override @Transactional(readOnly = true)
    public InterviewConversionResponse interviewConversion(String email) {
        Long userId = userId(email);
        long total = applications.countByUser_Id(userId);
        long converted = interviews.countDistinctApplicationsWithInterviews(userId);
        return new InterviewConversionResponse(total, converted, DashboardServiceImpl.rate(converted, total));
    }

    @Override @Transactional(readOnly = true)
    public List<CompanyApplicationCountResponse> companyCounts(String email) {
        return applications.countByCompanyForUser(userId(email), PageRequest.of(0, 10)).getContent().stream()
            .map(count -> new CompanyApplicationCountResponse(count.getCompanyName(), count.getCount())).toList();
    }

    @Override @Transactional(readOnly = true)
    public OutcomeStatisticsResponse outcomes(String email) {
        Long userId = userId(email);
        long total = applications.countByUser_Id(userId);
        long offers = applications.countByUser_IdAndStatus(userId, ApplicationStatus.OFFER);
        long rejections = applications.countByUser_IdAndStatus(userId, ApplicationStatus.REJECTED);
        return new OutcomeStatisticsResponse(total, offers, DashboardServiceImpl.rate(offers, total),
            rejections, DashboardServiceImpl.rate(rejections, total));
    }

    private List<StatusCountResponse> statusesFor(Long userId) {
        Map<ApplicationStatus, Long> counts = new EnumMap<>(ApplicationStatus.class);
        applications.countByStatusForUser(userId).forEach(row -> counts.put(row.getStatus(), row.getCount()));
        return java.util.Arrays.stream(ApplicationStatus.values())
            .map(status -> new StatusCountResponse(status.name(), counts.getOrDefault(status, 0L))).toList();
    }

    private List<MonthlyApplicationCountResponse> monthsFor(Long userId) {
        YearMonth current = YearMonth.now();
        YearMonth first = current.minusMonths(11);
        Map<YearMonth, Long> counts = new java.util.HashMap<>();
        applications.countByMonthForUser(userId, first.atDay(1), current.atEndOfMonth()).forEach(row ->
            counts.put(YearMonth.of(row.getYear(), row.getMonth()), row.getCount()));
        List<MonthlyApplicationCountResponse> result = new ArrayList<>(12);
        for (int i = 0; i < 12; i++) {
            YearMonth month = first.plusMonths(i);
            result.add(new MonthlyApplicationCountResponse(month.getYear(), month.getMonthValue(), counts.getOrDefault(month, 0L)));
        }
        return List.copyOf(result);
    }

    private Long userId(String email) {
        return users.findByEmailIgnoreCase(email).map(user -> user.getId())
            .orElseThrow(() -> new ResourceNotFoundException("User"));
    }
}
