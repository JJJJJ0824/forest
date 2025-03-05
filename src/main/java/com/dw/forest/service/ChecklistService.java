package com.dw.forest.service;

import com.dw.forest.dto.CheckListDTO;
import com.dw.forest.dto.CourseReadDTO;
import com.dw.forest.exception.InvalidRequestException;
import com.dw.forest.exception.ResourceNotFoundException;
import com.dw.forest.model.Category;
import com.dw.forest.model.Checklist;
import com.dw.forest.model.Course;
import com.dw.forest.model.Traveler;
import com.dw.forest.repository.CategoryRepository;
import com.dw.forest.repository.ChecklistRepository;
import com.dw.forest.repository.CourseRepository;
import com.dw.forest.repository.TravelerRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChecklistService {
    @Autowired
    ChecklistRepository checklistRepository;

    @Autowired
    TravelerRepository travelerRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    CategoryRepository categoryRepository;

    public List<CheckListDTO> getIncompleteChecklists(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 세션이 없으면 예외처리
        if (session == null) {
            throw new InvalidRequestException("세션이 없습니다.");
        }
        String travelerName = (String) session.getAttribute("travelerName");
        List<Checklist> incompleteChecklists = checklistRepository.findByTraveler_TravelerNameAndIsCheckedFalse(travelerName);

        if (incompleteChecklists.isEmpty()) {
            throw new ResourceNotFoundException("완료하지 않은 체크리스트 항목이 없습니다.");
        }

        return incompleteChecklists.stream()
                .map(Checklist::toDTO)
                .toList();
    }

    public CheckListDTO submitMyChecklist(CheckListDTO checkListDTO, HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 세션이 없으면 예외처리
        if (session == null) {
            throw new InvalidRequestException("세션이 없습니다.");
        }
        String travelerName = (String) session.getAttribute("travelerName");

        Traveler traveler = travelerRepository.findById(travelerName).orElseThrow(()->new ResourceNotFoundException("계정명이 잘못되었습니다."));

        Checklist checklist = new Checklist();
        checklist.setTraveler(traveler);
        checklist.setDirection(checkListDTO.getDirection());
        checklist.setResponse(checkListDTO.getResponse());
        checklist.setChecked(true);
        checklist.setCategory(categoryRepository.findById(checkListDTO.getCategory()).orElseThrow(()->new ResourceNotFoundException("잘못된 카테고리입니다.")));

        checklistRepository.save(checklist);

        return checkListDTO;
    }

    public CheckListDTO updateMyChecklist(CheckListDTO checkListDTO, HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 세션이 없으면 예외처리
        if (session == null) {
            throw new InvalidRequestException("세션이 없습니다.");
        }
        String travelerName = (String) session.getAttribute("travelerName");

        Traveler traveler = travelerRepository.findById(travelerName)
                .orElseThrow(() -> new ResourceNotFoundException("계정명이 잘못되었습니다."));

        Checklist checklist = checklistRepository.findByTraveler_TravelerNameAndDirection(travelerName, checkListDTO.getDirection())
                .orElseThrow(() -> new ResourceNotFoundException("해당 체크리스트 항목을 찾을 수 없습니다."));

        if (!checklist.getTraveler().getTravelerName().equals(travelerName)) {
            throw new InvalidRequestException("해당 체크리스트 항목을 수정할 권한이 없습니다.");
        }

        checklist.setResponse(checkListDTO.getResponse());  // 답변 업데이트
        checklist.setChecked(true);  // 체크된 항목으로 설정

        if (checkListDTO.getCategory() != null) {
            Category category = categoryRepository.findById(checkListDTO.getCategory())
                    .orElseThrow(() -> new ResourceNotFoundException("잘못된 카테고리입니다."));
            checklist.setCategory(category);
        }

        checklistRepository.save(checklist);

        return checkListDTO;
    }


    public List<CheckListDTO> getChecklistByTraveler(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 세션이 없으면 예외처리
        if (session == null) {
            throw new InvalidRequestException("세션이 없습니다.");
        }
        String travelerName = (String) session.getAttribute("travelerName");
        Traveler traveler = travelerRepository.findById(travelerName)
                .orElseThrow(() -> new ResourceNotFoundException("잘못된 계정명입니다."));

        List<Checklist> checklists = checklistRepository.findByTraveler_TravelerName(travelerName);

        return checklists.stream().map(Checklist::toDTO).toList();
    }

    public List<CourseReadDTO> recommendCourses(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 세션이 없으면 예외처리
        if (session == null) {
            throw new InvalidRequestException("세션이 없습니다.");
        }
        String travelerName = (String) session.getAttribute("travelerName");

        Traveler traveler = travelerRepository.findById(travelerName).orElseThrow(()->new ResourceNotFoundException("사용자를 찾을 수 없습니다."));

        List<Checklist> completedChecklists = checklistRepository.findByTravelerAndIsCheckedTrue(traveler);
        if (completedChecklists.isEmpty()) {
            throw new ResourceNotFoundException("체크리스트를 완료해야 강의를 추천할 수 있습니다.");
        }

        // 유형 찾기
        String travelerCategory = completedChecklists.stream()
                .map(Checklist::getCategory)
                .map(Category::getCategoryName)
                .findFirst()
                .orElse(null);

        if (travelerCategory.isEmpty()) {
            throw new ResourceNotFoundException("유형을 찾을 수 없습니다.");
        }

        List<Course> recommendedCourses = courseRepository.findByCategoryCategoryName(travelerCategory);
        if (recommendedCourses.isEmpty()) {
            throw new RuntimeException("강의를 추천할 수 없습니다.");
        }

        return recommendedCourses.stream().map(Course::toRead).toList();
    }

    public boolean checklistCompleted(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 세션이 없으면 예외처리
        if (session == null) {
            throw new InvalidRequestException("세션이 없습니다.");
        }
        String travelerName = (String) session.getAttribute("travelerName");
        Traveler traveler = travelerRepository.findById(travelerName)
                .orElseThrow(() -> new ResourceNotFoundException("해당 유저를 찾을수 없습니다."));

        List<Checklist> checklists = traveler.getChecklists();

        for (Checklist checklist : checklists) {
            if (checklist.isChecked()) {
                return true;
            }
        }
        return false;
    }

    public String deleteChecklist(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 세션이 없으면 예외처리
        if (session == null) {
            throw new InvalidRequestException("세션이 없습니다.");
        }
        String travelerName = (String) session.getAttribute("travelerName");
        checklistRepository.deleteAll(checklistRepository.findByTraveler_TravelerName(travelerName));
        return "성공하였습니다.";
    }

    public String saveFeedback(HttpServletRequest request, Long checklistId, String feedbackText) {
        HttpSession session = request.getSession(false); // 세션이 없으면 예외처리
        if (session == null) {
            throw new InvalidRequestException("세션이 없습니다.");
        }
        String travelerName = (String) session.getAttribute("travelerName");
        travelerRepository.findByTravelerName(travelerName)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));

        Checklist checklist = checklistRepository.findById(checklistId)
                .orElseThrow(() -> new ResourceNotFoundException("체크리스트를 찾을 수 없습니다."));

        if (!checklist.isCompleted()) {
            throw new InvalidRequestException("완료되지 않은 체크리스트에 피드백을 남길 수 없습니다.");
        }

        String feedbackMessage = "Traveler " + travelerName + " 체크리스트에 관한 피드백을 남겼습니다 " + checklistId + ": " + feedbackText;

        return "피드백이 저장되었습니다: " + feedbackMessage;
    }
}
