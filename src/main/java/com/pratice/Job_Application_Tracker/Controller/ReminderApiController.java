package com.pratice.Job_Application_Tracker.Controller;

import com.pratice.Job_Application_Tracker.dto.PageResponse;
import com.pratice.Job_Application_Tracker.dto.ReminderRequest;
import com.pratice.Job_Application_Tracker.dto.ReminderResponse;
import com.pratice.Job_Application_Tracker.service.ReminderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reminders")
public class ReminderApiController {
    private final ReminderService reminders;
    public ReminderApiController(ReminderService reminders) { this.reminders = reminders; }

    @PostMapping
    public ResponseEntity<ReminderResponse> create(Authentication authentication, @Valid @RequestBody ReminderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reminders.create(authentication.getName(), request));
    }
    @GetMapping
    public PageResponse<ReminderResponse> list(Authentication authentication,
        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "50") int size) {
        return reminders.list(authentication.getName(), page, size);
    }
    @GetMapping("/{id}")
    public ReminderResponse get(Authentication authentication, @PathVariable @Positive Long id) {
        return reminders.get(authentication.getName(), id);
    }
    @PutMapping("/{id}")
    public ReminderResponse update(Authentication authentication, @PathVariable @Positive Long id,
        @Valid @RequestBody ReminderRequest request) {
        return reminders.update(authentication.getName(), id, request);
    }
    @PatchMapping("/{id}/complete")
    public ReminderResponse complete(Authentication authentication, @PathVariable @Positive Long id) {
        return reminders.complete(authentication.getName(), id);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(Authentication authentication, @PathVariable @Positive Long id) {
        reminders.delete(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
