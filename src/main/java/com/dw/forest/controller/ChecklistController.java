package com.dw.forest.controller;

import com.dw.forest.service.ChecklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChecklistController {
    @Autowired
    ChecklistService checklistService;

}
