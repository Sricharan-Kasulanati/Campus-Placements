package com.example.campus_placements.exam.controller;

import com.example.campus_placements.exam.dto.AddExamRequest;
import com.example.campus_placements.exam.dto.ExamResponse;
import com.example.campus_placements.exam.service.ExamService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @PostMapping
    public ResponseEntity<ExamResponse> addExam(@Valid @RequestBody AddExamRequest request) {
        ExamResponse resp = examService.addExam(request);
        return ResponseEntity.ok(resp);
    }
}
