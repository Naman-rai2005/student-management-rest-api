package com.namanrai.sms.controller;

import com.namanrai.sms.dto.CourseDTO;
import com.namanrai.sms.service.CourseService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class CourseController {


    private final CourseService courseService;


    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }




    // CREATE
    @PostMapping("/courses")
    @ResponseStatus(HttpStatus.CREATED)
    public CourseDTO addCourse(@Valid @RequestBody CourseDTO dto) {

        log.info("POST request received to create course");

        return courseService.saveCourse(dto);
    }




    // READ ALL
    @GetMapping("/courses")
    public List<CourseDTO> getAllCourses() {

        log.info("GET request received to fetch all courses");

        return courseService.getAllCourses();
    }




    // READ BY ID
    @GetMapping("/courses/{id}")
    public CourseDTO getCourseById(@PathVariable Long id) {

        log.info("GET request received to fetch course with id: {}", id);

        return courseService.getCourseById(id);
    }




    // UPDATE
    @PutMapping("/courses/{id}")
    public CourseDTO updateCourse(@PathVariable Long id,
                                  @Valid @RequestBody CourseDTO dto) {

        log.info("PUT request received to update course with id: {}", id);

        return courseService.updateCourse(id, dto);
    }




    // DELETE
    // DELETE
    @DeleteMapping("/courses/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourse(@PathVariable Long id) {

        log.info("DELETE request received for course with id: {}", id);

        courseService.deleteCourse(id);
    }
}