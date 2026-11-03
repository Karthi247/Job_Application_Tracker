package com.pratice.Job_Application_Tracker.Controller;

import com.pratice.Job_Application_Tracker.dto.CalendarEventResponse;
import com.pratice.Job_Application_Tracker.service.CalendarService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/calendar")
public class CalendarApiController {
    private final CalendarService calendar;
    public CalendarApiController(CalendarService calendar) { this.calendar = calendar; }
    @GetMapping("/events")
    public List<CalendarEventResponse> events(Authentication authentication,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return calendar.events(authentication.getName(), from, to);
    }
}
