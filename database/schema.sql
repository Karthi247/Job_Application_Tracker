-- Fresh-install reference schema for MySQL 8.
-- Existing installations must use a reviewed migration; do not apply this over
-- the legacy schema without first preserving and mapping its data.

CREATE DATABASE IF NOT EXISTS job_application_tracker
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;

USE job_application_tracker;

CREATE TABLE IF NOT EXISTS users (
  id BIGINT NOT NULL AUTO_INCREMENT,
  full_name VARCHAR(160) NOT NULL,
  email VARCHAR(254) NOT NULL,
  mobile VARCHAR(32) NOT NULL,
  password_hash VARCHAR(100) NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  CONSTRAINT uq_users_email UNIQUE (email),
  CONSTRAINT uq_users_mobile UNIQUE (mobile)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS job_applications (
  id BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  company_name VARCHAR(180) NOT NULL,
  job_title VARCHAR(180) NOT NULL,
  job_description TEXT NULL,
  location VARCHAR(180) NULL,
  employment_type VARCHAR(32) NULL,
  work_mode VARCHAR(32) NULL,
  salary VARCHAR(120) NULL,
  job_url VARCHAR(2048) NULL,
  recruiter_name VARCHAR(160) NULL,
  recruiter_email VARCHAR(254) NULL,
  recruiter_phone VARCHAR(32) NULL,
  resume_url VARCHAR(2048) NULL,
  cover_letter TEXT NULL,
  status VARCHAR(32) NOT NULL DEFAULT 'SAVED',
  priority VARCHAR(16) NOT NULL DEFAULT 'MEDIUM',
  applied_date DATE NULL,
  deadline DATE NULL,
  notes TEXT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY ix_applications_user_created (user_id, created_at),
  KEY ix_applications_user_status (user_id, status),
  KEY ix_applications_user_priority (user_id, priority),
  KEY ix_applications_user_deadline (user_id, deadline),
  CONSTRAINT fk_applications_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
  CONSTRAINT ck_applications_status CHECK (status IN ('SAVED','APPLIED','UNDER_REVIEW','SHORTLISTED','ASSESSMENT','INTERVIEW','OFFER','REJECTED','WITHDRAWN')),
  CONSTRAINT ck_applications_priority CHECK (priority IN ('LOW','MEDIUM','HIGH'))
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS interviews (
  id BIGINT NOT NULL AUTO_INCREMENT,
  application_id BIGINT NOT NULL,
  interview_round VARCHAR(120) NOT NULL,
  interview_type VARCHAR(24) NOT NULL,
  interview_date DATE NOT NULL,
  interview_time TIME NULL,
  meeting_link VARCHAR(2048) NULL,
  interviewer_name VARCHAR(160) NULL,
  interviewer_email VARCHAR(254) NULL,
  location VARCHAR(255) NULL,
  notes TEXT NULL,
  status VARCHAR(24) NOT NULL DEFAULT 'SCHEDULED',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY ix_interviews_application_date (application_id, interview_date),
  CONSTRAINT fk_interviews_application FOREIGN KEY (application_id) REFERENCES job_applications (id) ON DELETE CASCADE,
  CONSTRAINT ck_interviews_type CHECK (interview_type IN ('PHONE','VIDEO','TECHNICAL','HR','ONSITE')),
  CONSTRAINT ck_interviews_status CHECK (status IN ('SCHEDULED','COMPLETED','CANCELLED','RESCHEDULED'))
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS reminders (
  id BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  application_id BIGINT NULL,
  interview_id BIGINT NULL,
  reminder_type VARCHAR(24) NOT NULL,
  title VARCHAR(180) NOT NULL,
  remind_at DATETIME NOT NULL,
  completed_at DATETIME NULL,
  last_notified_at DATETIME NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY ix_reminders_user_due (user_id, completed_at, remind_at),
  KEY ix_reminders_application (application_id),
  KEY ix_reminders_interview (interview_id),
  CONSTRAINT fk_reminders_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
  CONSTRAINT fk_reminders_application FOREIGN KEY (application_id) REFERENCES job_applications (id) ON DELETE CASCADE,
  CONSTRAINT fk_reminders_interview FOREIGN KEY (interview_id) REFERENCES interviews (id) ON DELETE CASCADE,
  CONSTRAINT ck_reminders_type CHECK (reminder_type IN ('INTERVIEW','DEADLINE','FOLLOW_UP','CUSTOM'))
) ENGINE=InnoDB;
