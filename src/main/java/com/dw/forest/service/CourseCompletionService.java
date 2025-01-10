package com.dw.forest.service;

import com.dw.forest.model.CourseCompletion;
import com.dw.forest.repository.CourseCompletionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseCompletionService {
    @Autowired
    CourseCompletionRepository courseCompletionRepository;

    public List<CourseCompletion> getAllCourseCompletions() {
        return courseCompletionRepository.findAll();
    }

}
