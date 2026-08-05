package com.portfolio.grading.controller;

import com.portfolio.grading.service.GradeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GradeController.class)
@Import(GradeService.class)
class GradeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void healthEndpointReturnsOk() throws Exception {
        mockMvc.perform(get("/api/grades/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ok"));
    }

    @Test
    void calculatesGradeForValidSubmission() throws Exception {
        mockMvc.perform(post("/api/grades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "studentName": "Jordan Lee",
                                  "assignmentName": "Midterm Exam",
                                  "score": 91.5
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.letterGrade").value("A"));
    }

    @Test
    void rejectsInvalidScore() throws Exception {
        mockMvc.perform(post("/api/grades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "studentName": "Jordan Lee",
                                  "assignmentName": "Midterm Exam",
                                  "score": 125
                                }
                                """))
                .andExpect(status().isBadRequest());
    }
}
