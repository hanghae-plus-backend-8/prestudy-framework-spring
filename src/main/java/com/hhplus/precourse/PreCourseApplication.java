package com.hhplus.precourse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class PreCourseApplication {

    public static void main(String[] args) {
        SpringApplication.run(PreCourseApplication.class, args);
    }
}
