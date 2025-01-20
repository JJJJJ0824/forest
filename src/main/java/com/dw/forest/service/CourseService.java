package com.dw.forest.service;

import com.dw.forest.dto.CourseDTO;
import com.dw.forest.exception.InvalidRequestException;
import com.dw.forest.exception.ResourceNotFoundException;
import com.dw.forest.model.Category;
import com.dw.forest.model.Course;
import com.dw.forest.repository.CategoryRepository;
import com.dw.forest.repository.CourseRepository;
import org.hibernate.PropertyValueException;
import org.springframework.beans.PropertyAccessException;
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

    @Autowired
    CategoryRepository categoryRepository;

    public List<CourseDTO> getAllCourses() {
        List<Course> courses = courseRepository.findAll().stream().toList();
        if (courses.isEmpty()) {
            throw new ResourceNotFoundException("강의 정보를 찾을 수 없습니다.");
        }
        return courses.stream().map(Course::toDTO).toList();
    }

    public CourseDTO createCourse(CourseDTO courseDTO){
        try {
            Course course = new Course();
            Category category = categoryRepository.findById(courseDTO.getCategory())
                    .orElseThrow(() -> new ResourceNotFoundException("카테고리를 찾을 수 없습니다."));
            course.setTitle(courseDTO.getTitle());
            course.setDescription(courseDTO.getDescription());
            course.setContent(courseDTO.getContent());
            course.setPrice(courseDTO.getPrice());
            course.setCreatedAt(courseDTO.getCreatedAt());
            course.setUpdatedAt(LocalDate.now());
            course.setCategory(category);

            courseRepository.save(course);
            return course.toDTO();
        }catch (PropertyValueException e) {
            throw new InvalidRequestException("");
        }
    }

    public CourseDTO getCourseById(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 강의를 찾을 수 없습니다."));

        return course.toDTO();
    }

    public CourseDTO updateCourse(Long courseId, CourseDTO courseDTO){
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 강의를 찾을 수 없습니다."));
        Category category = categoryRepository.findById(
                courseDTO.getCategory()).orElseThrow(()->new InvalidRequestException("카테고리를 찾을 수 없습니다."));

        course.setTitle(courseDTO.getTitle());
        course.setDescription(courseDTO.getDescription());
        course.setContent(courseDTO.getContent());
        course.setPrice(courseDTO.getPrice());
        course.setCategory(category);

        courseRepository.save(course);

        return course.toDTO();
    }

    public String deleteCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 강의를 찾을 수 없습니다."));

        courseRepository.delete(course);
        return "강의 삭제 성공 !!";
    }

    public List<CourseDTO> getCoursesByCategory(String categoryName) {
        List<Course> courses = courseRepository.findByCategoryCategoryName(categoryName);

        return courses.stream().map(Course::toDTO).toList();
    }

    public List<CourseDTO> getCoursesByPriceRange(long minPrice , long maxPrice) {
        List<Course> courses = courseRepository.findByPriceBetween(minPrice, maxPrice);
        return courses.stream().map(Course::toDTO).toList();
    }

    public List<CourseDTO> getFreeCourses() {
        List<Course> freeCourses = courseRepository.findByCategoryCategoryName("자유여행");
        return freeCourses.stream().map(Course::toDTO).toList();
    }

    public List<CourseDTO> getFamilyCourses() {
        List<Course> familyCourses = courseRepository.findByCategoryCategoryName("가족여행");
        return familyCourses.stream().map(Course::toDTO).toList();
    }

    public List<CourseDTO> getPackageCourses() {
        List<Course> packageCourses = courseRepository.findByCategoryCategoryName("패키지여행");
        return packageCourses.stream().map(Course::toDTO).toList();
    }

    public List<CourseDTO> getCommonCourses() {
        List<Course> commonCourses = courseRepository.findByCategoryCategoryName("공통");
        return commonCourses.stream().map(Course::toDTO).toList();
    }
}
