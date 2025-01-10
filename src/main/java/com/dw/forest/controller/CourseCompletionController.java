package com.dw.forest.controller;

import com.dw.forest.model.CourseCompletion;
import com.dw.forest.service.CourseCompletionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CourseCompletionController {
    @Autowired
    CourseCompletionService courseCompletionService;

    @GetMapping("/course-completions/all")
    public List<CourseCompletion> getAllCourseCompletions() {
        return courseCompletionService.getAllCourseCompletions();
    }

}
