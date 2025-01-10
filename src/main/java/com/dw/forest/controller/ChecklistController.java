package com.dw.forest.controller;

import com.dw.forest.model.Checklist;
import com.dw.forest.service.ChecklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ChecklistController {
    @Autowired
    ChecklistService checklistService;

    @GetMapping("/checklist/all")
    public List<Checklist> getAllChecklists() {
        return checklistService.getAllChecklists();
    }

}
