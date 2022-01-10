package com.example.demo.student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class StudentServiceUnitTest {

    private Student student;
    private Student student2;

    static Student createdStudent;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    StudentRepository studentRepository;

    @InjectMocks
    StudentServiceImpl studentService;

    @BeforeEach
    void setUp() {

        student = new Student();
        student2 = new Student();

        //Set up for student 1
        student.setName("Dummy Student");
        student.setEmail("dummy@student.com");
        student.setDob(LocalDate.of(1989, 1, 1));

        //Set up for student 2
        student2.setName("Dummy Student 2");
        student2.setEmail("dummy2@student2.com");
        student2.setDob(LocalDate.of(1979, 1, 1));

        when(studentRepository.findAll()).thenReturn(List.of(student, student2));
        when(studentRepository.findStudentByEmail(anyString())).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenAnswer(invocationOnMock -> {
            createdStudent = invocationOnMock.getArgument(0);
            return createdStudent;
        });
    }

    @Test
    void saveStudent() {

        //1. We want repo to return empty any time we call findStudentByEmail
        when(studentRepository.findStudentByEmail(anyString())).thenReturn(Optional.empty());
        when(studentRepository.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        //2. Set up a student we want to be saved in our DB
        Student newStudent = new Student();
        newStudent.setName("Ade");
        newStudent.setEmail("ade@gmial.com");
        newStudent.setDob(LocalDate.of(1999, 1, 1));

        //Proof that before we called the addNewStudent method, createdStudent was null
        assertNull(createdStudent);

        //3. Call the method we want to test and see what is inside
        final Student saveStudent = studentService.saveStudent(newStudent);

        //Assert that there now values in the created student object
        assertNull(createdStudent);
        assertNotNull(saveStudent);
        assertEquals(newStudent.getAge(), saveStudent.getAge());
        assertEquals(newStudent.getName(), saveStudent.getName());
        assertEquals(newStudent.getEmail(), saveStudent.getEmail());
    }

    @Test
    void addNewStudent() {

        //1. We want repo to return empty any time we call findStudentByEmail
        when(studentRepository.findStudentByEmail(anyString())).thenReturn(Optional.empty());

        //2. Set up a student we want to be saved in our DB
        Student newStudent = new Student();
        newStudent.setName("Ade");
        newStudent.setEmail("ade@gmial.com");
        newStudent.setDob(LocalDate.of(1999, 1, 1));

        //Proof that before we called the addNewStudent method, createdStudent was null
        assertNull(createdStudent);

        //3. Call the method we want to test and see what is inside
        studentService.addNewStudent(newStudent);

        //Assert that there now values in the created student object
        assertNotNull(createdStudent);
        assertEquals(newStudent.getAge(), createdStudent.getAge());
        assertEquals(newStudent.getName(), createdStudent.getName());
        assertEquals(newStudent.getEmail(), createdStudent.getEmail());
    }

    @Test
    void getStudents() {

        //1. Set up a student we want to be saved in our DB
        Student newStudent = new Student();
        newStudent.setName("Ade");
        newStudent.setEmail("ade@gmial.com");
        newStudent.setDob(LocalDate.of(1999, 1, 1));

        //2. Proof that before we called the addNewStudent method, createdStudent was null
        assertNull(createdStudent);

        //3. Call the method we want to test and see what is inside
        List<Student> result = studentService.getStudents();
        Student firstStudent = result.get(0);

        //4. Assert that there now values in the created student object
        assertEquals(result.size(), 2);

    }
        @Test
    void deleteStudent() throws Exception {

            Mockito.when(studentRepository.findStudentByEmail(anyString())).thenReturn(Optional.empty());

            mockMvc.perform(MockMvcRequestBuilders
                                .delete("http://localhost:8080/api/v1/student/1")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
            }

   @Test
      void deleteStudentById() {

       long studentId = 1L;
       when(studentRepository.existsById(any())).thenReturn(false);

       //Asserting that sn error will occur if a student is not found by the id we hare deleting with
       assertThrows(
               IllegalStateException.class,
               () -> studentService.deleteStudent(studentId),
               "student with ID" + studentId + "does not exist"
       );
   }
       @Test
       void updateStudent() {


       }
   }
