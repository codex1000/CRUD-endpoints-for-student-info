package com.example.demo.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student saveStudent(Student student) {

        return studentRepository.findStudentByEmail(student.getEmail())
                .orElseGet(() -> studentRepository.save(student));
    }

    public void addNewStudent(Student student){
        Optional<Student> studentOptional = studentRepository
                .findStudentByEmail(student.getEmail());
        if(studentOptional.isPresent()){
            throw new IllegalStateException("email taken");
        }
        studentRepository.save(student);
    }

    public void deleteStudent(Long studentId){

        boolean exists = studentRepository.existsById(studentId);
        if(!exists){
            throw new IllegalStateException(
                    "student with ID" + studentId + "does not exist"
            );
        }
        studentRepository.deleteById(studentId);
    }


    @Transactional
    public void updateStudent(Long studentId,
                              String name,
                              String email){

        //Simple Business logic checking if a student exist with the id Otherwise an Exception is thrown
        Student student = studentRepository.findById(studentId).orElseThrow(()
                -> new IllegalStateException("student with id" + studentId + "does not exist"));

                if (name != null && name.length() > 0 &&
                    !Objects.equals(student.getName(), name)){
                    student.setName(name);
                }

                if (email != null && email.length() > 0 &&
                   !Objects.equals(student.getEmail(), email)){

                    //Check if email has not been taken
                    Optional<Student> studentOptional = studentRepository
                            .findStudentByEmail(email);

                    //If email has been taken throw exception
                    if (studentOptional.isPresent()){
                        throw  new IllegalStateException("email taken");
                    }
                    //Else update the student with email
                        student.setEmail(email);
                }



    }
}
