package com.dw.forest.controller;

import com.dw.forest.dto.TravelerDTO;
import com.dw.forest.exception.UnauthorizedTravelerException;
import com.dw.forest.model.Traveler;
import com.dw.forest.service.TravelerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TravelerController {
    @Autowired
    TravelerService travelerService;

    @PostMapping("/register")
    public ResponseEntity<TravelerDTO> registerTraveler(@RequestBody TravelerDTO travelerDTO) {
        return new ResponseEntity<>(
                travelerService.registerTraveler(travelerDTO),
                HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody TravelerDTO travelerDTO, HttpServletRequest request) {
        String travelerName = travelerDTO.getTravelerName();
        String password = travelerDTO.getPassword();
        if (travelerService.validateUser(travelerName, password)) {
            HttpSession session = request.getSession();
            session.setAttribute("travelerName", travelerName);
            return new ResponseEntity<>("Login successful", HttpStatus.OK);
        } else {
            throw new UnauthorizedTravelerException("Authentication Failed");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate(); // 세션 종료
        return new ResponseEntity<>("You have been logged out.", HttpStatus.OK);
    }

    @GetMapping("/travelers/all")
    public ResponseEntity<List<Traveler>> getAllTraveler() {
        return new ResponseEntity<>(travelerService.getAllTravelers(), HttpStatus.OK);
    }

    @GetMapping("/current-traveler")
    public ResponseEntity<TravelerDTO> getCurrentTraveler(HttpServletRequest request) {
        Traveler traveler = travelerService.getCurrentTraveler(request);
        return new ResponseEntity<>(traveler.toDTO(), HttpStatus.OK);
    }
}
