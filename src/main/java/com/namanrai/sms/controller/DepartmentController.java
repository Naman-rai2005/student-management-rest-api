package com.namanrai.sms.controller;

import com.namanrai.sms.dto.DepartmentDTO;
import com.namanrai.sms.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class DepartmentController {


    private final DepartmentService departmentService;


    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }



    // CREATE
    @PostMapping("/departments")
    @ResponseStatus(HttpStatus.CREATED)
    public DepartmentDTO addDepartment(@Valid @RequestBody DepartmentDTO dto) {

        log.info("POST request received to create department");

        return departmentService.saveDepartment(dto);
    }




    // READ ALL
    @GetMapping("/departments")
    public List<DepartmentDTO> getAllDepartments() {

        log.info("GET request received to fetch all departments");

        return departmentService.getAllDepartments();
    }




    // READ BY ID
    @GetMapping("/departments/{id}")
    public DepartmentDTO getDepartmentById(@PathVariable Long id) {

        log.info("GET request received to fetch department with id: {}", id);

        return departmentService.getDepartmentById(id);
    }




    // UPDATE
    @PutMapping("/departments/{id}")
    public DepartmentDTO updateDepartment(@PathVariable Long id,
                                          @Valid @RequestBody DepartmentDTO dto) {

        log.info("PUT request received to update department with id: {}", id);

        return departmentService.updateDepartment(id, dto);
    }




    // DELETE
    @DeleteMapping("/departments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDepartment(@PathVariable Long id) {

        log.info("DELETE request received for department with id: {}", id);

        departmentService.deleteDepartment(id);
    }
}