package com.dw.forest.service;

import com.dw.forest.repository.ChecklistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChecklistService {
    @Autowired
    ChecklistRepository checklistRepository;
}
