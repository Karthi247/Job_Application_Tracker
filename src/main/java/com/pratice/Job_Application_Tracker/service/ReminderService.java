package com.pratice.Job_Application_Tracker.service;

import com.pratice.Job_Application_Tracker.dto.PageResponse;
import com.pratice.Job_Application_Tracker.dto.ReminderRequest;
import com.pratice.Job_Application_Tracker.dto.ReminderResponse;

public interface ReminderService {
    ReminderResponse create(String email, ReminderRequest request);
    PageResponse<ReminderResponse> list(String email, int page, int size);
    ReminderResponse get(String email, Long id);
    ReminderResponse update(String email, Long id, ReminderRequest request);
    ReminderResponse complete(String email, Long id);
    void delete(String email, Long id);
}
