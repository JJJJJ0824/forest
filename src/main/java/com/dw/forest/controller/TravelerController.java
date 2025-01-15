package com.dw.forest.controller;

import com.dw.forest.dto.TravelerDTO;
import com.dw.forest.dto.TravelerResponseDTO;
import com.dw.forest.exception.ResourceNotFoundException;
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
@RequestMapping("/api/traveler")
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
        if (request==null&&response==null) {
            throw new ResourceNotFoundException("log out failed.");
        }
        return new ResponseEntity<>("You have been logged out.", HttpStatus.OK);
    }


    @GetMapping("/all")
    public ResponseEntity<List<Traveler>> getAllTraveler() {
        return new ResponseEntity<>(travelerService.getAllTravelers(), HttpStatus.OK);
    }

    @GetMapping("/mypage")
    public ResponseEntity<TravelerDTO> getCurrentTraveler(HttpServletRequest request) {
        Traveler traveler = travelerService.getCurrentTraveler(request);
        return new ResponseEntity<>(traveler.toDTO(), HttpStatus.OK);
    }

    @PutMapping("/{traveler_name}/update")
    public ResponseEntity<Traveler> updateTraveler(@RequestBody TravelerResponseDTO travelerResponseDTO) {
        return new ResponseEntity<>(travelerService.updateTraveler(travelerResponseDTO),HttpStatus.OK);
    }

    @PutMapping("/{traveler_name}/password")
    public ResponseEntity<String> changePassword(@PathVariable String traveler_name, @RequestBody String old_password, @RequestBody String new_password) {
        return new ResponseEntity<>(travelerService.changePassword(traveler_name, old_password, new_password)+"가 정상적으로 변경되었습니다.", HttpStatus.OK);
    }

    @DeleteMapping("/{traveler_name}/delete")
    public ResponseEntity<String> deleteTraveler(@PathVariable String traveler_name) {
        return new ResponseEntity<>("여행자 삭제에 "+travelerService.deleteTraveler(traveler_name), HttpStatus.OK);
    }


}
