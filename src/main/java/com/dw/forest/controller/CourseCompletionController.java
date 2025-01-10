package com.dw.forest.controller;

import com.dw.forest.service.CourseCompletionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CourseCompletionController {
    @Autowired
    CourseCompletionService courseCompletionService;


}
