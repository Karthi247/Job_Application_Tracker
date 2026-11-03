package com.pratice.Job_Application_Tracker.Controller;

import com.pratice.Job_Application_Tracker.dto.InterviewRequest;
import com.pratice.Job_Application_Tracker.dto.InterviewResponse;
import com.pratice.Job_Application_Tracker.dto.PageResponse;
import com.pratice.Job_Application_Tracker.service.InterviewService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interviews")
public class InterviewApiController {
    private final InterviewService interviews;
    public InterviewApiController(InterviewService interviews) { this.interviews = interviews; }

    @PostMapping
    public ResponseEntity<InterviewResponse> create(Authentication authentication, @Valid @RequestBody InterviewRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(interviews.create(authentication.getName(), request));
    }

    @GetMapping
    public PageResponse<InterviewResponse> list(Authentication authentication,
        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "50") int size,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return interviews.list(authentication.getName(), page, size, from, to);
    }

    @GetMapping("/{id}")
    public InterviewResponse get(Authentication authentication, @PathVariable @Positive Long id) {
        return interviews.get(authentication.getName(), id);
    }

    @PutMapping("/{id}")
    public InterviewResponse update(Authentication authentication, @PathVariable @Positive Long id,
        @Valid @RequestBody InterviewRequest request) {
        return interviews.update(authentication.getName(), id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(Authentication authentication, @PathVariable @Positive Long id) {
        interviews.delete(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
