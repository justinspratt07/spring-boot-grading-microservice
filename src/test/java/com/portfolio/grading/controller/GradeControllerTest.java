package com.portfolio.grading.controller;

import com.portfolio.grading.service.GradeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.Arguments;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

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
                .andExpect(jsonPath("$.studentName").value("Jordan Lee"))
                .andExpect(jsonPath("$.assignmentName").value("Midterm Exam"))
                .andExpect(jsonPath("$.score").value(91.5))
                .andExpect(jsonPath("$.letterGrade").value("A"));
    }

    @ParameterizedTest
    @MethodSource("invalidSubmissions")
    void rejectsInvalidSubmission(String body, String field, String message) throws Exception {
        mockMvc.perform(post("/api/grades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors." + field + "[0]").value(message));
    }

    static Stream<Arguments> invalidSubmissions() {
        return Stream.of(
                Arguments.of("{\"studentName\":\"Jordan\",\"assignmentName\":\"Exam\",\"score\":100.1}", "score", "score must be at most 100"),
                Arguments.of("{\"studentName\":\"Jordan\",\"assignmentName\":\"Exam\",\"score\":-0.1}", "score", "score must be at least 0"),
                Arguments.of("{\"studentName\":\"Jordan\",\"assignmentName\":\"Exam\"}", "score", "score is required"),
                Arguments.of("{\"studentName\":\"Jordan\",\"assignmentName\":\"Exam\",\"score\":null}", "score", "score is required"),
                Arguments.of("{\"studentName\":\"  \",\"assignmentName\":\"Exam\",\"score\":90}", "studentName", "studentName is required"),
                Arguments.of("{\"studentName\":\"Jordan\",\"assignmentName\":\"  \",\"score\":90}", "assignmentName", "assignmentName is required"));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0, 100})
    void acceptsInclusiveScoreLimits(double score) throws Exception {
        mockMvc.perform(post("/api/grades").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"studentName\":\"Jordan\",\"assignmentName\":\"Exam\",\"score\":" + score + "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.score").value(score))
                .andExpect(jsonPath("$.letterGrade").value(score == 0 ? "F" : "A"));
    }

    @Test
    void reportsAllMissingFields() throws Exception {
        mockMvc.perform(post("/api/grades").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.studentName[0]").value("studentName is required"))
                .andExpect(jsonPath("$.errors.assignmentName[0]").value("assignmentName is required"))
                .andExpect(jsonPath("$.errors.score[0]").value("score is required"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"{", "", "{\"score\":\"invalid\"}"})
    void rejectsUnreadableRequestBody(String body) throws Exception {
        mockMvc.perform(post("/api/grades").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Request body must be valid JSON with the expected field types"))
                .andExpect(jsonPath("$.errors").isEmpty());
    }
}
