package com.pratice.Job_Application_Tracker.service;

import com.pratice.Job_Application_Tracker.dto.InterviewRequest;
import com.pratice.Job_Application_Tracker.dto.InterviewResponse;
import com.pratice.Job_Application_Tracker.dto.PageResponse;
import java.time.LocalDate;

public interface InterviewService {
    InterviewResponse create(String email, InterviewRequest request);
    PageResponse<InterviewResponse> list(String email, int page, int size, LocalDate from, LocalDate to);
    InterviewResponse get(String email, Long id);
    InterviewResponse update(String email, Long id, InterviewRequest request);
    void delete(String email, Long id);
}
