package com.pratice.Job_Application_Tracker.service.impl;

import com.pratice.Job_Application_Tracker.Repository.InterviewRepository;
import com.pratice.Job_Application_Tracker.Repository.JobApplicationRepository;
import com.pratice.Job_Application_Tracker.Repository.ReminderRepository;
import com.pratice.Job_Application_Tracker.Repository.UserAccountRepository;
import com.pratice.Job_Application_Tracker.domain.entity.Interview;
import com.pratice.Job_Application_Tracker.domain.entity.JobApplication;
import com.pratice.Job_Application_Tracker.domain.entity.Reminder;
import com.pratice.Job_Application_Tracker.domain.entity.UserAccount;
import com.pratice.Job_Application_Tracker.dto.PageResponse;
import com.pratice.Job_Application_Tracker.dto.ReminderRequest;
import com.pratice.Job_Application_Tracker.dto.ReminderResponse;
import com.pratice.Job_Application_Tracker.exception.ResourceNotFoundException;
import com.pratice.Job_Application_Tracker.service.ReminderService;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReminderServiceImpl implements ReminderService {
    private final ReminderRepository reminders;
    private final UserAccountRepository users;
    private final JobApplicationRepository applications;
    private final InterviewRepository interviews;
    public ReminderServiceImpl(ReminderRepository reminders, UserAccountRepository users,
        JobApplicationRepository applications, InterviewRepository interviews) {
        this.reminders = reminders; this.users = users; this.applications = applications; this.interviews = interviews;
    }

    @Override @Transactional
    public ReminderResponse create(String email, ReminderRequest request) {
        UserAccount user = requireUser(email);
        Reminder reminder = new Reminder();
        reminder.setUser(user);
        apply(reminder, user, request);
        return ReminderResponse.from(reminders.save(reminder));
    }

    @Override @Transactional(readOnly = true)
    public PageResponse<ReminderResponse> list(String email, int page, int size) {
        validatePage(page, size);
        Page<Reminder> result = reminders.findByUser_Id(requireUser(email).getId(),
            PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "remindAt")));
        return PageResponse.from(result, ReminderResponse::from);
    }

    @Override @Transactional(readOnly = true)
    public ReminderResponse get(String email, Long id) { return ReminderResponse.from(owned(email, id)); }

    @Override @Transactional
    public ReminderResponse update(String email, Long id, ReminderRequest request) {
        Reminder reminder = owned(email, id);
        apply(reminder, reminder.getUser(), request);
        return ReminderResponse.from(reminders.save(reminder));
    }

    @Override @Transactional
    public ReminderResponse complete(String email, Long id) {
        Reminder reminder = owned(email, id);
        if (reminder.getCompletedAt() == null) reminder.setCompletedAt(LocalDateTime.now());
        return ReminderResponse.from(reminders.save(reminder));
    }

    @Override @Transactional
    public void delete(String email, Long id) { reminders.delete(owned(email, id)); }

    private void apply(Reminder reminder, UserAccount user, ReminderRequest request) {
        if (reminder.getRemindAt() == null || !reminder.getRemindAt().equals(request.remindAt()))
            reminder.setLastNotifiedAt(null);
        JobApplication application = request.applicationId() == null ? null : applications
            .findByIdAndUser_Id(request.applicationId(), user.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Application"));
        Interview interview = request.interviewId() == null ? null : interviews
            .findByIdAndApplication_User_Id(request.interviewId(), user.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Interview"));
        if (interview != null && application != null && !interview.getApplication().getId().equals(application.getId()))
            throw new IllegalArgumentException("The interview does not belong to the selected application.");
        if (application == null && interview != null) application = interview.getApplication();
        reminder.setApplication(application);
        reminder.setInterview(interview);
        reminder.setReminderType(request.reminderType());
        reminder.setTitle(request.title().trim());
        reminder.setRemindAt(request.remindAt());
    }

    private Reminder owned(String email, Long id) {
        return reminders.findByIdAndUser_Id(id, requireUser(email).getId())
            .orElseThrow(() -> new ResourceNotFoundException("Reminder"));
    }
    private UserAccount requireUser(String email) {
        return users.findByEmailIgnoreCase(email).orElseThrow(() -> new ResourceNotFoundException("User"));
    }
    private void validatePage(int page, int size) {
        if (page < 0) throw new IllegalArgumentException("Page must be zero or greater.");
        if (size < 1 || size > 100) throw new IllegalArgumentException("Page size must be between 1 and 100.");
    }
}
