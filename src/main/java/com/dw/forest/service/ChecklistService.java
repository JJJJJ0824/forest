package com.dw.forest.service;

import com.dw.forest.dto.CheckListDTO;
import com.dw.forest.dto.CourseDTO;
import com.dw.forest.dto.CourseReadDTO;
import com.dw.forest.exception.InvalidRequestException;
import com.dw.forest.exception.ResourceNotFoundException;
import com.dw.forest.model.Checklist;
import com.dw.forest.model.Course;
import com.dw.forest.model.Traveler;
import com.dw.forest.repository.ChecklistRepository;
import com.dw.forest.repository.CourseRepository;
import com.dw.forest.repository.TravelerRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChecklistService {
    @Autowired
    ChecklistRepository checklistRepository;

    @Autowired
    TravelerRepository travelerRepository;

    @Autowired
    CourseRepository courseRepository;

    public List<CheckListDTO> getIncompleteChecklists(String travelerName) {
        List<Checklist> incompleteChecklists = checklistRepository.findByTraveler_TravelerNameAndIsCheckedFalse(travelerName);

        if (incompleteChecklists.isEmpty()) {
            throw new ResourceNotFoundException("완료하지 않은 체크리스트 항목이 없습니다.");
        }

        return incompleteChecklists.stream()
                .map(Checklist::toDTO)
                .toList();
    }

    public List<CheckListDTO> getChecklistsByTraveler(String travelerName) {

        Traveler traveler = travelerRepository.findById(travelerName)
                .orElseThrow(() -> new ResourceNotFoundException("해당 사용자가 존재하지 않습니다."));

        List<Checklist> checklists = checklistRepository.findByTraveler(traveler);

        return checklists.stream().map(Checklist::toDTO).toList();
    }

    public boolean isChecklistCompleted(String travelerName) {
        Traveler traveler = travelerRepository.findById(travelerName)
                .orElseThrow(() -> new ResourceNotFoundException("해당 유저를 찾을수 없습니다."));

        return traveler.getChecklists().stream().allMatch(Checklist::isCompleted);
    }

    public List<CourseReadDTO> recommendCourses(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 세션이 없으면 예외처리
        if (session == null) {
            throw new InvalidRequestException("세션이 없습니다.");
        }
        String travelerName = (String) session.getAttribute("travelerName");
        if (!isChecklistCompleted(travelerName)){
            throw new ResourceNotFoundException("모든 체크리스트를 완료해야 강의추천이 가능합니다.");
        }

        Traveler traveler = travelerRepository.findById(travelerName)
                .orElseThrow(() -> new ResourceNotFoundException("해당 유저를 찾을수 없습니다."));
        String coursePreference = traveler.getChecklists().stream()
                .map(Checklist::getDirection)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("사용자의 방향성을 찾을 수 없습니다."));

        return courseRepository.findByCategoryCategoryName(coursePreference).stream().map(Course::toRead).toList();
    }

    public boolean checklistCompleted(String travelerName) {

        Traveler traveler = travelerRepository.findById(travelerName)
                .orElseThrow(() -> new ResourceNotFoundException("해당 유저를 찾을수 없습니다."));

        List<Checklist> checklists = traveler.getChecklists();

        for (Checklist checklist : checklists) {
            if (checklist.isChecked()) {
                return true;
            }
        }
        return  false;
    }

    public List<CheckListDTO> resetChecklist(String travelerName) {

        Traveler traveler = travelerRepository.findByTravelerName(travelerName)
                .orElseThrow(() -> new ResourceNotFoundException("해당 유저를 찾을수 없습니다."));

        List<Checklist> checklists = checklistRepository.findByTraveler(traveler);

        List<CheckListDTO> updateChecklists = new ArrayList<>();
        for (Checklist checklist : checklists){
            checklist.setChecked(false);
            checklistRepository.save(checklist);
            updateChecklists.add(checklist.toDTO());
        }
        return updateChecklists;
    }
}
