# Spring Boot Grading Microservice

A small REST API for submitting numeric grades and receiving letter-grade feedback. The project is designed as a portfolio-ready Spring Boot microservice with validation, service-layer logic, and focused unit tests.

## Features

- `POST /api/grades` accepts a student name, assignment name, and score.
- Validates that names are present and scores are between `0` and `100`.
- Returns the original submission with a calculated letter grade.
- Keeps grading rules isolated in a service for easy testing and future changes.

## Tech Stack

- Java 17
- Spring Boot 3
- Maven
- JUnit 5
- MockMvc

## API Example

```http
POST /api/grades
Content-Type: application/json

{
  "studentName": "Jordan Lee",
  "assignmentName": "Midterm Exam",
  "score": 91.5
}
```

```json
{
  "studentName": "Jordan Lee",
  "assignmentName": "Midterm Exam",
  "score": 91.5,
  "letterGrade": "A"
}
```

## Run Locally

```bash
mvn spring-boot:run
```

Then open:

```text
http://localhost:8080/api/grades/health
```

## Run Tests

```bash
mvn test
```

## Project Structure

```text
src/main/java/com/portfolio/grading
  GradingMicroserviceApplication.java
  controller/GradeController.java
  dto/GradeRequest.java
  dto/GradeResponse.java
  service/GradeService.java

src/test/java/com/portfolio/grading
  controller/GradeControllerTest.java
  service/GradeServiceTest.java
```
