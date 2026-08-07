package com.namanrai.sms.util;

import com.namanrai.sms.dto.CourseDTO;
import com.namanrai.sms.entity.Course;

public class CourseMapper {

    public static CourseDTO toDTO(Course course) {

        CourseDTO dto = new CourseDTO();

        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setDuration(course.getDuration());

        return dto;
    }

    public static Course toEntity(CourseDTO dto) {

        Course course = new Course();

        course.setId(dto.getId());
        course.setName(dto.getName());
        course.setDuration(dto.getDuration());

        return course;
    }
}