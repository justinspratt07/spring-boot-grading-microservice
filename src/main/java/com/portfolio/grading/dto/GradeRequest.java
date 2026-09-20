package com.portfolio.grading.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GradeRequest(
        @NotBlank(message = "studentName is required")
        String studentName,

        @NotBlank(message = "assignmentName is required")
        String assignmentName,

        @NotNull(message = "score is required")
        @DecimalMin(value = "0.0", message = "score must be at least 0")
        @DecimalMax(value = "100.0", message = "score must be at most 100")
        Double score
) {
}
