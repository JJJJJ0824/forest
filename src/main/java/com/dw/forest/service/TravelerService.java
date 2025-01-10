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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TravelerService {
    @Autowired
    TravelerRepository travelerRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    public TravelerDTO registerTraveler(TravelerDTO travelerDTO) {
        Optional<Traveler> traveler = travelerRepository.findById(travelerDTO.getTravelerName());
        if (traveler.isPresent()) {
            throw new InvalidRequestStateException("Traveler name already exists");
        }
        Traveler newTraveler = new Traveler(
                travelerDTO.getTravelerName(),
                authorityRepository.findById("USER")
                        .orElseThrow(() -> new ResourceNotFoundException("No role")),
                travelerDTO.getEmail(),
                travelerDTO.getContact(),
                travelerDTO.getPassword(),
                travelerDTO.getRealName(),
                LocalDate.now(),
                travelerDTO.getPoint(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );
        Traveler savedTraveler = travelerRepository.save(newTraveler);
        return savedTraveler.toDTO();
    }

    public List<Traveler> getAllTravelers() {
        return travelerRepository.findAll();
    }

    public Traveler getTraveler(String traveler_name) {
        return travelerRepository.findById(traveler_name).orElseThrow();
    }
}
