package com.dw.forest.controller;

import com.dw.forest.dto.CheckListDTO;
import com.dw.forest.dto.CourseReadDTO;
import com.dw.forest.service.ChecklistService;
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

    @GetMapping("/{traveler_name}/all")
    public ResponseEntity<List<CheckListDTO>> getAllCheckList() {
        return new ResponseEntity<>(checklistService.getAllChecklists(), HttpStatus.OK);
    }

    @GetMapping("/{traveler_name}/all")
    public ResponseEntity<List<CheckListDTO>> getChecklistsByTraveler(@PathVariable String traveler_name) {
        List<CheckListDTO> checklists = checklistService.getChecklistsByTraveler(traveler_name);
        return new ResponseEntity<>(checklists, HttpStatus.OK);
    }

    @GetMapping("/recommend/{traveler_name}")
    public ResponseEntity<List<CourseReadDTO>> recommendCourses(@PathVariable String traveler_name) {
        List<CourseReadDTO> recommendedCourses = checklistService.recommendCourses(traveler_name);
        return new ResponseEntity<>(recommendedCourses, HttpStatus.OK);
    }

    @GetMapping("/{travelerName}/completion")
    public ResponseEntity<Map<String, Object>> getTravelerChecklistCompletion(@PathVariable String travelerName) {
        boolean isCompleted = checklistService.checklistCompleted(travelerName);

        Map<String, Object> response = new HashMap<>();
        response.put("traveler_name", travelerName);
        response.put("completed", isCompleted);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{travelerName}/reset")
    public ResponseEntity<List<CheckListDTO>> resetChecklist(@PathVariable String travelerName) {

        List<CheckListDTO> updateChecklists = checklistService.resetChecklist(travelerName);
        return ResponseEntity.ok(updateChecklists);
    }
}
