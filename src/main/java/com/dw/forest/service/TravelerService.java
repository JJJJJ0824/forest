package com.dw.forest.service;

import com.dw.forest.dto.TravelerDTO;
import com.dw.forest.exception.ResourceNotFoundException;
import com.dw.forest.model.Authority;
import com.dw.forest.model.Point;
import com.dw.forest.model.Traveler;
import com.dw.forest.repository.AuthorityRepository;
import com.dw.forest.repository.PointRepository;
import com.dw.forest.repository.TravelerRepository;
import com.sun.jdi.request.InvalidRequestStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TravelerService {
    @Autowired
    TravelerRepository travelerRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    PointRepository pointRepository;

    public TravelerDTO registerTraveler(TravelerDTO travelerDTO) {
        if (travelerRepository.existsById(travelerDTO.getTravelerName())) {
            throw new InvalidRequestStateException("Traveler name already exists");
        }

        Authority authority = authorityRepository.findById("USER")
                .orElseThrow(() -> new ResourceNotFoundException("No role found"));

        Traveler traveler = Traveler.fromDTO(travelerDTO, authority);
        traveler.setRegistrationDate(LocalDate.now());
        Traveler savedTraveler = travelerRepository.save(traveler);

        Point welcomePoint = new Point(
                null,
                savedTraveler,
                "Welcome Bonus",
                100L
        );
        pointRepository.save(welcomePoint);

        return savedTraveler.toDTO();
    }

    public List<Traveler> getAllTravelers() {
        return travelerRepository.findAll();
    }

    public Traveler getTraveler(String traveler_name) {
        return travelerRepository.findById(traveler_name).orElseThrow();
    }

}
