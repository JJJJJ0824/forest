package com.dw.forest.controller;

import com.dw.forest.model.Checklist;
import com.dw.forest.service.ChecklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/checklist")
public class ChecklistController {
    @Autowired
    ChecklistService checklistService;

//    @GetMapping("/{traveler_name}/all")
//    public List<Checklist> getAllChecklists(@PathVariable String traveler_name) {
//        return checklistService.getAllChecklists(traveler_name);
//    }


}
