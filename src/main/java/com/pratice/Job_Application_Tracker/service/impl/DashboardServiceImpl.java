package com.pratice.Job_Application_Tracker.service.impl;

import com.pratice.Job_Application_Tracker.Repository.InterviewRepository;
import com.pratice.Job_Application_Tracker.Repository.JobApplicationRepository;
import com.pratice.Job_Application_Tracker.Repository.UserAccountRepository;
import com.pratice.Job_Application_Tracker.domain.entity.UserAccount;
import com.pratice.Job_Application_Tracker.dto.ApplicationResponse;
import com.pratice.Job_Application_Tracker.dto.DashboardSummaryResponse;
import com.pratice.Job_Application_Tracker.dto.InterviewResponse;
import com.pratice.Job_Application_Tracker.dto.PageResponse;
import com.pratice.Job_Application_Tracker.enums.ApplicationStatus;
import com.pratice.Job_Application_Tracker.exception.ResourceNotFoundException;
import com.pratice.Job_Application_Tracker.service.DashboardService;
import java.time.LocalDate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardServiceImpl implements DashboardService {
    private final JobApplicationRepository applications;
    private final InterviewRepository interviews;
    private final UserAccountRepository users;
    public DashboardServiceImpl(JobApplicationRepository applications, InterviewRepository interviews, UserAccountRepository users) {
        this.applications = applications; this.interviews = interviews; this.users = users;
    }

    @Override @Transactional(readOnly = true)
    public DashboardSummaryResponse summary(String email) {
        Long userId = user(email).getId();
        long total = applications.countByUser_Id(userId);
        long offers = applications.countByUser_IdAndStatus(userId, ApplicationStatus.OFFER);
        return new DashboardSummaryResponse(total,
            applications.countByUser_IdAndStatus(userId, ApplicationStatus.APPLIED),
            applications.countByUser_IdAndStatus(userId, ApplicationStatus.SHORTLISTED),
            interviews.countDistinctApplicationsWithInterviews(userId), offers,
            applications.countByUser_IdAndStatus(userId, ApplicationStatus.REJECTED), rate(offers, total));
    }

    @Override @Transactional(readOnly = true)
    public PageResponse<ApplicationResponse> recentApplications(String email) {
        var page = applications.findByUser_IdOrderByCreatedAtDesc(user(email).getId(), PageRequest.of(0, 5));
        return PageResponse.from(page, ApplicationResponse::from);
    }

    @Override @Transactional(readOnly = true)
    public PageResponse<InterviewResponse> upcomingInterviews(String email) {
        var page = interviews.findByApplication_User_IdAndInterviewDateGreaterThanEqualOrderByInterviewDateAscInterviewTimeAsc(
            user(email).getId(), LocalDate.now(), PageRequest.of(0, 5));
        return PageResponse.from(page, InterviewResponse::from);
    }

    @Override @Transactional(readOnly = true)
    public PageResponse<ApplicationResponse> upcomingDeadlines(String email) {
        var page = applications.findByUser_IdAndDeadlineGreaterThanEqualOrderByDeadlineAsc(user(email).getId(),
            LocalDate.now(), PageRequest.of(0, 5, Sort.by("deadline")));
        return PageResponse.from(page, ApplicationResponse::from);
    }

    private UserAccount user(String email) {
        return users.findByEmailIgnoreCase(email).orElseThrow(() -> new ResourceNotFoundException("User"));
    }
    static double rate(long numerator, long denominator) {
        return denominator == 0 ? 0.0 : Math.round((double) numerator * 10000.0 / denominator) / 100.0;
    }
}
