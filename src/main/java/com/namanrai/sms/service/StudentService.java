package com.namanrai.sms.service;

import com.namanrai.sms.dto.StudentDTO;
import com.namanrai.sms.entity.Address;
import com.namanrai.sms.entity.Course;
import com.namanrai.sms.entity.Department;
import com.namanrai.sms.entity.Student;
import com.namanrai.sms.exception.AddressNotFoundException;
import com.namanrai.sms.exception.CourseNotFoundException;
import com.namanrai.sms.exception.DepartmentNotFoundException;
import com.namanrai.sms.exception.StudentNotFoundException;
import com.namanrai.sms.repository.AddressRepository;
import com.namanrai.sms.repository.CourseRepository;
import com.namanrai.sms.repository.DepartmentRepository;
import com.namanrai.sms.repository.StudentRepository;
import com.namanrai.sms.util.StudentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class StudentService {

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;
    private final AddressRepository addressRepository;


    public StudentService(StudentRepository studentRepository,
                          DepartmentRepository departmentRepository,
                          CourseRepository courseRepository,
                          AddressRepository addressRepository) {

        this.studentRepository = studentRepository;
        this.departmentRepository = departmentRepository;
        this.courseRepository = courseRepository;
        this.addressRepository = addressRepository;
    }


    // CREATE
    public StudentDTO saveStudent(StudentDTO dto) {

        log.info("Creating student with email: {}", dto.getEmail());

        Student student = StudentMapper.toEntity(dto);


        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException(
                        "Department not found with id: " + dto.getDepartmentId()));


        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new CourseNotFoundException(
                        "Course not found with id: " + dto.getCourseId()));


        Address address = addressRepository.findById(dto.getAddressId())
                .orElseThrow(() -> new AddressNotFoundException(
                        "Address not found with id: " + dto.getAddressId()));


        student.setDepartment(department);
        student.setCourse(course);
        student.setAddress(address);


        Student savedStudent = studentRepository.save(student);


        log.info("Student created successfully with id: {}", savedStudent.getId());


        return StudentMapper.toDTO(savedStudent);
    }



    // READ ALL
    public List<StudentDTO> getAllStudents() {

        log.info("Fetching all students");


        return studentRepository.findAll()
                .stream()
                .map(StudentMapper::toDTO)
                .toList();
    }



    // READ BY ID
    public StudentDTO getStudentById(Long id) {

        log.info("Fetching student with id: {}", id);


        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException(id));


        return StudentMapper.toDTO(student);
    }



    // UPDATE
    public StudentDTO updateStudent(Long id, StudentDTO dto) {

        log.info("Updating student with id: {}", id);


        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException(id));


        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException(
                        "Department not found with id: " + dto.getDepartmentId()));


        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new CourseNotFoundException(
                        "Course not found with id: " + dto.getCourseId()));


        Address address = addressRepository.findById(dto.getAddressId())
                .orElseThrow(() -> new AddressNotFoundException(
                        "Address not found with id: " + dto.getAddressId()));



        existingStudent.setName(dto.getName());
        existingStudent.setEmail(dto.getEmail());
        existingStudent.setAge(dto.getAge());

        existingStudent.setDepartment(department);
        existingStudent.setCourse(course);
        existingStudent.setAddress(address);



        Student updatedStudent = studentRepository.save(existingStudent);


        log.info("Student updated successfully with id: {}", id);


        return StudentMapper.toDTO(updatedStudent);
    }




    // DELETE
    public void deleteStudent(Long id) {

        log.info("Deleting student with id: {}", id);


        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException(id));


        studentRepository.delete(student);


        log.info("Student deleted successfully with id: {}", id);
    }
}