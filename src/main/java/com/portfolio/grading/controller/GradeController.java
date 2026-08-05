package com.portfolio.grading.controller;

import com.portfolio.grading.dto.GradeRequest;
import com.portfolio.grading.dto.GradeResponse;
import com.portfolio.grading.service.GradeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/grades")
public class GradeController {
    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "ok");
    }

    @PostMapping
    public ResponseEntity<GradeResponse> calculateGrade(@Valid @RequestBody GradeRequest request) {
        return ResponseEntity.ok(gradeService.calculateGrade(request));
    }
}
