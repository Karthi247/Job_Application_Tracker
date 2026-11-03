package com.pratice.Job_Application_Tracker.domain.entity;

import com.pratice.Job_Application_Tracker.enums.ApplicationPriority;
import com.pratice.Job_Application_Tracker.enums.ApplicationStatus;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_applications", indexes = {
    @Index(name = "ix_applications_user_created", columnList = "user_id, created_at"),
    @Index(name = "ix_applications_user_status", columnList = "user_id, status"),
    @Index(name = "ix_applications_user_deadline", columnList = "user_id, deadline")
})
public class JobApplication {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount user;
    @Column(name = "company_name", nullable = false, length = 180) private String companyName;
    @Column(name = "job_title", nullable = false, length = 180) private String jobTitle;
    @Column(name = "job_description", columnDefinition = "TEXT") private String jobDescription;
    @Column(length = 180) private String location;
    @Column(name = "employment_type", length = 32) private String employmentType;
    @Column(name = "work_mode", length = 32) private String workMode;
    @Column(length = 120) private String salary;
    @Column(name = "job_url", length = 2048) private String jobUrl;
    @Column(name = "recruiter_name", length = 160) private String recruiterName;
    @Column(name = "recruiter_email", length = 254) private String recruiterEmail;
    @Column(name = "recruiter_phone", length = 32) private String recruiterPhone;
    @Column(name = "resume_url", length = 2048) private String resumeUrl;
    @Column(name = "cover_letter", columnDefinition = "TEXT") private String coverLetter;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 32) private ApplicationStatus status = ApplicationStatus.SAVED;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 16) private ApplicationPriority priority = ApplicationPriority.MEDIUM;
    @Column(name = "applied_date") private LocalDate appliedDate;
    private LocalDate deadline;
    @Column(columnDefinition = "TEXT") private String notes;
    @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false) private LocalDateTime updatedAt;

    @PrePersist void onCreate() { LocalDateTime now = LocalDateTime.now(); createdAt = now; updatedAt = now; }
    @PreUpdate void onUpdate() { updatedAt = LocalDateTime.now(); }
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public UserAccount getUser() { return user; } public void setUser(UserAccount user) { this.user = user; }
    public String getCompanyName() { return companyName; } public void setCompanyName(String v) { companyName = v; }
    public String getJobTitle() { return jobTitle; } public void setJobTitle(String v) { jobTitle = v; }
    public String getJobDescription() { return jobDescription; } public void setJobDescription(String v) { jobDescription = v; }
    public String getLocation() { return location; } public void setLocation(String v) { location = v; }
    public String getEmploymentType() { return employmentType; } public void setEmploymentType(String v) { employmentType = v; }
    public String getWorkMode() { return workMode; } public void setWorkMode(String v) { workMode = v; }
    public String getSalary() { return salary; } public void setSalary(String v) { salary = v; }
    public String getJobUrl() { return jobUrl; } public void setJobUrl(String v) { jobUrl = v; }
    public String getRecruiterName() { return recruiterName; } public void setRecruiterName(String v) { recruiterName = v; }
    public String getRecruiterEmail() { return recruiterEmail; } public void setRecruiterEmail(String v) { recruiterEmail = v; }
    public String getRecruiterPhone() { return recruiterPhone; } public void setRecruiterPhone(String v) { recruiterPhone = v; }
    public String getResumeUrl() { return resumeUrl; } public void setResumeUrl(String v) { resumeUrl = v; }
    public String getCoverLetter() { return coverLetter; } public void setCoverLetter(String v) { coverLetter = v; }
    public ApplicationStatus getStatus() { return status; } public void setStatus(ApplicationStatus v) { status = v; }
    public ApplicationPriority getPriority() { return priority; } public void setPriority(ApplicationPriority v) { priority = v; }
    public LocalDate getAppliedDate() { return appliedDate; } public void setAppliedDate(LocalDate v) { appliedDate = v; }
    public LocalDate getDeadline() { return deadline; } public void setDeadline(LocalDate v) { deadline = v; }
    public String getNotes() { return notes; } public void setNotes(String v) { notes = v; }
    public LocalDateTime getCreatedAt() { return createdAt; } public LocalDateTime getUpdatedAt() { return updatedAt; }
}
