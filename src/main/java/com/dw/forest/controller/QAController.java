package com.dw.forest.controller;

import com.dw.forest.dto.QaDTO;
import com.dw.forest.service.QAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/q_a")
public class QAController {
    @Autowired
    QAService qaService;

    @GetMapping("/all")
    public ResponseEntity<List<QaDTO>> getAllForumPosts() {
        return new ResponseEntity<>(qaService.getAllForumPosts(), HttpStatus.OK);
    }

    @PostMapping("/question/ask")
    public ResponseEntity<QaDTO> saveQuestion(QaDTO qaDTO) {
        return new ResponseEntity<>(qaService.saveQuestion(qaDTO), HttpStatus.CREATED);
    }

    @PostMapping("/answer/reply")
    public ResponseEntity<QaDTO> saveAnswer(QaDTO qaDTO) {
        return new ResponseEntity<>(qaService.saveAnswer(qaDTO), HttpStatus.CREATED);
    }
}
