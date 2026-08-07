package com.namanrai.sms.service;

import com.namanrai.sms.dto.CourseDTO;
import com.namanrai.sms.entity.Course;
import com.namanrai.sms.exception.CourseNotFoundException;
import com.namanrai.sms.repository.CourseRepository;
import com.namanrai.sms.util.CourseMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CourseService {


    private final CourseRepository courseRepository;


    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }



    // CREATE
    public CourseDTO saveCourse(CourseDTO dto) {

        log.info("Creating course with name: {}", dto.getName());


        Course course = CourseMapper.toEntity(dto);


        Course savedCourse = courseRepository.save(course);


        log.info("Course created successfully with id: {}", savedCourse.getId());


        return CourseMapper.toDTO(savedCourse);
    }




    // READ ALL
    public List<CourseDTO> getAllCourses() {

        log.info("Fetching all courses");


        return courseRepository.findAll()
                .stream()
                .map(CourseMapper::toDTO)
                .toList();
    }




    // READ BY ID
    public CourseDTO getCourseById(Long id) {

        log.info("Fetching course with id: {}", id);


        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new CourseNotFoundException(
                                "Course not found with id: " + id));


        return CourseMapper.toDTO(course);
    }




    // UPDATE
    public CourseDTO updateCourse(Long id, CourseDTO dto) {

        log.info("Updating course with id: {}", id);


        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() ->
                        new CourseNotFoundException(
                                "Course not found with id: " + id));


        existingCourse.setName(dto.getName());
        existingCourse.setDuration(dto.getDuration());


        Course updatedCourse = courseRepository.save(existingCourse);


        log.info("Course updated successfully with id: {}", id);


        return CourseMapper.toDTO(updatedCourse);
    }




    // DELETE
    public void deleteCourse(Long id) {

        log.info("Deleting course with id: {}", id);


        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new CourseNotFoundException(
                                "Course not found with id: " + id));


        courseRepository.delete(course);


        log.info("Course deleted successfully with id: {}", id);
    }
}