package com.namanrai.sms.service;

import com.namanrai.sms.dto.StudentDTO;
import com.namanrai.sms.entity.Address;
import com.namanrai.sms.entity.Course;
import com.namanrai.sms.entity.Department;
import com.namanrai.sms.entity.Student;
import com.namanrai.sms.exception.StudentNotFoundException;
import com.namanrai.sms.repository.AddressRepository;
import com.namanrai.sms.repository.CourseRepository;
import com.namanrai.sms.repository.DepartmentRepository;
import com.namanrai.sms.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock private StudentRepository studentRepository;
    @Mock private DepartmentRepository departmentRepository;
    @Mock private CourseRepository courseRepository;
    @Mock private AddressRepository addressRepository;

    @InjectMocks private StudentService studentService;

    @Test
    void getStudentById_whenStudentExists_returnsStudent() {
        Student student = student(1L, "Naman", "naman@example.com", 21);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        StudentDTO result = studentService.getStudentById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Naman", result.getName());
        assertEquals("naman@example.com", result.getEmail());
        verify(studentRepository).findById(1L);
    }

    @Test
    void getStudentById_whenStudentDoesNotExist_throwsException() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class,
                () -> studentService.getStudentById(99L));

        verify(studentRepository).findById(99L);
    }

    @Test
    void getAllStudents_returnsMappedStudents() {
        when(studentRepository.findAll()).thenReturn(List.of(
                student(1L, "Naman", "naman@example.com", 21),
                student(2L, "Rahul", "rahul@example.com", 22)
        ));

        List<StudentDTO> result = studentService.getAllStudents();

        assertEquals(2, result.size());
        assertEquals("Naman", result.get(0).getName());
        assertEquals("Rahul", result.get(1).getName());
        verify(studentRepository).findAll();
    }

    @Test
    void saveStudent_whenRelatedEntitiesExist_createsStudent() {
        StudentDTO dto = dto(null, "Naman", "naman@example.com", 21, 10L, 20L, 30L);
        Department department = new Department(10L, "CSE", "Block A", null);
        Course course = new Course(20L, "Java", 6, null);
        Address address = new Address(30L, "Main Street", "Lucknow", "UP", "226001", "India", null);

        when(departmentRepository.findById(10L)).thenReturn(Optional.of(department));
        when(courseRepository.findById(20L)).thenReturn(Optional.of(course));
        when(addressRepository.findById(30L)).thenReturn(Optional.of(address));
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> {
            Student saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        StudentDTO result = studentService.saveStudent(dto);

        assertEquals(1L, result.getId());
        assertEquals("Naman", result.getName());
        assertEquals(10L, result.getDepartmentId());
        assertEquals(20L, result.getCourseId());
        assertEquals(30L, result.getAddressId());
        verify(studentRepository).save(any(Student.class));
    }


    @Test
    void saveStudent_whenDepartmentDoesNotExist_throwsException() {
        StudentDTO dto = dto(null, "Naman", "naman@example.com", 21, 10L, 20L, 30L);
        when(departmentRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(com.namanrai.sms.exception.DepartmentNotFoundException.class,
                () -> studentService.saveStudent(dto));

        verify(studentRepository, never()).save(any());
    }

    @Test
    void saveStudent_whenCourseDoesNotExist_throwsException() {
        StudentDTO dto = dto(null, "Naman", "naman@example.com", 21, 10L, 20L, 30L);
        when(departmentRepository.findById(10L)).thenReturn(Optional.of(
                new Department(10L, "CSE", "Block A", null)));
        when(courseRepository.findById(20L)).thenReturn(Optional.empty());

        assertThrows(com.namanrai.sms.exception.CourseNotFoundException.class,
                () -> studentService.saveStudent(dto));

        verify(studentRepository, never()).save(any());
    }

    @Test
    void saveStudent_whenAddressDoesNotExist_throwsException() {
        StudentDTO dto = dto(null, "Naman", "naman@example.com", 21, 10L, 20L, 30L);
        when(departmentRepository.findById(10L)).thenReturn(Optional.of(
                new Department(10L, "CSE", "Block A", null)));
        when(courseRepository.findById(20L)).thenReturn(Optional.of(
                new Course(20L, "Java", 6, null)));
        when(addressRepository.findById(30L)).thenReturn(Optional.empty());

        assertThrows(com.namanrai.sms.exception.AddressNotFoundException.class,
                () -> studentService.saveStudent(dto));

        verify(studentRepository, never()).save(any());
    }

    @Test
    void updateStudent_whenStudentExists_updatesAndReturnsStudent() {
        Student existing = student(1L, "Old Name", "old@example.com", 20);
        StudentDTO dto = dto(1L, "Naman", "naman@example.com", 21, 10L, 20L, 30L);

        Department department = new Department(10L, "CSE", "Block A", null);
        Course course = new Course(20L, "Java", 6, null);
        Address address = new Address(30L, "Main Street", "Lucknow", "UP", "226001", "India", null);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(departmentRepository.findById(10L)).thenReturn(Optional.of(department));
        when(courseRepository.findById(20L)).thenReturn(Optional.of(course));
        when(addressRepository.findById(30L)).thenReturn(Optional.of(address));
        when(studentRepository.save(existing)).thenReturn(existing);

        StudentDTO result = studentService.updateStudent(1L, dto);

        assertEquals("Naman", result.getName());
        assertEquals("naman@example.com", result.getEmail());
        assertEquals(21, result.getAge());
        assertEquals(10L, result.getDepartmentId());
        verify(studentRepository).save(existing);
    }

    @Test
    void updateStudent_whenStudentDoesNotExist_throwsException() {
        StudentDTO dto = dto(99L, "Naman", "naman@example.com", 21, 10L, 20L, 30L);
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class,
                () -> studentService.updateStudent(99L, dto));

        verify(studentRepository, never()).save(any());
    }

    @Test
    void deleteStudent_whenStudentExists_deletesStudent() {
        Student student = student(1L, "Naman", "naman@example.com", 21);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        studentService.deleteStudent(1L);

        verify(studentRepository).delete(student);
    }

    @Test
    void deleteStudent_whenStudentDoesNotExist_throwsException() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class,
                () -> studentService.deleteStudent(99L));

        verify(studentRepository, never()).delete(any());
    }

    private Student student(Long id, String name, String email, Integer age) {
        return new Student(id, name, email, age, null, null, null);
    }

    private StudentDTO dto(Long id, String name, String email, Integer age,
                           Long departmentId, Long courseId, Long addressId) {
        return new StudentDTO(id, name, email, age, departmentId, courseId, addressId);
    }
}
