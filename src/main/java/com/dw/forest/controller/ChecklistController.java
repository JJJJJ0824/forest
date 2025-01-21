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
    public ResponseEntity<List<CheckListDTO>> getAllIncompleteChecklists(@RequestParam String travelerName) {
        List<CheckListDTO> incompleteChecklists = checklistService.getIncompleteChecklists(travelerName);
        return ResponseEntity.ok(incompleteChecklists);
    }

    @GetMapping("/{traveler_name}/all")
    public ResponseEntity<List<CheckListDTO>> getChecklistsByTraveler(@PathVariable String traveler_name) {
        return new ResponseEntity<>(checklistService.getChecklistsByTraveler(traveler_name), HttpStatus.OK);
    }

    @GetMapping("/recommend")
    public ResponseEntity<List<CourseReadDTO>> recommendCourses(HttpServletRequest request) {
        return new ResponseEntity<>(checklistService.recommendCourses(request), HttpStatus.OK);
    }

    @GetMapping("/{travelerName}/completion")
    public ResponseEntity<Map<String, Object>> getTravelerChecklistCompletion(@PathVariable String travelerName) {
        boolean isCompleted = checklistService.checklistCompleted(travelerName);
        Map<String, Object> response = new HashMap<>();
        response.put("traveler_name", travelerName);
        response.put("completed", isCompleted);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{travelerName}/reset")
    public ResponseEntity<List<CheckListDTO>> resetChecklist(@PathVariable String travelerName) {
        return new ResponseEntity<>(checklistService.resetChecklist(travelerName), HttpStatus.OK);
    }
}
