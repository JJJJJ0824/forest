package com.dw.forest.controller;

import com.dw.forest.dto.CheckListDTO;
import com.dw.forest.dto.CourseReadDTO;
import com.dw.forest.service.ChecklistService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/checklist")
public class ChecklistController {
    @Autowired
    ChecklistService checklistService;

    @GetMapping("/all")
    public ResponseEntity<List<CheckListDTO>> getAllIncompleteChecklists(HttpServletRequest request) {
        return new ResponseEntity<>(checklistService.getIncompleteChecklists(request), HttpStatus.OK);
    }

    @PostMapping("/submit")
    public ResponseEntity<CheckListDTO> submitMyChecklist(CheckListDTO checkListDTO, HttpServletRequest request) {
        return new ResponseEntity<>(checklistService.submitMyChecklist(checkListDTO, request), HttpStatus.CREATED);
    }

    @GetMapping("/me/check")
    public ResponseEntity<List<CheckListDTO>> getChecklistByTraveler(HttpServletRequest request) {
        return new ResponseEntity<>(checklistService.getChecklistByTraveler(request), HttpStatus.OK);
    }

    @GetMapping("/recommend")
    public ResponseEntity<List<CourseReadDTO>> recommendCourses(HttpServletRequest request) {
        return new ResponseEntity<>(checklistService.recommendCourses(request), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<CheckListDTO> updateMyChecklist(CheckListDTO checkListDTO, HttpServletRequest request) {
        return new ResponseEntity<>(checklistService.updateMyChecklist(checkListDTO, request), HttpStatus.OK);
    }

    @GetMapping("/completion")
    public ResponseEntity<Map<String, Object>> getTravelerChecklistCompletion(HttpServletRequest request) {
        boolean isCompleted = checklistService.checklistCompleted(request);
        Map<String, Object> response = new HashMap<>();
        response.put("travelerName", request.getSession().getAttribute("travelerName"));
        response.put("completed", isCompleted);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteChecklist(List<CheckListDTO> checkListDTOS, HttpServletRequest request) {
        return new ResponseEntity<>(checklistService.deleteChecklist(checkListDTOS, request), HttpStatus.OK);
    }

    @PostMapping("/feedback")
    public ResponseEntity<String> submitFeedback(HttpServletRequest request, @RequestParam Long checklistId, @RequestParam String feedbackText) {
        String responseMessage = checklistService.saveFeedback(request, checklistId, feedbackText);
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }
}
