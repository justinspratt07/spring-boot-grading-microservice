package com.portfolio.grading.service;

import com.portfolio.grading.dto.GradeRequest;
import com.portfolio.grading.dto.GradeResponse;
import org.springframework.stereotype.Service;

@Service
public class GradeService {
    public GradeResponse calculateGrade(GradeRequest request) {
        String letterGrade = toLetterGrade(request.score());
        return new GradeResponse(
                request.studentName(),
                request.assignmentName(),
                request.score(),
                letterGrade
        );
    }

    public String toLetterGrade(double score) {
        if (score >= 90) {
            return "A";
        }
        if (score >= 80) {
            return "B";
        }
        if (score >= 70) {
            return "C";
        }
        if (score >= 60) {
            return "D";
        }
        return "F";
    }
}
