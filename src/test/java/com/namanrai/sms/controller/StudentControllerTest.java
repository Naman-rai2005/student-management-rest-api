package com.namanrai.sms.controller;

import com.namanrai.sms.config.ApiKeyAuthenticationFilter;
import com.namanrai.sms.config.SecurityConfig;
import com.namanrai.sms.dto.StudentDTO;
import com.namanrai.sms.exception.GlobalExceptionHandler;
import com.namanrai.sms.exception.StudentNotFoundException;
import com.namanrai.sms.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
@Import({SecurityConfig.class, ApiKeyAuthenticationFilter.class, GlobalExceptionHandler.class})
@TestPropertySource(properties = "app.security.api-key=test-api-key")
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Test
    void getAllStudents_returnsOk() throws Exception {
        when(studentService.getAllStudents()).thenReturn(List.of(
                new StudentDTO(1L, "Naman", "naman@example.com", 21, 1L, 2L, 3L)
        ));

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Naman"));

        verify(studentService).getAllStudents();
    }

    @Test
    void getStudentById_whenFound_returnsOk() throws Exception {
        when(studentService.getStudentById(1L))
                .thenReturn(new StudentDTO(
                        1L, "Naman", "naman@example.com", 21, 1L, 2L, 3L));

        mockMvc.perform(get("/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("naman@example.com"));
    }

    @Test
    void getStudentById_whenNotFound_returnsNotFound() throws Exception {
        when(studentService.getStudentById(99L))
                .thenThrow(new StudentNotFoundException(99L));

        mockMvc.perform(get("/students/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.path").value("/students/99"));
    }

    @Test
    void getStudentById_whenUnexpectedError_returnsInternalServerError() throws Exception {
        when(studentService.getStudentById(1L))
                .thenThrow(new IllegalStateException("Database unavailable"));

        mockMvc.perform(get("/students/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.path").value("/students/1"));
    }

    @Test
    void createStudent_withApiKey_returnsCreated() throws Exception {
        when(studentService.saveStudent(any(StudentDTO.class)))
                .thenReturn(new StudentDTO(
                        1L, "Naman", "naman@example.com", 21, 1L, 2L, 3L));

        String body = """
                {
                  "name": "Naman",
                  "email": "naman@example.com",
                  "age": 21,
                  "departmentId": 1,
                  "courseId": 2,
                  "addressId": 3
                }
                """;

        mockMvc.perform(post("/students")
                        .header("X-API-KEY", "test-api-key")
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

        verify(studentService).saveStudent(any(StudentDTO.class));
    }

    @Test
    void createStudent_withoutApiKey_returnsUnauthorized() throws Exception {
        String body = """
                {
                  "name": "Naman",
                  "email": "naman@example.com",
                  "age": 21,
                  "departmentId": 1,
                  "courseId": 2,
                  "addressId": 3
                }
                """;

        mockMvc.perform(post("/students")
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(studentService);
    }


    @Test
    void updateStudent_withApiKey_returnsOk() throws Exception {
        when(studentService.updateStudent(eq(1L), any(StudentDTO.class)))
                .thenReturn(new StudentDTO(1L, "Naman Updated", "updated@example.com", 22, 1L, 2L, 3L));

        String body = """
                {
                  "name": "Naman Updated",
                  "email": "updated@example.com",
                  "age": 22,
                  "departmentId": 1,
                  "courseId": 2,
                  "addressId": 3
                }
                """;

        mockMvc.perform(put("/students/1")
                        .header("X-API-KEY", "test-api-key")
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Naman Updated"));

        verify(studentService).updateStudent(eq(1L), any(StudentDTO.class));
    }

    @Test
    void updateStudent_withoutApiKey_returnsUnauthorized() throws Exception {
        String body = """
                {
                  "name": "Naman Updated",
                  "email": "updated@example.com",
                  "age": 22,
                  "departmentId": 1,
                  "courseId": 2,
                  "addressId": 3
                }
                """;

        mockMvc.perform(put("/students/1")
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(studentService);
    }

    @Test
    void createStudent_withInvalidPayload_returnsBadRequest() throws Exception {
        String body = """
                {
                  "name": "",
                  "email": "not-an-email",
                  "age": 17
                }
                """;

        mockMvc.perform(post("/students")
                        .header("X-API-KEY", "test-api-key")
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Request validation failed."))
                .andExpect(jsonPath("$.validationErrors.name").exists())
                .andExpect(jsonPath("$.validationErrors.email").exists())
                .andExpect(jsonPath("$.validationErrors.age").exists());

        verifyNoInteractions(studentService);
    }

    @Test
    void deleteStudent_withApiKey_returnsNoContent() throws Exception {
        doNothing().when(studentService).deleteStudent(1L);

        mockMvc.perform(delete("/students/1")
                        .header("X-API-KEY", "test-api-key"))
                .andExpect(status().isNoContent());

        verify(studentService).deleteStudent(1L);
    }
}
