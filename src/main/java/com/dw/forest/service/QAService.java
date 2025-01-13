package com.dw.forest.service;

import com.dw.forest.model.QA;
import com.dw.forest.repository.QARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QAService {
    @Autowired
    QARepository QARepository;

    public List<QA> getAllForumPosts() {
        return QARepository.findAll();
    }

}
