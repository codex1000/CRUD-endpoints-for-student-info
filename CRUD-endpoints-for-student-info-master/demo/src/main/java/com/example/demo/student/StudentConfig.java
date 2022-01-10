package com.example.demo.student;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Configuration
public class StudentConfig {

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository repository){
        return args -> {
            Student mariam = new Student(
                    "Mariam",
                    "mariam@gmail.com",
                    LocalDate.of(1996, Month.NOVEMBER, 11)
            );

            Student yousef = new Student(
                    "yousef",
                    "yousef@gmail.com",
                    LocalDate.of(1998, Month.NOVEMBER, 15)
            );
            repository.saveAll(
                    List.of(mariam, yousef)
            );

        };
    }
}
