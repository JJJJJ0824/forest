package com.dw.forest.service;

import com.dw.forest.dto.TravelerDTO;
import com.dw.forest.exception.InvalidRequestException;
import com.dw.forest.exception.ResourceNotFoundException;
import com.dw.forest.exception.UnauthorizedTravelerException;
import com.dw.forest.model.Point;
import com.dw.forest.model.Traveler;
import com.dw.forest.repository.AuthorityRepository;
import com.dw.forest.repository.PointRepository;
import com.dw.forest.repository.TravelerRepository;
import com.sun.jdi.request.InvalidRequestStateException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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

    public List<Traveler> getAllTravelers() {
        return travelerRepository.findAll();
    }

    public TravelerDTO registerTraveler(TravelerDTO travelerDTO) {
        if (travelerRepository.existsById(travelerDTO.getTravelerName())) {
            throw new InvalidRequestStateException("Traveler name already exists");
        }

        Traveler newTraveler = new Traveler(travelerDTO.getTravelerName(), authorityRepository.findById("USER")
                .orElseThrow(()->new ResourceNotFoundException("No Role")), travelerDTO.getEmail(),
                travelerDTO.getContact(), passwordEncoder.encode(travelerDTO.getPassword()), travelerDTO.getRealName(),
                LocalDate.now(), null, null, null, null, null);

        travelerRepository.save(newTraveler);

        Point welcomePoint = new Point(null, newTraveler, "Welcome Bonus", 100, LocalDate.now(), null);

        pointRepository.save(welcomePoint);
        
        return newTraveler.toDTO();
    }

    public boolean validateUser(String travelerName, String password) {
        Traveler traveler = travelerRepository.findById(travelerName).orElseThrow(()->new InvalidRequestException("Invalid TravelerName"));
        return passwordEncoder.matches(password, traveler.getPassword());
    }

    public Traveler getCurrentTraveler(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 세션이 없으면 예외처리
        if (session == null) {
            throw new UnauthorizedTravelerException("No Session exist");
        }
        String travelerName = (String) session.getAttribute("travelerName");
        return travelerRepository.findById(travelerName).orElseThrow(()->new InvalidRequestException("No TravelerName"));
    }
}
