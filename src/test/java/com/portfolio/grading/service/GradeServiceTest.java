package com.portfolio.grading.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import com.portfolio.grading.dto.GradeRequest;
import com.portfolio.grading.dto.GradeResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GradeServiceTest {
    private final GradeService gradeService = new GradeService();

    @ParameterizedTest
    @CsvSource({"100,A", "90,A", "89.9,B", "80,B", "79.9,C", "70,C",
            "69.9,D", "60,D", "59.9,F", "0,F"})
    void convertsScoresAtGradeBoundaries(double score, String expectedGrade) {
        assertEquals(expectedGrade, gradeService.toLetterGrade(score));
    }

    @Test
    void preservesSubmissionDetails() {
        GradeRequest request = new GradeRequest("Jordan Lee", "Midterm Exam", 91.5);
        assertEquals(new GradeResponse("Jordan Lee", "Midterm Exam", 91.5, "A"),
                gradeService.calculateGrade(request));
    }
}
