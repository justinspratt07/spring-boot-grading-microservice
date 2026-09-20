# Spring Boot Grading Microservice

A Java REST API that turns assignment scores into consistent letter grades. This focused backend portfolio project demonstrates request validation, clear API errors, isolated business rules, and automated testing for a grading workflow.

[![Java CI](https://github.com/justinspratt07/spring-boot-grading-microservice/actions/workflows/ci.yml/badge.svg)](https://github.com/justinspratt07/spring-boot-grading-microservice/actions/workflows/ci.yml)

## What it demonstrates

- REST endpoint design with Spring MVC and typed request/response records.
- Jakarta Bean Validation for required names and scores, including missing scores.
- Centralized, field-specific HTTP 400 responses that a client can display.
- Business logic separated from HTTP handling, with JUnit 5 and MockMvc tests.
- Maven builds and GitHub Actions continuous integration.

The service is stateless: it calculates a grade and returns the submission without storing student data. There is no database, authentication, or external service dependency. It is a demonstration API; deployment-specific access controls and operations are outside its scope.

## Grading rules

Scores are required and must be between 0 and 100 inclusive. Decimal scores are accepted and are not rounded before grading.

| Score | Letter grade |
| --- | --- |
| 90 through 100 | A |
| 80 up to 90 | B |
| 70 up to 80 | C |
| 60 up to 70 | D |
| 0 up to 60 | F |

For example, 89.9 returns B and 90 returns A. Both names must contain at least one non-whitespace character.

## Setup and run

Requirements: JDK 17 and Maven 3.6.3 or later on your PATH. Verify with `java -version` and `mvn -version`; Maven should report Java 17. No credentials or environment configuration are needed. The first build needs internet access to download dependencies.

```bash
git clone https://github.com/justinspratt07/spring-boot-grading-microservice.git
cd spring-boot-grading-microservice
mvn spring-boot:run
```

The server listens at `http://localhost:8080`. Stop it with Ctrl+C. To use a different port:

```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

To build and run the executable JAR:

```bash
mvn clean verify
java -jar target/spring-boot-grading-microservice-0.0.1-SNAPSHOT.jar
```

## API

### Calculate a grade

`POST /api/grades` with `Content-Type: application/json` returns HTTP 200:

```json
{
  "studentName": "Jordan Lee",
  "assignmentName": "Midterm Exam",
  "score": 91.5
}
```

Response:

```json
{
  "studentName": "Jordan Lee",
  "assignmentName": "Midterm Exam",
  "score": 91.5,
  "letterGrade": "A"
}
```

macOS/Linux:

```bash
curl -X POST http://localhost:8080/api/grades \
  -H 'Content-Type: application/json' \
  -d '{"studentName":"Jordan Lee","assignmentName":"Midterm Exam","score":91.5}'
```

Windows PowerShell:

```powershell
$body = @{ studentName = 'Jordan Lee'; assignmentName = 'Midterm Exam'; score = 91.5 } | ConvertTo-Json
Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/grades -ContentType 'application/json' -Body $body
```

In Postman, select POST, enter the same URL, choose Body > raw > JSON, and paste the request above.

### Validation errors

Missing or null scores, out-of-range scores, and missing or blank names return HTTP 400. Multiple invalid fields are reported together. For a request that omits `score`:

```json
{
  "status": 400,
  "message": "Validation failed",
  "errors": {
    "score": ["score is required"]
  }
}
```

Malformed JSON, an empty body, or an incompatible field value returns HTTP 400 with the same response structure:

```json
{
  "status": 400,
  "message": "Request body must be valid JSON with the expected field types",
  "errors": {}
}
```

These responses omit stack traces and internal exception details.

### Health check

`GET /api/grades/health` returns HTTP 200 and `{"status":"ok"}`. This is a simple endpoint availability check, not a dependency readiness monitor.

## Tests and CI

```bash
mvn test
mvn clean verify
```

`test` runs the tests; `verify` also packages the executable JAR. Test reports are written to `target/surefire-reports/`.

- Service tests cover 100, 90, 89.9, 80, 79.9, 70, 69.9, 60, 59.9, and 0, plus preservation of submission details.
- Controller tests cover successful submissions, inclusive score limits, scores outside the range, missing/null scores, blank names, combined validation errors, unreadable JSON, and health.
- GitHub Actions runs `mvn --batch-mode --no-transfer-progress clean verify` with Java 17 for pushes and pull requests.

## Architecture

```text
src/main/java/com/portfolio/grading/
  GradingMicroserviceApplication.java
  controller/GradeController.java       HTTP endpoints
  controller/ApiExceptionHandler.java   Shared HTTP 400 responses
  dto/GradeRequest.java                Request validation
  dto/GradeResponse.java                Successful response
  service/GradeService.java             Grading rules
src/test/java/com/portfolio/grading/
  controller/GradeControllerTest.java
  service/GradeServiceTest.java
```

Validation occurs at the HTTP boundary before the grading service is called. Keeping the rules in one service makes changes to grading thresholds straightforward.

## Dependency maintenance

Uses Java 17 and Spring Boot 3.5.16. This is the final open-source 3.5.x release, retained here to stay within Spring Boot 3.x. [Spring's release announcement](https://spring.io/blog/2026/06/25/spring-boot-3-5-16-available-now/) describes the end of open-source support. A production deployment should plan a supported major-version upgrade or commercial support.

## License

[MIT](LICENSE) — Copyright (c) 2026 Justin Spratt.
