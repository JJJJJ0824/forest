package com.dw.forest.controller;

import com.dw.forest.model.QA;
import com.dw.forest.service.QAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class QAController {
    @Autowired
    QAService QAService;

    @GetMapping("/forum-posts/all")
    public List<QA> getAllForumPosts() {
        return QAService.getAllForumPosts();
    }
}
