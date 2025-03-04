package com.dw.forest.controller;

import com.dw.forest.dto.CompletionDTO;
import com.dw.forest.dto.CourseReadDTO;
import com.dw.forest.dto.CourseWithStudentsDTO;
import com.dw.forest.model.Completion;
import com.dw.forest.service.CompletionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/completion")
public class CompletionController {
    @Autowired
    CompletionService completionService;

    @GetMapping("/complete/traveler")
    public ResponseEntity<List<CourseReadDTO>> getCompletedCoursesByTraveler(HttpServletRequest request) {
        List<CourseReadDTO> completedCourses = completionService.getCompletedCoursesByTraveler(request);
        return new ResponseEntity<>(completedCourses, HttpStatus.OK);
    }

    @GetMapping("/complete/{course_id}")
    public ResponseEntity<Boolean> checkIfCompleted(HttpServletRequest request, @PathVariable Long course_id) {
        boolean hasCompleted = completionService.completedCourse(request, course_id);
        return new ResponseEntity<>(hasCompleted, HttpStatus.OK);
    }

    @GetMapping("{courseId}/completions")
    public ResponseEntity<CourseWithStudentsDTO> getCourseWithStudents(HttpServletRequest request, @PathVariable Long courseId) {
        return new ResponseEntity<>(completionService.getCourseWithStudents(request,courseId), HttpStatus.OK);
    }

    @PutMapping("/{course_id}/complete")
    public ResponseEntity<String> completeCourse(@PathVariable Long course_id, HttpServletRequest request) {
        String message = completionService.completeCourse(request, course_id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
