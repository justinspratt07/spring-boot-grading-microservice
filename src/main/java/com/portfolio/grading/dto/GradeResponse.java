package com.portfolio.grading.dto;

public record GradeResponse(
        String studentName,
        String assignmentName,
        double score,
        String letterGrade
) {
}
