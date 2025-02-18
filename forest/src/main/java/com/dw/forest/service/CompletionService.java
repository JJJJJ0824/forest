package com.dw.forest.service;

import com.dw.forest.dto.CourseReadDTO;
import com.dw.forest.dto.CourseWithStudentsDTO;
import com.dw.forest.exception.InvalidRequestException;
import com.dw.forest.exception.ResourceNotFoundException;
import com.dw.forest.exception.UnauthorizedTravelerException;
import com.dw.forest.model.Completion;
import com.dw.forest.model.Course;
import com.dw.forest.model.Traveler;
import com.dw.forest.repository.CompletionRepository;
import com.dw.forest.repository.CourseRepository;
import com.dw.forest.repository.TravelerRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CompletionService {
    @Autowired
    CompletionRepository completionRepository;

    @Autowired
    TravelerRepository travelerRepository;

    @Autowired
    CourseRepository courseRepository;

    public boolean completedCourse(HttpServletRequest request, Long courseId) {
        HttpSession session = request.getSession(false); // 세션이 없으면 예외처리
        if (session == null) {
            throw new InvalidRequestException("세션이 없습니다.");
        }
        String travelerName = (String) session.getAttribute("travelerName");

        Traveler traveler = travelerRepository.findByTravelerName(travelerName)
                .orElseThrow(() -> new ResourceNotFoundException("해당 유저를 찾을 수 없습니다."));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 강의를 찾을 수 없습니다."));

        Optional<Completion> completion = completionRepository.findByTravelerAndCourse(traveler, course);

        return completion.isPresent();
    }

    public List<CourseReadDTO> getCompletedCoursesByTraveler(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 세션이 없으면 예외처리
        if (session == null) {
            throw new InvalidRequestException("세션이 없습니다.");
        }
        String travelerName = (String) session.getAttribute("travelerName");
        List<Completion> completions = completionRepository.findByTravelerTravelerName(travelerName);

        if (completions.isEmpty()) {
            throw new ResourceNotFoundException("해당 유저를 찾을수 없습니다");
        }

        return completions.stream().map(Completion::toRead).toList();
    }

    public String completeCourse(HttpServletRequest request, Long courseId) {
        HttpSession session = request.getSession(false); // 세션이 없으면 예외처리
        if (session == null) {
            throw new InvalidRequestException("세션이 없습니다.");
        }
        String travelerName = (String) session.getAttribute("travelerName");

        Traveler traveler = travelerRepository.findByTravelerName(travelerName)
                .orElseThrow(() -> new ResourceNotFoundException("해당 유저를 찾을 수 없습니다."));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 강의를 찾을 수 없습니다."));

        Optional<Completion> existingCompletion = completionRepository.findByTravelerAndCourse(traveler, course);
        if (existingCompletion.isEmpty()) {
            throw new InvalidRequestException("해당 강의를 수강한 유저를 찾을 수 없습니다.");
        }

        Completion completion = completionRepository.findByTravelerAndCourse(traveler, course)
                .orElseThrow(()->new ResourceNotFoundException("해당 강의가 없습니다."));
        completion.setTraveler(traveler);
        completion.setCourse(course);
        completion.setCompletionDate(LocalDate.now());  // 수강 완료일로 현재 날짜 설정
        completionRepository.save(completion);

        return "강의 '" + course.getTitle() + "'의 수강이 완료되었습니다.";
    }

    public CourseWithStudentsDTO getCourseWithStudents(HttpServletRequest request, Long courseId) {
        HttpSession session = request.getSession(false); // 세션이 없으면 예외처리
        if (session == null) {
            throw new InvalidRequestException("세션이 없습니다.");
        }
        String travelerName = (String) session.getAttribute("travelerName");

        if (!travelerName.matches("admin")) {
            throw new UnauthorizedTravelerException("권한이 없습니다.");
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 강의를 찾을 수 없습니다."));

        // 해당 강의를 수강한 학생들 조회
        List<Completion> completions = completionRepository.findByCourseCourseId(courseId);

        if (completions.isEmpty()) {
            throw new ResourceNotFoundException("해당 강의를 수강한 사용자가 없습니다.");
        }

        List<CourseWithStudentsDTO.TravelerDTO> travelerDTOs = completions.stream()
                .map(completion -> new CourseWithStudentsDTO.TravelerDTO(
                        completion.getTraveler().getTravelerName(),
                        completion.getTraveler().getContact(),
                        completion.getTraveler().getEmail(),
                        completion.getTraveler().getRealName(),
                        completion.getCompletionDate())) // 완료일
                .toList();

        CourseReadDTO courseReadDTO = course.toRead();

        return new CourseWithStudentsDTO(courseReadDTO, travelerDTOs);
    }
}
