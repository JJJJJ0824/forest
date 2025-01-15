package com.dw.forest.controller;

import com.dw.forest.dto.CourseDTO;
import com.dw.forest.model.Course;
import com.dw.forest.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/course")
public class CourseController {
    @Autowired
    CourseService courseService;

    @GetMapping("/all")
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @PostMapping("/create")
    public ResponseEntity<Course> createCourse(@RequestBody CourseDTO courseDTO){
        Course createdCourse = courseService.createCourse(courseDTO);

        return new ResponseEntity<>(
                createdCourse, HttpStatus.CREATED
        );
    }

    @GetMapping("/{course_id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable Long course_id){
        CourseDTO courseDTO = courseService.getCourseById(course_id);
        return new ResponseEntity<>(courseDTO, HttpStatus.OK);
    }

    @PutMapping("/{course_id}/update")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable Long course_id,
                                                  @RequestBody CourseDTO courseDTO) {
        CourseDTO updatedCourse = courseService.updateCourse(course_id, courseDTO);
        return new ResponseEntity<>(updatedCourse, HttpStatus.OK);
    }

    @DeleteMapping("/{course_id}/delete")
    public ResponseEntity<String> deleteCourse(@PathVariable Long course_id) {
        return courseService.deleteCourse(course_id);
    }

    @GetMapping("/category/{category_name}") // 특정 카테고리의 강의목록을 조회
    public ResponseEntity<List<CourseDTO>> getCoursesByCategory(@PathVariable String category_name ) {
        List<CourseDTO> courses = courseService.getCoursesByCategory(category_name);
        return new ResponseEntity<>(courses,HttpStatus.OK);
    }

    @GetMapping("/price-range") // 특정가격 범위 내의 강의를 조회
    public ResponseEntity<List<CourseDTO>> getCoursesByPriceRange(
            @RequestParam("minPrice") long minPrice,
            @RequestParam("maxPrice") long maxPrice) {

        List<CourseDTO> courses = courseService.getCoursesByPriceRange(minPrice, maxPrice);

        return ResponseEntity.ok(courses);
    }

    @GetMapping("/type/free")
    public List<CourseDTO> getFreeCourses() {
        return courseService.getFreeCourses();
    }

    @GetMapping("/type/family")
    public List<CourseDTO> getFamilyCourses() {
        return courseService.getFamilyCourses();
    }

    @GetMapping("/type/package")
    public List<CourseDTO> getPackageCourses() {
        return courseService.getPackageCourses();
    }

    @GetMapping("/type/common")
    public List<CourseDTO> getCommonCourses() {
        return courseService.getCommonCourses();
    }

}
