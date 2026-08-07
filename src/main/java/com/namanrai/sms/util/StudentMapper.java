package com.namanrai.sms.util;

import com.namanrai.sms.dto.StudentDTO;
import com.namanrai.sms.entity.Student;

public class StudentMapper {

    public static StudentDTO toDTO(Student student) {

        StudentDTO dto = new StudentDTO();

        dto.setId(student.getId());
        dto.setName(student.getName());
        dto.setEmail(student.getEmail());
        dto.setAge(student.getAge());

        if (student.getDepartment() != null) {
            dto.setDepartmentId(student.getDepartment().getId());
        }

        if (student.getCourse() != null) {
            dto.setCourseId(student.getCourse().getId());
        }

        if (student.getAddress() != null) {
            dto.setAddressId(student.getAddress().getId());
        }

        return dto;
    }

    public static Student toEntity(StudentDTO dto) {

        Student student = new Student();

        student.setId(dto.getId());
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setAge(dto.getAge());

        // Relationships will be set in StudentService

        return student;
    }
}