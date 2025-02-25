package com.dw.forest.service;

import com.dw.forest.dto.FindID_PWD;
import com.dw.forest.dto.TravelerDTO;
import com.dw.forest.dto.TravelerResponseDTO;
import com.dw.forest.exception.InvalidRequestException;
import com.dw.forest.exception.ResourceNotFoundException;
import com.dw.forest.exception.UnauthorizedTravelerException;
import com.dw.forest.model.Point;
import com.dw.forest.model.Traveler;
import com.dw.forest.repository.*;
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
    ChecklistRepository checklistRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    PointRepository pointRepository;
    @Autowired
    CompletionRepository completionRepository;
    @Autowired
    QRepository qRepository;

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

    public TravelerDTO registerTraveler(TravelerResponseDTO responseDTO) {
        if (travelerRepository.existsById(responseDTO.getTravelerName())) {
            throw new InvalidRequestException("이미 존재하는 계정명입니다.");
        }

        Traveler newTraveler = new Traveler(responseDTO.getTravelerName(), authorityRepository.findById("USER")
                .orElseThrow(() ->new UnauthorizedTravelerException("올바른 역할이 아닙니다.")), responseDTO.getEmail(),
                responseDTO.getContact(), passwordEncoder.encode(responseDTO.getPassword()), responseDTO.getRealName(),
                LocalDate.now(), null, null, null, null, null, null, null);

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

    public String findId(FindID_PWD findIDPwd) {
        Traveler traveler = travelerRepository.findByRealNameAndContact(findIDPwd.getRealName(), findIDPwd.getContact()).orElseThrow(()->new ResourceNotFoundException("해당 이름과 전화번호의 여행자를 찾지 못했습니다."));
        return traveler.getTravelerName();
    }

    public String findPwd(FindID_PWD findIDPwd) {
        travelerRepository.findById(findIDPwd.getTravelerName()).orElseThrow(()->new ResourceNotFoundException("해당 이름과 전화번호의 여행자를 찾지 못했습니다."));
        return null;
    }

    public TravelerResponseDTO getCurrentTraveler(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 세션이 없으면 예외처리
        if (session == null) {
            throw new InvalidRequestException("세션이 없습니다.");
        }
        String travelerName = (String) session.getAttribute("travelerName");
        Traveler traveler = travelerRepository.findById(travelerName).orElseThrow(()->new ResourceNotFoundException("계정명이 잘못되었습니다."));

        return traveler.toResponse();
    }

    public TravelerResponseDTO updateTraveler(HttpServletRequest request,TravelerResponseDTO travelerResponseDTO) {
        HttpSession session = request.getSession(false); // 세션이 없으면 예외처리
        if (session == null) {
            throw new InvalidRequestException("세션이 없습니다.");
        }
        String travelerName = (String) session.getAttribute("travelerName");
        Traveler traveler = travelerRepository.findById(travelerName)
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

        return traveler.toResponse();
    }

    public String changePassword(HttpServletRequest request, String oldPassword, String newPassword, String newPassCheck) {
        HttpSession session = request.getSession(false); // 세션이 없으면 예외처리
        if (session == null) {
            throw new InvalidRequestException("세션이 없습니다.");
        }
        String travelerName = (String) session.getAttribute("travelerName");

        Traveler traveler = travelerRepository.findById(travelerName).orElseThrow(
                ()->new ResourceNotFoundException("올바른 계정명 및 비밀번호를 입력해주세요."));

        // 기존 비밀번호 검증
        if (traveler.getPassword().equals(oldPassword)) {
            throw new InvalidRequestException("올바른 계정명 및 비밀번호를 입력해주세요.");
        }

        // 새 비밀번호로 설정
        if (!newPassword.equals(newPassCheck)) {
            throw new InvalidRequestException("새 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }
        traveler.setPassword(passwordEncoder.encode(newPassword));
        travelerRepository.save(traveler);
        return "비밀번호";
    }

    public String deleteTraveler(HttpServletRequest request, String traveler_name) {
        HttpSession session = request.getSession(false); // 세션이 없으면 예외처리
        if (session == null) {
            throw new InvalidRequestException("세션이 없습니다.");
        }
        String admin = (String) session.getAttribute("travelerName");
        Traveler traveler1 = travelerRepository.findById(admin).orElseThrow(()->new ResourceNotFoundException("계정명이 잘못되었습니다."));
        if (!traveler1.getTravelerName().equals("admin")) {
            throw new UnauthorizedTravelerException("권한이 없습니다.");
        }

        Traveler traveler = travelerRepository.findById(traveler_name)
                .orElseThrow(()->new ResourceNotFoundException("해당 여행자를 찾을 수 없습니다."));

        checklistRepository.deleteAll(checklistRepository.findByTraveler_TravelerName(traveler_name));
        pointRepository.deleteAll(pointRepository.findByTravelerTravelerName(traveler_name));
        cartRepository.deleteAll(cartRepository.findByTraveler_TravelerName(traveler_name));
        qRepository.deleteAll(qRepository.findByTraveler_TravelerName(traveler_name));
        completionRepository.deleteAll(completionRepository.findByTravelerTravelerName(traveler_name));

        travelerRepository.delete(traveler);

        return "성공하였습니다.";
    }
}
