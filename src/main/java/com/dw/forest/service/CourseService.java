package com.dw.forest.service;

import com.dw.forest.dto.CourseDTO;
import com.dw.forest.exception.ResourceNotFoundException;
import com.dw.forest.model.Course;
import com.dw.forest.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {
    @Autowired
    CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course createCourse(CourseDTO courseDTO){

        Course course = new Course();
        course.setTitle(courseDTO.getTitle());
        course.setDescription(courseDTO.getDescription());
        course.setContent(courseDTO.getContent());
        course.setType(courseDTO.getType());
        course.setPrice(courseDTO.getPrice());
        course.setCreatedAt(courseDTO.getCreatedAt());
        course.setUpdatedAt(LocalDate.now());

        return courseRepository.save(course);
    }

    public CourseDTO getCourseById(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 아이디를 찾을수 없습니다."));

        return new CourseDTO(course);
    }

    public CourseDTO updateCourse(Long courseId, CourseDTO courseDTO){
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 아이디를 찾을수 없습니다."));

        course.setTitle(courseDTO.getTitle());
        course.setDescription(courseDTO.getDescription());
        course.setContent(courseDTO.getContent());
        course.setType(courseDTO.getType());
        course.setPrice(courseDTO.getPrice());

        courseRepository.save(course);

        return new CourseDTO(course);
    }

    public ResponseEntity<String> deleteCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 아이디를 찾을수 없습니다."));

        courseRepository.delete(course);
        return new ResponseEntity<>("강의 삭제 성공 !!" , HttpStatus.OK);
    }

    public List<CourseDTO> getCoursesByCategory(String categoryName) {
        List<Course> courses = courseRepository.findByCategoryCategoryName(categoryName);

        return courses.stream()
                .map(course -> new CourseDTO(course))
                .collect(Collectors.toList());
    }

    public List<CourseDTO> getCoursesByPriceRange(long minPrice , long maxPrice) {
        List<Course> courses = courseRepository.findByPriceBetween(minPrice, maxPrice);
        return courses.stream()
                .map(CourseDTO::new)
                .collect(Collectors.toList());
    }

    public List<CourseDTO> getFreeCourses() {
        List<Course> freeCourses = courseRepository.findByType("자유");
        return freeCourses.stream()
                .map(CourseDTO::new)
                .collect(Collectors.toList());
    }

    public List<CourseDTO> getFamilyCourses() {
        List<Course> familyCourses = courseRepository.findByType("가족");
        return familyCourses.stream()
                .map(CourseDTO::new)
                .toList();
    }

    public List<CourseDTO> getPackageCourses() {
        List<Course> packageCourses = courseRepository.findByType("패키지");
        return packageCourses.stream()
                .map(CourseDTO::new)
                .toList();
    }

    public List<CourseDTO> getCommonCourses() {
        List<Course> commonCourses = courseRepository.findByType("공통");
        return commonCourses.stream()
                .map(CourseDTO::new)
                .toList();
    }

}
