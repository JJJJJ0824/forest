package com.dw.forest.controller;

import com.dw.forest.dto.CourseDTO;
import com.dw.forest.dto.CourseReadDTO;
import com.dw.forest.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course")
public class CourseController {
    @Autowired
    CourseService courseService;

    @GetMapping("/all")
    public List<CourseReadDTO> getAllCourses() {
        return courseService.getAllCourses();
    }

    @PostMapping("/create")
    public ResponseEntity<CourseReadDTO> createCourse(@RequestBody CourseDTO courseDTO) {
        return new ResponseEntity<>(courseService.createCourse(courseDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{course_id}")
    public ResponseEntity<CourseReadDTO> getCourseById(@PathVariable Long course_id){
        return new ResponseEntity<>(courseService.getCourseById(course_id), HttpStatus.OK);
    }

    @PutMapping("/{course_id}/update")
    public ResponseEntity<CourseReadDTO> updateCourse(@PathVariable Long course_id, @RequestBody CourseDTO courseDTO) {
        CourseReadDTO updatedCourse = courseService.updateCourse(course_id, courseDTO);
        return new ResponseEntity<>(updatedCourse, HttpStatus.OK);
    }

    @DeleteMapping("/{course_id}/delete")
    public ResponseEntity<String> deleteCourse(@PathVariable Long course_id) {
        return new ResponseEntity<>(courseService.deleteCourse(course_id), HttpStatus.OK);
    }

    @GetMapping("/category/{category_name}") // 특정 카테고리의 강의목록을 조회
    public ResponseEntity<List<CourseReadDTO>> getCoursesByCategory(@PathVariable String category_name) {
        List<CourseReadDTO> courses = courseService.getCoursesByCategory(category_name);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping("/price-range") // 특정가격 범위 내의 강의를 조회
    public ResponseEntity<List<CourseReadDTO>> getCoursesByPriceRange(@RequestParam double min_price, @RequestParam double max_price) {
        List<CourseReadDTO> courses = courseService.getCoursesByPriceRange(min_price, max_price);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/type/free")
    public List<CourseReadDTO> getFreeCourses() {return courseService.getFreeCourses();}

    @GetMapping("/type/family")
    public List<CourseReadDTO> getFamilyCourses() {return courseService.getFamilyCourses();}

    @GetMapping("/type/package")
    public List<CourseReadDTO> getPackageCourses() {return courseService.getPackageCourses();}

    @GetMapping("/type/common")
    public List<CourseReadDTO> getCommonCourses() {return courseService.getCommonCourses();}
}
