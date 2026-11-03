package com.pratice.Job_Application_Tracker.dto;

import java.util.List;
import org.springframework.data.domain.Page;

public record PageResponse<T>(List<T> content, int page, int size, long totalElements, int totalPages) {
    public static <T, R> PageResponse<R> from(Page<T> source, java.util.function.Function<T, R> mapper) {
        return new PageResponse<>(source.getContent().stream().map(mapper).toList(), source.getNumber(),
            source.getSize(), source.getTotalElements(), source.getTotalPages());
    }
}
