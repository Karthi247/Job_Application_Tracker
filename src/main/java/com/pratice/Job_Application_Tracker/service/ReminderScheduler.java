package com.pratice.Job_Application_Tracker.service;

import com.pratice.Job_Application_Tracker.Repository.ReminderRepository;
import com.pratice.Job_Application_Tracker.domain.entity.Reminder;
import java.time.LocalDateTime;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ReminderScheduler {
    private final ReminderRepository reminders;
    private final ApplicationEventPublisher events;
    public ReminderScheduler(ReminderRepository reminders, ApplicationEventPublisher events) {
        this.reminders = reminders; this.events = events;
    }

    @Scheduled(fixedDelayString = "${app.reminders.scan-interval-ms:60000}")
    @Transactional
    public void publishDueReminderEvents() {
        LocalDateTime now = LocalDateTime.now();
        var dueReminders = reminders.findTop100ByCompletedAtIsNullAndRemindAtBeforeAndLastNotifiedAtIsNullOrderByRemindAtAsc(now);
        for (Reminder reminder : dueReminders) {
            reminder.setLastNotifiedAt(now);
            events.publishEvent(new ReminderDueEvent(reminder.getId(), reminder.getUser().getId(),
                reminder.getReminderType(), reminder.getRemindAt()));
        }
        reminders.saveAll(dueReminders);
    }
}
