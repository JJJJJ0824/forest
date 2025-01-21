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
        List<CheckListDTO> incompleteChecklists = checklistService.getIncompleteChecklists(request);
        return ResponseEntity.ok(incompleteChecklists);
    }

    @GetMapping("/me/check")
    public ResponseEntity<List<CheckListDTO>> getChecklistsByTraveler(HttpServletRequest request) {
        return new ResponseEntity<>(checklistService.getChecklistsByTraveler(request), HttpStatus.OK);
    }

    @GetMapping("/recommend")
    public ResponseEntity<List<CourseReadDTO>> recommendCourses(HttpServletRequest request) {
        return new ResponseEntity<>(checklistService.recommendCourses(request), HttpStatus.OK);
    }

    @GetMapping("/completion")
    public ResponseEntity<Map<String, Object>> getTravelerChecklistCompletion(HttpServletRequest request) {
        boolean isCompleted = checklistService.checklistCompleted(request);
        Map<String, Object> response = new HashMap<>();
        response.put("traveler_name", request.getSession().getAttribute("travelerName"));
        response.put("completed", isCompleted);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/reset")
    public ResponseEntity<List<CheckListDTO>> resetChecklist(HttpServletRequest request) {
        return new ResponseEntity<>(checklistService.resetChecklist(request), HttpStatus.OK);
    }
}
