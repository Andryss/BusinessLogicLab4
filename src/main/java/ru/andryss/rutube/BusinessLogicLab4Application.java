package ru.andryss.rutube;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableProcessApplication
public class BusinessLogicLab4Application {

    public static void main(String[] args) {
        SpringApplication.run(BusinessLogicLab4Application.class, args);
    }

}
