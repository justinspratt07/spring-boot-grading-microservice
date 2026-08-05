package com.portfolio.grading.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GradeServiceTest {
    private final GradeService gradeService = new GradeService();

    @Test
    void convertsScoresToLetterGrades() {
        assertEquals("A", gradeService.toLetterGrade(90));
        assertEquals("B", gradeService.toLetterGrade(80));
        assertEquals("C", gradeService.toLetterGrade(70));
        assertEquals("D", gradeService.toLetterGrade(60));
        assertEquals("F", gradeService.toLetterGrade(59.9));
    }
}
