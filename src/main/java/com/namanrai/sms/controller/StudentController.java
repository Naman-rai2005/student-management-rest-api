package com.namanrai.sms.controller;

import com.namanrai.sms.dto.StudentDTO;
import com.namanrai.sms.service.StudentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class StudentController {


    private final StudentService studentService;


    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }



    // CREATE
    @PostMapping("/students")
    @ResponseStatus(HttpStatus.CREATED)
    public StudentDTO addStudent(@Valid @RequestBody StudentDTO dto) {

        log.info("POST request received to create student");

        return studentService.saveStudent(dto);
    }




    // READ ALL
    @GetMapping("/students")
    public List<StudentDTO> getAllStudents() {

        log.info("GET request received to fetch all students");

        return studentService.getAllStudents();
    }




    // READ BY ID
    @GetMapping("/students/{id}")
    public StudentDTO getStudentById(@PathVariable Long id) {

        log.info("GET request received to fetch student with id: {}", id);

        return studentService.getStudentById(id);
    }




    // UPDATE
    @PutMapping("/students/{id}")
    public StudentDTO updateStudent(@PathVariable Long id,
                                    @Valid @RequestBody StudentDTO dto) {

        log.info("PUT request received to update student with id: {}", id);

        return studentService.updateStudent(id, dto);
    }




    // DELETE
    @DeleteMapping("/students/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable Long id) {

        log.info("DELETE request received for student with id: {}", id);

        studentService.deleteStudent(id);
    }
}