package com.dw.forest.service;

import com.dw.forest.dto.QaDTO;
import com.dw.forest.exception.InvalidRequestException;
import com.dw.forest.exception.ResourceNotFoundException;
import com.dw.forest.model.QA;
import com.dw.forest.repository.QARepository;
import com.dw.forest.repository.TravelerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class QAService {
    @Autowired
    QARepository qaRepository;

    @Autowired
    TravelerRepository travelerRepository;

    public List<QaDTO> getAllForumPosts() {
        List<QaDTO> q = qaRepository.findAll().stream().map(QA::toDTO).toList();
        if (q.isEmpty()) {
            throw new ResourceNotFoundException("Q&A가 없습니다.");
        }
        return q;
    }

    public QaDTO saveQuestion(QaDTO QaDTO) {
        QA qa = new QA(QaDTO.getId(), travelerRepository.findById(QaDTO.getTraveler_name()).
                orElseThrow(()->new ResourceNotFoundException("계정명이 잘못되었습니다.")),
                QaDTO.getTitle(), QaDTO.getContent(), LocalDate.now(), "q");

        if (qa==null) {
            throw new InvalidRequestException("");
        }

        qaRepository.save(qa);

        return qa.toDTO();
    }

    public QaDTO saveAnswer(QaDTO QaDTO) {
        QA qa = new QA(QaDTO.getId(), travelerRepository.findById(QaDTO.getTraveler_name()).
                orElseThrow(()->new ResourceNotFoundException("계정명이 잘못되었습니다.")),
                QaDTO.getTitle(), QaDTO.getContent(), LocalDate.now(), "a");

        if (qa==null) {
            throw new InvalidRequestException("");
        }

        qaRepository.save(qa);

        return qa.toDTO();
    }
}
