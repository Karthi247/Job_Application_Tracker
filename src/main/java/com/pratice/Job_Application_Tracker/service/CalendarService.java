package com.pratice.Job_Application_Tracker.service;

import com.pratice.Job_Application_Tracker.dto.CalendarEventResponse;
import java.time.LocalDate;
import java.util.List;

public interface CalendarService {
    List<CalendarEventResponse> events(String email, LocalDate from, LocalDate to);
}
