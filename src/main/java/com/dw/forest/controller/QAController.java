package com.dw.forest.controller;

import com.dw.forest.dto.QaDTO;
import com.dw.forest.dto.QaReadDTO;
import com.dw.forest.model.QA;
import com.dw.forest.service.QAService;
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

    @GetMapping
    public ResponseEntity<List<QaReadDTO>> getAllQas() {
        return new ResponseEntity<>(qaService.getAllQas(), HttpStatus.OK);
    }

    @PostMapping("/ask")
    public ResponseEntity<QaReadDTO> createQuestion(@RequestBody QaDTO qaDTO) {
        return new ResponseEntity<>(qaService.createQuestion(qaDTO), HttpStatus.CREATED);
    }

    @PostMapping("/reply")
    public ResponseEntity<QaReadDTO> createAnswer(@RequestBody QaDTO qaDTO) {
        return new ResponseEntity<>(qaService.createAnswer(qaDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{qa_id}")
    public ResponseEntity<String> deleteById(@PathVariable Long qa_id) {
        return new ResponseEntity<>(qaService.deleteById(qa_id), HttpStatus.OK);
    }

    @PutMapping("/update/{qa_id}")
    public ResponseEntity<QaReadDTO> updateById(@PathVariable Long qa_id, @RequestBody QaDTO qaDTO) {
        return new ResponseEntity<>(qaService.updateById(qa_id, qaDTO), HttpStatus.OK);
    }

    @GetMapping("/notifications")
    public ResponseEntity<List<QaReadDTO>> searchByTitle(@RequestParam String title) {
        return new ResponseEntity<>(qaService.searchByTitle(title), HttpStatus.OK);
    }

    @GetMapping("/contents")
    public ResponseEntity<List<QaReadDTO>> searchByContent(@RequestParam String content) {
        return new ResponseEntity<>(qaService.searchByContent(content), HttpStatus.OK);
    }
}
