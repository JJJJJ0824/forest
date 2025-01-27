package com.dw.forest.service;

import com.dw.forest.dto.CourseDTO;
import com.dw.forest.dto.CourseReadDTO;
import com.dw.forest.exception.InvalidRequestException;
import com.dw.forest.exception.ResourceNotFoundException;
import com.dw.forest.model.Category;
import com.dw.forest.model.Course;
import com.dw.forest.repository.CategoryRepository;
import com.dw.forest.repository.CourseRepository;
import org.hibernate.PropertyValueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CourseService {
    @Autowired
    CourseRepository courseRepository;

    @Autowired
    CategoryRepository categoryRepository;

    public List<CourseReadDTO> getAllCourses() {
        List<Course> courses = courseRepository.findAll().stream().toList();
        if (courses.isEmpty()) {
            throw new ResourceNotFoundException("강의 정보를 찾을 수 없습니다.");
        }
        return courses.stream().map(Course::toRead).toList();
    }

    public CourseReadDTO createCourse(CourseDTO courseDTO){
        try {
            Course course = new Course();
            Category category = categoryRepository.findById(courseDTO.getCategoryName())
                    .orElseThrow(() -> new ResourceNotFoundException("카테고리를 찾을 수 없습니다."));

            course.setTitle(courseDTO.getTitle());
            course.setDescription(courseDTO.getDescription());
            course.setContent(courseDTO.getContent());
            course.setPrice(courseDTO.getPrice());
            course.setCreatedAt(LocalDate.now());
            course.setUpdatedAt(null);
            course.setCategory(category);

            courseRepository.save(course);
            return course.toRead();
        }catch (PropertyValueException e) {
            throw new InvalidRequestException("강의를 생성하지 못했습니다. 강의를 생성하려면 모든 내용을 기입해야합니다.");
        }
    }

    public CourseReadDTO getCourseById(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 강의를 찾을 수 없습니다."));

        return course.toRead();
    }

    public CourseReadDTO updateCourse(Long courseId, CourseDTO courseDTO){
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 강의를 찾을 수 없습니다."));

        Category category = categoryRepository.findById(
                courseDTO.getCategoryName()).orElseThrow(()->new InvalidRequestException("카테고리를 찾을 수 없습니다."));

        course.setTitle(courseDTO.getTitle());
        course.setDescription(courseDTO.getDescription());
        course.setContent(courseDTO.getContent());
        course.setPrice(courseDTO.getPrice());
        course.setCategory(category);
        course.setUpdatedAt(LocalDate.now());

        courseRepository.save(course);

        return course.toRead();
    }

    public String deleteCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 강의를 찾을 수 없습니다."));

        courseRepository.delete(course);
        return "강의 삭제 성공";
    }

    public List<CourseReadDTO> getCoursesByCategory(String categoryName) {
        List<Course> courses = courseRepository.findByCategoryCategoryName(categoryName);

        if (courses.isEmpty()) {
            throw new ResourceNotFoundException("해당 카테고리의 강의를 찾을 수 없습니다.");
        }

        return courses.stream().map(Course::toRead).toList();
    }

    public List<CourseReadDTO> getCoursesByPriceRange(double min_price, double max_price) {
        List<Course> courses = courseRepository.findByPriceBetween(min_price, max_price);
        if (courses.isEmpty()) {
            throw new ResourceNotFoundException("해당 가격 범위 내 강의가 없습니다.");
        }
        return courses.stream().map(Course::toRead).toList();
    }

    public List<CourseReadDTO> getFreeCourses() {
        List<Course> freeCourses = courseRepository.findByCategoryCategoryName("자유여행");
        if (freeCourses.isEmpty()) {
            throw new ResourceNotFoundException("자유 유형 강의를 찾을 수 없습니다");
        }
        return freeCourses.stream().map(Course::toRead).toList();
    }

    public List<CourseReadDTO> getFamilyCourses() {
        List<Course> familyCourses = courseRepository.findByCategoryCategoryName("가족여행");
        if (familyCourses.isEmpty()) {
            throw new ResourceNotFoundException("가족 유형 강의를 찾을 수 없습니다");
        }
        return familyCourses.stream().map(Course::toRead).toList();
    }

    public List<CourseReadDTO> getPackageCourses() {
        List<Course> packageCourses = courseRepository.findByCategoryCategoryName("패키지여행");
        if (packageCourses.isEmpty()) {
            throw new ResourceNotFoundException("패키지 유형 강의를 찾을 수 없습니다.");
        }
        return packageCourses.stream().map(Course::toRead).toList();
    }

    public List<CourseReadDTO> getCommonCourses() {
        List<Course> commonCourses = courseRepository.findByCategoryCategoryName("공통");
        if (commonCourses.isEmpty()) {
            throw new ResourceNotFoundException("공통 유형 강의를 찾을 수 없습니다.");
        }
        return commonCourses.stream().map(Course::toRead).toList();
    }
}
