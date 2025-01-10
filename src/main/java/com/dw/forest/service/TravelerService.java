package com.dw.forest.service;

import com.dw.forest.dto.TravelerDTO;
import com.dw.forest.exception.ResourceNotFoundException;
import com.dw.forest.model.Traveler;
import com.dw.forest.repository.AuthorityRepository;
import com.dw.forest.repository.TravelerRepository;
import com.sun.jdi.request.InvalidRequestStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TravelerService {
    @Autowired
    TravelerRepository travelerRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    public List<Traveler> getAllTravelers() {
        return travelerRepository.findAll();
    }

    public Traveler getTraveler(String name) {
        return travelerRepository.findById(name).orElseThrow();
    }

//    public TravelerDTO register(TravelerDTO travelerDTO) {
//        Optional<Traveler> traveler = travelerRepository.findById(travelerDTO.getTravelerName());
//        if (traveler.isPresent()) {
//            throw new InvalidRequestStateException("Traveler name already exists");
//        }
//        return travelerRepository.save(
//                new Traveler(
//                        travelerDTO.getTravelerName(),
//                        travelerDTO.getRealName(),
//                        travelerDTO.getPassword(),
//                        travelerDTO.getEmail(),
//                        travelerDTO.getContact(),
//                        authorityRepository.findById("USER").orElseThrow(()->new ResourceNotFoundException("No role")),
//                        LocalDateTime.now()
//                ).toDTO()
//        );
//    }
}
