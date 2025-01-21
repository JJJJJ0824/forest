package com.dw.forest.service;

import com.dw.forest.model.Completion;
import com.dw.forest.repository.CompletionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompletionService {
    @Autowired
    CompletionRepository completionRepository;

    public List<Completion> getAllCompletions() {
        return completionRepository.findAll();
    }
}
