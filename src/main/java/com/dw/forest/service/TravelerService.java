package com.dw.forest.service;

import com.dw.forest.dto.TravelerDTO;
import com.dw.forest.dto.TravelerResponseDTO;
import com.dw.forest.exception.InvalidRequestException;
import com.dw.forest.exception.ResourceNotFoundException;
import com.dw.forest.exception.UnauthorizedTravelerException;
import com.dw.forest.model.Point;
import com.dw.forest.model.Traveler;
import com.dw.forest.repository.AuthorityRepository;
import com.dw.forest.repository.ChecklistRepository;
import com.dw.forest.repository.PointRepository;
import com.dw.forest.repository.TravelerRepository;
import com.sun.jdi.request.InvalidRequestStateException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
    ChecklistRepository checklistRepository;



    @Autowired
    PointRepository pointRepository;

    public List<TravelerDTO> getAllTravelers(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 세션이 없으면 예외처리
        if (session == null) {
            throw new InvalidRequestException("세션이 없습니다.");
        }
        String travelerName = (String) session.getAttribute("travelerName");
        if (!travelerName.matches("admin")) {
            throw new UnauthorizedTravelerException("권한이 없습니다.");
        }
        List<Traveler> travelers = travelerRepository.findAll();
        if (travelers.isEmpty()) {
            throw new ResourceNotFoundException("여행자가 없습니다.");
        }
        return travelers.stream().map(Traveler::toDTO).toList();
    }

    public TravelerDTO registerTraveler(TravelerDTO travelerDTO) {
        if (travelerRepository.existsById(travelerDTO.getTravelerName())) {
            throw new InvalidRequestStateException("이미 존재하는 계정명입니다.");
        }

        Traveler newTraveler = new Traveler(travelerDTO.getTravelerName(), authorityRepository.findById("USER")
                .orElseThrow(()->new UnauthorizedTravelerException("올바른 역할이 아닙니다.")), travelerDTO.getEmail(),
                travelerDTO.getContact(), passwordEncoder.encode(travelerDTO.getPassword()), travelerDTO.getRealName(),
                LocalDate.now(), null, null, null, null, null);

        travelerRepository.save(newTraveler);

        Point welcomePoint = new Point(null, newTraveler, "Welcome Bonus", 100, LocalDate.now(), null);

        pointRepository.save(welcomePoint);

        return newTraveler.toDTO();
    }

    public boolean validateUser(String travelerName, String password) {
        try {
            if (travelerName.isEmpty() || password.isEmpty()) {
                throw new InvalidRequestException("올바른 계정명 및 비밀번호를 입력해주세요.");
            }
            if (!travelerRepository.existsById(travelerName)) {
                throw new InvalidRequestException("올바른 계정명 및 비밀번호를 입력해주세요.");
            }
            Traveler traveler = travelerRepository.findById(travelerName).orElseThrow(() -> new ResourceNotFoundException("올바른 계정명 및 비밀번호를 입력해주세요."));
            return passwordEncoder.matches(password, traveler.getPassword());
        }catch (NullPointerException e) {
            throw new InvalidRequestException("올바른 계정명 및 비밀번호를 입력해주세요.");
        }
    }

    public TravelerDTO getCurrentTraveler(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 세션이 없으면 예외처리
        if (session == null) {
            throw new InvalidRequestException("세션이 없습니다.");
        }
        String travelerName = (String) session.getAttribute("travelerName");
        Traveler traveler = travelerRepository.findById(travelerName).orElseThrow(()->new ResourceNotFoundException("계정명이 잘못되었습니다."));

        return traveler.toDTO();
    }

    public TravelerDTO updateTraveler(TravelerResponseDTO travelerResponseDTO) {
        Traveler traveler = travelerRepository.findById(travelerResponseDTO.getTravelerName())
                .orElseThrow(()->new InvalidRequestException("유저 정보 업데이트에 실패하였습니다."));

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

        travelerRepository.save(traveler);

        return traveler.toDTO();
    }

    public String changePassword(String traveler_name, String oldPassword, String newPassword) {
        Traveler traveler = travelerRepository.findById(traveler_name).orElseThrow();

        if (traveler == null) {
            throw new ResourceNotFoundException("계정명이 잘못되었습니다.");
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
       travelerRepository.findById(traveler_name).orElseThrow(()->new ResourceNotFoundException("계정명이 잘못되었습니다."));
       travelerRepository.deleteById(traveler_name);
       return "성공하였습니다.";
    }
}
