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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TravelerService {
    @Autowired
    TravelerRepository travelerRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    PointRepository pointRepository;

    public TravelerDTO registerTraveler(TravelerDTO travelerDTO) {
        if (travelerRepository.existsById(travelerDTO.getTravelerName())) {
            throw new InvalidRequestStateException("Traveler name already exists");
        }

        Traveler newTraveler = new Traveler(travelerDTO.getTravelerName(), authorityRepository.findById("USER")
                .orElseThrow(()->new ResourceNotFoundException("No Role")), travelerDTO.getEmail(),
                travelerDTO.getContact(), passwordEncoder.encode(travelerDTO.getPassword()), travelerDTO.getRealName(),
                LocalDate.now(), null, null, null, null, null);

        travelerRepository.save(newTraveler);

        Point welcomePoint = new Point(
                null,
                newTraveler,
                "Welcome Bonus",
                100,
                null
        );
        pointRepository.save(welcomePoint);

        return newTraveler.toDTO();
    }

    public List<Traveler> getAllTravelers() {
        return travelerRepository.findAll();
    }

    public Traveler getTraveler(String traveler_name) {
        return travelerRepository.findById(traveler_name).orElseThrow();
    }
}
