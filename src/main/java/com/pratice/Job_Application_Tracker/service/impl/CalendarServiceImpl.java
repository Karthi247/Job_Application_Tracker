package com.pratice.Job_Application_Tracker.service.impl;

import com.pratice.Job_Application_Tracker.Repository.InterviewRepository;
import com.pratice.Job_Application_Tracker.Repository.JobApplicationRepository;
import com.pratice.Job_Application_Tracker.Repository.ReminderRepository;
import com.pratice.Job_Application_Tracker.Repository.UserAccountRepository;
import com.pratice.Job_Application_Tracker.domain.entity.Interview;
import com.pratice.Job_Application_Tracker.domain.entity.JobApplication;
import com.pratice.Job_Application_Tracker.domain.entity.Reminder;
import com.pratice.Job_Application_Tracker.dto.CalendarEventResponse;
import com.pratice.Job_Application_Tracker.exception.ResourceNotFoundException;
import com.pratice.Job_Application_Tracker.service.CalendarService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CalendarServiceImpl implements CalendarService {
    private final InterviewRepository interviews;
    private final JobApplicationRepository applications;
    private final ReminderRepository reminders;
    private final UserAccountRepository users;
    public CalendarServiceImpl(InterviewRepository interviews, JobApplicationRepository applications,
        ReminderRepository reminders, UserAccountRepository users) {
        this.interviews = interviews; this.applications = applications; this.reminders = reminders; this.users = users;
    }

    @Override @Transactional(readOnly = true)
    public List<CalendarEventResponse> events(String email, LocalDate from, LocalDate to) {
        if (from == null || to == null) throw new IllegalArgumentException("Both from and to dates are required.");
        if (from.isAfter(to)) throw new IllegalArgumentException("Start date must be on or before end date.");
        if (from.plusDays(366).isBefore(to)) throw new IllegalArgumentException("Calendar range cannot exceed one year.");
        Long userId = users.findByEmailIgnoreCase(email).map(user -> user.getId())
            .orElseThrow(() -> new ResourceNotFoundException("User"));
        ArrayList<CalendarEventResponse> events = new ArrayList<>();
        for (Interview interview : interviews.findByApplication_User_IdAndInterviewDateBetweenOrderByInterviewDateAscInterviewTimeAsc(userId, from, to)) {
            JobApplication application = interview.getApplication();
            LocalDateTime start = LocalDateTime.of(interview.getInterviewDate(),
                interview.getInterviewTime() == null ? LocalTime.MIDNIGHT : interview.getInterviewTime());
            events.add(new CalendarEventResponse("interview-" + interview.getId(), "INTERVIEW",
                interview.getInterviewRound(), start, application.getId(), interview.getId(), null,
                application.getCompanyName(), application.getJobTitle()));
        }
        for (JobApplication application : applications.findByUser_IdAndDeadlineBetween(userId, from, to)) {
            events.add(new CalendarEventResponse("deadline-" + application.getId(), "DEADLINE",
                "Application deadline", application.getDeadline().atStartOfDay(), application.getId(), null, null,
                application.getCompanyName(), application.getJobTitle()));
        }
        LocalDateTime startAt = from.atStartOfDay();
        LocalDateTime endAt = to.plusDays(1).atStartOfDay().minusNanos(1);
        for (Reminder reminder : reminders.findByUser_IdAndCompletedAtIsNullAndRemindAtBetweenOrderByRemindAtAsc(userId, startAt, endAt)) {
            JobApplication application = reminder.getApplication();
            events.add(new CalendarEventResponse("reminder-" + reminder.getId(), reminder.getReminderType().name(),
                reminder.getTitle(), reminder.getRemindAt(), application == null ? null : application.getId(),
                reminder.getInterview() == null ? null : reminder.getInterview().getId(), reminder.getId(),
                application == null ? null : application.getCompanyName(), application == null ? null : application.getJobTitle()));
        }
        events.sort(Comparator.comparing(CalendarEventResponse::start));
        return List.copyOf(events);
    }
}
