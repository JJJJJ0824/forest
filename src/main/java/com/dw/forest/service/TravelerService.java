package com.dw.forest.service;

import com.dw.forest.dto.TravelerDTO;
import com.dw.forest.dto.TravelerResponseDTO;
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
import java.util.Optional;

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
        List<Traveler> travelers = travelerRepository.findAll();
        if (travelers.isEmpty()) {
            throw new ResourceNotFoundException("Cannot find any traveler");
        }
        return travelers;
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

    public Traveler updateTraveler(TravelerResponseDTO travelerResponseDTO) {
        Optional<Traveler> travelerOptional = travelerRepository.findById(travelerResponseDTO.getTravelerName());

        if (travelerOptional.isEmpty()) {
            throw new InvalidRequestException("유저 정보 업데이트에 실패하였습니다.");
        }

        Traveler traveler = travelerOptional.get();

        // 업데이트할 정보 설정
        if (travelerResponseDTO.getContact() != null) {
            traveler.setContact(travelerResponseDTO.getContact());
        }
        if (travelerResponseDTO.getEmail() != null) {
            traveler.setEmail(travelerResponseDTO.getEmail());
        }
        if (travelerResponseDTO.getRealName() != null) {
            traveler.setRealName(travelerResponseDTO.getRealName());
        }

        return travelerRepository.save(traveler);
    }

    public String changePassword(String traveler_name, String oldPassword, String newPassword) {
        Traveler traveler = travelerRepository.findById(traveler_name).orElseThrow();

        if (traveler==null) {
            throw new ResourceNotFoundException("유저 이름이 맞지 않습니다.");
        }

        // 기존 비밀번호 검증
        if (!traveler.getPassword().equals(oldPassword)) {
            throw new InvalidRequestException("비밀번호가 틀렸습니다.");
        }

        // 새 비밀번호로 설정
        traveler.setPassword(newPassword);
        travelerRepository.save(traveler);

        return "비밀번호";
    }

    public String deleteTraveler(String traveler_name) {
       travelerRepository.findById(traveler_name).orElseThrow(()->new ResourceNotFoundException("Cannot find Traveler"));
       travelerRepository.deleteById(traveler_name);
       return "성공하였습니다.";
    }
}
