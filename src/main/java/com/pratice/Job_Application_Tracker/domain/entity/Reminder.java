package com.pratice.Job_Application_Tracker.domain.entity;

import com.pratice.Job_Application_Tracker.enums.ReminderType;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reminders", indexes = @Index(name = "ix_reminders_user_due", columnList = "user_id, completed_at, remind_at"))
public class Reminder {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "user_id", nullable = false) private UserAccount user;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "application_id") private JobApplication application;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "interview_id") private Interview interview;
    @Enumerated(EnumType.STRING) @Column(name = "reminder_type", nullable = false, length = 24) private ReminderType reminderType;
    @Column(nullable = false, length = 180) private String title;
    @Column(name = "remind_at", nullable = false) private LocalDateTime remindAt;
    @Column(name = "completed_at") private LocalDateTime completedAt;
    @Column(name = "last_notified_at") private LocalDateTime lastNotifiedAt;
    @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt;
    @PrePersist void onCreate() { createdAt = LocalDateTime.now(); }
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public UserAccount getUser() { return user; } public void setUser(UserAccount v) { user = v; }
    public JobApplication getApplication() { return application; } public void setApplication(JobApplication v) { application = v; }
    public Interview getInterview() { return interview; } public void setInterview(Interview v) { interview = v; }
    public ReminderType getReminderType() { return reminderType; } public void setReminderType(ReminderType v) { reminderType = v; }
    public String getTitle() { return title; } public void setTitle(String v) { title = v; }
    public LocalDateTime getRemindAt() { return remindAt; } public void setRemindAt(LocalDateTime v) { remindAt = v; }
    public LocalDateTime getCompletedAt() { return completedAt; } public void setCompletedAt(LocalDateTime v) { completedAt = v; }
    public LocalDateTime getLastNotifiedAt() { return lastNotifiedAt; } public void setLastNotifiedAt(LocalDateTime v) { lastNotifiedAt = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
