package com.dw.forest.controller;

import com.dw.forest.model.Completion;
import com.dw.forest.service.CompletionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/completion")
public class CompletionController {
    @Autowired
    CompletionService completionService;

    @GetMapping("/all")
    public List<Completion> getAllCompletions() {
        return completionService.getAllCompletions();
    }
}
