package com.namanrai.sms.service;

import com.namanrai.sms.dto.DepartmentDTO;
import com.namanrai.sms.entity.Department;
import com.namanrai.sms.exception.DepartmentNotFoundException;
import com.namanrai.sms.repository.DepartmentRepository;
import com.namanrai.sms.util.DepartmentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DepartmentService {

    private final DepartmentRepository departmentRepository;


    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }


    // CREATE
    public DepartmentDTO saveDepartment(DepartmentDTO dto) {

        log.info("Creating department with name: {}", dto.getName());


        Department department = DepartmentMapper.toEntity(dto);


        Department savedDepartment = departmentRepository.save(department);


        log.info("Department created successfully with id: {}", savedDepartment.getId());


        return DepartmentMapper.toDTO(savedDepartment);
    }



    // READ ALL
    public List<DepartmentDTO> getAllDepartments() {

        log.info("Fetching all departments");


        return departmentRepository.findAll()
                .stream()
                .map(DepartmentMapper::toDTO)
                .toList();
    }




    // READ BY ID
    public DepartmentDTO getDepartmentById(Long id) {

        log.info("Fetching department with id: {}", id);


        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new DepartmentNotFoundException(
                                "Department not found with id: " + id));


        return DepartmentMapper.toDTO(department);
    }




    // UPDATE
    public DepartmentDTO updateDepartment(Long id, DepartmentDTO dto) {

        log.info("Updating department with id: {}", id);


        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new DepartmentNotFoundException(
                                "Department not found with id: " + id));


        existingDepartment.setName(dto.getName());
        existingDepartment.setLocation(dto.getLocation());


        Department updatedDepartment = departmentRepository.save(existingDepartment);


        log.info("Department updated successfully with id: {}", id);


        return DepartmentMapper.toDTO(updatedDepartment);
    }




    // DELETE
    public void deleteDepartment(Long id) {

        log.info("Deleting department with id: {}", id);


        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new DepartmentNotFoundException(
                                "Department not found with id: " + id));


        departmentRepository.delete(department);


        log.info("Department deleted successfully with id: {}", id);
    }
}