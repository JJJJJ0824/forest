package com.dw.forest.service;

import com.dw.forest.model.Checklist;
import com.dw.forest.repository.ChecklistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChecklistService {
    @Autowired
    ChecklistRepository checklistRepository;

    public List<Checklist> getAllChecklists() {
        return checklistRepository.findAll();
    }
}
