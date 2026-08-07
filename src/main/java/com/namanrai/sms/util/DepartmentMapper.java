package com.namanrai.sms.util;

import com.namanrai.sms.dto.DepartmentDTO;
import com.namanrai.sms.entity.Department;

public class DepartmentMapper {

    public static DepartmentDTO toDTO(Department department) {

        DepartmentDTO dto = new DepartmentDTO();

        dto.setId(department.getId());
        dto.setName(department.getName());
        dto.setLocation(department.getLocation());

        return dto;
    }

    public static Department toEntity(DepartmentDTO dto) {

        Department department = new Department();

        department.setId(dto.getId());
        department.setName(dto.getName());
        department.setLocation(dto.getLocation());

        return department;
    }
}