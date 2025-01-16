package com.dw.forest.service;

import com.dw.forest.model.Checklist;
import com.dw.forest.repository.ChecklistRepository;
import com.dw.forest.repository.TravelerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChecklistService {
    @Autowired
    ChecklistRepository checklistRepository;

    @Autowired
    TravelerRepository travelerRepository;

//    public List<Checklist> getAllChecklists(String traveler_name) {
//        return travelerRepository.findById(traveler_name).orElseThrow().setChecklists(checklistRepository.findAll());
//    }
//
//    public












}
