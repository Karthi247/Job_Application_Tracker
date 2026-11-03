package com.pratice.Job_Application_Tracker.service.impl;

import com.pratice.Job_Application_Tracker.Repository.InterviewRepository;
import com.pratice.Job_Application_Tracker.Repository.JobApplicationRepository;
import com.pratice.Job_Application_Tracker.Repository.UserAccountRepository;
import com.pratice.Job_Application_Tracker.domain.entity.Interview;
import com.pratice.Job_Application_Tracker.domain.entity.JobApplication;
import com.pratice.Job_Application_Tracker.dto.InterviewRequest;
import com.pratice.Job_Application_Tracker.dto.InterviewResponse;
import com.pratice.Job_Application_Tracker.dto.PageResponse;
import com.pratice.Job_Application_Tracker.exception.ResourceNotFoundException;
import com.pratice.Job_Application_Tracker.service.InterviewService;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InterviewServiceImpl implements InterviewService {
    private final InterviewRepository interviews;
    private final JobApplicationRepository applications;
    private final UserAccountRepository users;
    public InterviewServiceImpl(InterviewRepository interviews, JobApplicationRepository applications, UserAccountRepository users) {
        this.interviews = interviews; this.applications = applications; this.users = users;
    }

    @Override @Transactional
    public InterviewResponse create(String email, InterviewRequest request) {
        Interview interview = new Interview();
        apply(interview, ownedApplication(email, request.applicationId()), request);
        return InterviewResponse.from(interviews.save(interview));
    }

    @Override @Transactional(readOnly = true)
    public PageResponse<InterviewResponse> list(String email, int page, int size, LocalDate from, LocalDate to) {
        validateWindow(page, size, from, to);
        Long userId = requireUserId(email);
        Specification<Interview> specification = (root, query, builder) -> {
            ArrayList<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("application").get("user").get("id"), userId));
            if (from != null) predicates.add(builder.greaterThanOrEqualTo(root.get("interviewDate"), from));
            if (to != null) predicates.add(builder.lessThanOrEqualTo(root.get("interviewDate"), to));
            return builder.and(predicates.toArray(Predicate[]::new));
        };
        Page<Interview> result = interviews.findAll(specification,
            PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "interviewDate").and(Sort.by("interviewTime"))));
        return PageResponse.from(result, InterviewResponse::from);
    }

    @Override @Transactional(readOnly = true)
    public InterviewResponse get(String email, Long id) { return InterviewResponse.from(ownedInterview(email, id)); }

    @Override @Transactional
    public InterviewResponse update(String email, Long id, InterviewRequest request) {
        Interview interview = ownedInterview(email, id);
        apply(interview, ownedApplication(email, request.applicationId()), request);
        return InterviewResponse.from(interviews.save(interview));
    }

    @Override @Transactional
    public void delete(String email, Long id) { interviews.delete(ownedInterview(email, id)); }

    private void apply(Interview interview, JobApplication application, InterviewRequest request) {
        interview.setApplication(application);
        interview.setInterviewRound(request.interviewRound().trim());
        interview.setInterviewType(request.interviewType());
        interview.setInterviewDate(request.interviewDate());
        interview.setInterviewTime(request.interviewTime());
        interview.setMeetingLink(request.meetingLink());
        interview.setInterviewerName(request.interviewerName());
        interview.setInterviewerEmail(request.interviewerEmail());
        interview.setLocation(request.location());
        interview.setNotes(request.notes());
        if (request.status() != null) interview.setStatus(request.status());
    }

    private JobApplication ownedApplication(String email, Long applicationId) {
        return applications.findByIdAndUser_Id(applicationId, requireUserId(email))
            .orElseThrow(() -> new ResourceNotFoundException("Application"));
    }
    private Interview ownedInterview(String email, Long id) {
        return interviews.findByIdAndApplication_User_Id(id, requireUserId(email))
            .orElseThrow(() -> new ResourceNotFoundException("Interview"));
    }
    private Long requireUserId(String email) {
        return users.findByEmailIgnoreCase(email).map(user -> user.getId())
            .orElseThrow(() -> new ResourceNotFoundException("User"));
    }
    private void validateWindow(int page, int size, LocalDate from, LocalDate to) {
        if (page < 0) throw new IllegalArgumentException("Page must be zero or greater.");
        if (size < 1 || size > 100) throw new IllegalArgumentException("Page size must be between 1 and 100.");
        if (from != null && to != null && from.isAfter(to)) throw new IllegalArgumentException("Start date must be on or before end date.");
    }
}
