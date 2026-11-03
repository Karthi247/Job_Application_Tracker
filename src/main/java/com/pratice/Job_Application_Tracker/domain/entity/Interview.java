package com.pratice.Job_Application_Tracker.domain.entity;

import com.pratice.Job_Application_Tracker.enums.InterviewStatus;
import com.pratice.Job_Application_Tracker.enums.InterviewType;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "interviews", indexes = @Index(name = "ix_interviews_application_date", columnList = "application_id, interview_date"))
public class Interview {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "application_id", nullable = false) private JobApplication application;
    @Column(name = "interview_round", nullable = false, length = 120) private String interviewRound;
    @Enumerated(EnumType.STRING) @Column(name = "interview_type", nullable = false, length = 24) private InterviewType interviewType;
    @Column(name = "interview_date", nullable = false) private LocalDate interviewDate;
    @Column(name = "interview_time") private LocalTime interviewTime;
    @Column(name = "meeting_link", length = 2048) private String meetingLink;
    @Column(name = "interviewer_name", length = 160) private String interviewerName;
    @Column(name = "interviewer_email", length = 254) private String interviewerEmail;
    @Column(length = 255) private String location;
    @Column(columnDefinition = "TEXT") private String notes;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 24) private InterviewStatus status = InterviewStatus.SCHEDULED;
    @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt;
    @PrePersist void onCreate() { createdAt = LocalDateTime.now(); }
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public JobApplication getApplication() { return application; } public void setApplication(JobApplication v) { application = v; }
    public String getInterviewRound() { return interviewRound; } public void setInterviewRound(String v) { interviewRound = v; }
    public InterviewType getInterviewType() { return interviewType; } public void setInterviewType(InterviewType v) { interviewType = v; }
    public LocalDate getInterviewDate() { return interviewDate; } public void setInterviewDate(LocalDate v) { interviewDate = v; }
    public LocalTime getInterviewTime() { return interviewTime; } public void setInterviewTime(LocalTime v) { interviewTime = v; }
    public String getMeetingLink() { return meetingLink; } public void setMeetingLink(String v) { meetingLink = v; }
    public String getInterviewerName() { return interviewerName; } public void setInterviewerName(String v) { interviewerName = v; }
    public String getInterviewerEmail() { return interviewerEmail; } public void setInterviewerEmail(String v) { interviewerEmail = v; }
    public String getLocation() { return location; } public void setLocation(String v) { location = v; }
    public String getNotes() { return notes; } public void setNotes(String v) { notes = v; }
    public InterviewStatus getStatus() { return status; } public void setStatus(InterviewStatus v) { status = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
