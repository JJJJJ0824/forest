package com.dw.forest.controller;

import com.dw.forest.dto.QaDTO;
import com.dw.forest.dto.QaReadDTO;
import com.dw.forest.model.QA;
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
    public ResponseEntity<List<QaReadDTO>> getAllQas() {
        return new ResponseEntity<>(qaService.getAllQas(), HttpStatus.OK);
    }

    @PostMapping("/ask")
    public ResponseEntity<QaReadDTO> createQuestion(HttpServletRequest request, @RequestBody QaDTO qaDTO) {
        return new ResponseEntity<>(qaService.createQuestion(request, qaDTO), HttpStatus.CREATED);
    }

    @PostMapping("/reply")
    public ResponseEntity<QaReadDTO> createAnswer(HttpServletRequest request,@RequestBody QaDTO qaDTO) {
        return new ResponseEntity<>(qaService.createAnswer(request, qaDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{qa_id}")
    public ResponseEntity<String> deleteById(HttpServletRequest request, @PathVariable Long qa_id) {
        return new ResponseEntity<>(qaService.deleteById(request, qa_id), HttpStatus.OK);
    }

    @PutMapping("/update/{qa_id}")
    public ResponseEntity<QaReadDTO> updateById(HttpServletRequest request, @PathVariable Long qa_id, @RequestBody QaDTO qaDTO) {
        return new ResponseEntity<>(qaService.updateById(qa_id, qaDTO, request), HttpStatus.OK);
    }

    @GetMapping("/{qa_id}")
    public ResponseEntity<QaReadDTO> getQA(@PathVariable Long qa_id) {
        return new ResponseEntity<>(qaService.getQA(qa_id), HttpStatus.OK);
    }

    @GetMapping("/title")
    public ResponseEntity<List<QaReadDTO>> searchByTitle(@RequestParam String title) {
        return new ResponseEntity<>(qaService.searchByTitle(title), HttpStatus.OK);
    }

    @GetMapping("/content")
    public ResponseEntity<List<QaReadDTO>> searchByContent(@RequestParam String content) {
        return new ResponseEntity<>(qaService.searchByContent(content), HttpStatus.OK);
    }

    @GetMapping("/title-content")
    public ResponseEntity<List<QaReadDTO>> searchByTitleAndContent(@RequestParam String title, @RequestParam String content) {
        return new ResponseEntity<>(qaService.searchByTitleAndContent(title, content), HttpStatus.OK);
    }
}
