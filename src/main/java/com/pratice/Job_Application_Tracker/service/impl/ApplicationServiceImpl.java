package com.pratice.Job_Application_Tracker.service.impl;

import com.pratice.Job_Application_Tracker.Repository.JobApplicationRepository;
import com.pratice.Job_Application_Tracker.Repository.UserAccountRepository;
import com.pratice.Job_Application_Tracker.domain.entity.JobApplication;
import com.pratice.Job_Application_Tracker.domain.entity.UserAccount;
import com.pratice.Job_Application_Tracker.dto.ApplicationCreateRequest;
import com.pratice.Job_Application_Tracker.dto.ApplicationResponse;
import com.pratice.Job_Application_Tracker.dto.PageResponse;
import com.pratice.Job_Application_Tracker.enums.ApplicationPriority;
import com.pratice.Job_Application_Tracker.enums.ApplicationStatus;
import com.pratice.Job_Application_Tracker.exception.ResourceNotFoundException;
import com.pratice.Job_Application_Tracker.service.ApplicationService;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ApplicationServiceImpl implements ApplicationService {
    private static final Set<String> SORT_FIELDS = Set.of("createdAt", "updatedAt", "appliedDate", "companyName",
        "jobTitle", "status", "priority", "deadline");
    private final JobApplicationRepository applications;
    private final UserAccountRepository users;

    public ApplicationServiceImpl(JobApplicationRepository applications, UserAccountRepository users) {
        this.applications = applications;
        this.users = users;
    }

    @Override
    @Transactional
    public ApplicationResponse create(String email, ApplicationCreateRequest request) {
        JobApplication application = new JobApplication();
        application.setUser(requireUser(email));
        apply(application, request);
        return ApplicationResponse.from(applications.save(application));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ApplicationResponse> list(String email, int page, int size, String sortBy, String direction,
        String search, ApplicationStatus status, ApplicationPriority priority, LocalDate appliedFrom, LocalDate appliedTo) {
        if (page < 0) throw new IllegalArgumentException("Page must be zero or greater.");
        if (size < 1 || size > 100) throw new IllegalArgumentException("Page size must be between 1 and 100.");
        if (appliedFrom != null && appliedTo != null && appliedFrom.isAfter(appliedTo))
            throw new IllegalArgumentException("Applied date start must be on or before the end date.");
        String sortField = sortBy == null || sortBy.isBlank() ? "createdAt" : sortBy;
        if (!SORT_FIELDS.contains(sortField)) throw new IllegalArgumentException("Unsupported sort field.");
        Sort.Direction sortDirection = "asc".equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Long userId = requireUser(email).getId();
        Specification<JobApplication> specification = (root, query, builder) -> {
            ArrayList<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("user").get("id"), userId));
            if (status != null) predicates.add(builder.equal(root.get("status"), status));
            if (priority != null) predicates.add(builder.equal(root.get("priority"), priority));
            if (appliedFrom != null) predicates.add(builder.greaterThanOrEqualTo(root.get("appliedDate"), appliedFrom));
            if (appliedTo != null) predicates.add(builder.lessThanOrEqualTo(root.get("appliedDate"), appliedTo));
            if (search != null && !search.isBlank()) {
                String pattern = "%" + search.trim().toLowerCase(Locale.ROOT) + "%";
                predicates.add(builder.or(builder.like(builder.lower(root.get("companyName")), pattern),
                    builder.like(builder.lower(root.get("jobTitle")), pattern)));
            }
            return builder.and(predicates.toArray(Predicate[]::new));
        };
        Page<JobApplication> results = applications.findAll(specification,
            PageRequest.of(page, size, Sort.by(sortDirection, sortField)));
        return PageResponse.from(results, ApplicationResponse::from);
    }

    @Override
    @Transactional(readOnly = true)
    public ApplicationResponse get(String email, Long id) { return ApplicationResponse.from(owned(email, id)); }

    @Override
    @Transactional
    public ApplicationResponse update(String email, Long id, ApplicationCreateRequest request) {
        JobApplication application = owned(email, id);
        apply(application, request);
        return ApplicationResponse.from(applications.save(application));
    }

    @Override
    @Transactional
    public ApplicationResponse updateStatus(String email, Long id, ApplicationStatus status) {
        JobApplication application = owned(email, id);
        application.setStatus(status);
        return ApplicationResponse.from(applications.save(application));
    }

    @Override
    @Transactional
    public void delete(String email, Long id) { applications.delete(owned(email, id)); }

    private UserAccount requireUser(String email) {
        return users.findByEmailIgnoreCase(email).orElseThrow(() -> new ResourceNotFoundException("User"));
    }

    private JobApplication owned(String email, Long id) {
        Long userId = requireUser(email).getId();
        return applications.findByIdAndUser_Id(id, userId).orElseThrow(() -> new ResourceNotFoundException("Application"));
    }

    private void apply(JobApplication application, ApplicationCreateRequest request) {
        application.setCompanyName(request.companyName().trim());
        application.setJobTitle(request.jobTitle().trim());
        application.setJobDescription(request.jobDescription());
        application.setLocation(request.location());
        application.setEmploymentType(request.employmentType());
        application.setWorkMode(request.workMode());
        application.setSalary(request.salary());
        application.setJobUrl(request.jobUrl());
        application.setRecruiterName(request.recruiterName());
        application.setRecruiterEmail(request.recruiterEmail());
        application.setRecruiterPhone(request.recruiterPhone());
        application.setResumeUrl(request.resumeUrl());
        application.setCoverLetter(request.coverLetter());
        if (request.status() != null) application.setStatus(request.status());
        if (request.priority() != null) application.setPriority(request.priority());
        application.setAppliedDate(request.appliedDate());
        application.setDeadline(request.deadline());
        application.setNotes(request.notes());
    }
}
