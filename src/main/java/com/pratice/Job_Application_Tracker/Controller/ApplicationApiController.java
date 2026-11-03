package com.pratice.Job_Application_Tracker.Controller;

import com.pratice.Job_Application_Tracker.dto.ApplicationCreateRequest;
import com.pratice.Job_Application_Tracker.dto.ApplicationResponse;
import com.pratice.Job_Application_Tracker.dto.ApplicationStatusUpdateRequest;
import com.pratice.Job_Application_Tracker.dto.PageResponse;
import com.pratice.Job_Application_Tracker.enums.ApplicationPriority;
import com.pratice.Job_Application_Tracker.enums.ApplicationStatus;
import com.pratice.Job_Application_Tracker.service.ApplicationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/applications")
public class ApplicationApiController {
    private final ApplicationService applicationService;
    public ApplicationApiController(ApplicationService applicationService) { this.applicationService = applicationService; }

    @PostMapping
    public ResponseEntity<ApplicationResponse> create(Authentication authentication,
        @Valid @RequestBody ApplicationCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(applicationService.create(authentication.getName(), request));
    }

    @GetMapping
    public PageResponse<ApplicationResponse> list(Authentication authentication,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "desc") String direction,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) ApplicationStatus status,
        @RequestParam(required = false) ApplicationPriority priority,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate appliedFrom,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate appliedTo) {
        return applicationService.list(authentication.getName(), page, size, sortBy, direction, search,
            status, priority, appliedFrom, appliedTo);
    }

    @GetMapping("/{id}")
    public ApplicationResponse get(Authentication authentication, @PathVariable @Positive Long id) {
        return applicationService.get(authentication.getName(), id);
    }

    @PutMapping("/{id}")
    public ApplicationResponse update(Authentication authentication, @PathVariable @Positive Long id,
        @Valid @RequestBody ApplicationCreateRequest request) {
        return applicationService.update(authentication.getName(), id, request);
    }

    @PatchMapping("/{id}/status")
    public ApplicationResponse updateStatus(Authentication authentication, @PathVariable @Positive Long id,
        @Valid @RequestBody ApplicationStatusUpdateRequest request) {
        return applicationService.updateStatus(authentication.getName(), id, request.status());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(Authentication authentication, @PathVariable @Positive Long id) {
        applicationService.delete(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
