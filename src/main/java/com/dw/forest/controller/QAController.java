package com.dw.forest.controller;

import com.dw.forest.dto.QaDTO;
import com.dw.forest.service.QAService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/q_a")
public class QAController {
    @Autowired
    QAService qaService;

    @GetMapping("/all")
    public ResponseEntity<List<QaDTO>> getAllQs(HttpServletRequest request) {
        return new ResponseEntity<>(qaService.getAllQs(request), HttpStatus.OK);
    }

    @GetMapping("/a")
    public ResponseEntity<List<QaDTO>> getAllQas(HttpServletRequest request) {
        return new ResponseEntity<>(qaService.getAllAs(request), HttpStatus.OK);
    }

    @PostMapping("/ask")
    public ResponseEntity<QaDTO> createQuestion(HttpServletRequest request, @RequestBody QaDTO qaDTO) {
        return new ResponseEntity<>(qaService.createQuestion(request, qaDTO), HttpStatus.CREATED);
    }

    @PostMapping("/{qa_id}/reply")
    public ResponseEntity<QaDTO> createAnswer(HttpServletRequest request, @RequestBody QaDTO qaDTO, @PathVariable Long qa_id) {
        return new ResponseEntity<>(qaService.createAnswer(request, qaDTO, qa_id), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{qa_id}")
    public ResponseEntity<String> deleteById(HttpServletRequest request, @PathVariable Long qa_id) {
        return new ResponseEntity<>(qaService.deleteById(request, qa_id), HttpStatus.OK);
    }

    @PutMapping("/update/{qa_id}")
    public ResponseEntity<QaDTO> updateById(HttpServletRequest request, @PathVariable Long q_id, @RequestBody QaDTO qaDTO) {
        return new ResponseEntity<>(qaService.updateById(q_id, qaDTO, request), HttpStatus.OK);
    }

    @GetMapping("/{q_id}")
    public ResponseEntity<QaDTO> getQA(HttpServletRequest request, @PathVariable Long q_id) {
        return new ResponseEntity<>(qaService.getQA(request, q_id), HttpStatus.OK);
    }

    @GetMapping("/q/title")
    public ResponseEntity<List<QaDTO>> searchByQuestionTitle(HttpServletRequest request,@RequestParam String title) {
        return new ResponseEntity<>(qaService.searchByQuestionTitle(request, title), HttpStatus.OK);
    }

    @GetMapping("/a/title")
    public ResponseEntity<List<QaDTO>> searchByAnswerTitle(HttpServletRequest request,@RequestParam String content) {
        return new ResponseEntity<>(qaService.searchByAnswerTitle(request, content), HttpStatus.OK);
    }

    @GetMapping("/q/content")
    public ResponseEntity<List<QaDTO>> searchByQuestionContent(HttpServletRequest request,@RequestParam String content) {
        return new ResponseEntity<>(qaService.searchByQuestionContent(request, content), HttpStatus.OK);
    }

    @GetMapping("/a/content")
    public ResponseEntity<List<QaDTO>> searchByAnswerContent(HttpServletRequest request,@RequestParam String content) {
        return new ResponseEntity<>(qaService.searchByAnswerContent(request,content), HttpStatus.OK);
    }

    @GetMapping("/q/title-content")
    public ResponseEntity<List<QaDTO>> searchByQuestionTitleAndContent(HttpServletRequest request,@RequestParam String title, @RequestParam String content) {
        return new ResponseEntity<>(qaService.searchByQuestionTitleAndContent(request, title, content), HttpStatus.OK);
    }

    @GetMapping("/a/title-content")
    public ResponseEntity<List<QaDTO>> searchByAnswerTitleAndContent(HttpServletRequest request, @RequestParam String title, @RequestParam String content) {
        return new ResponseEntity<>(qaService.searchByAnswerTitleAndContent(request, title, content), HttpStatus.OK);
    }
}
