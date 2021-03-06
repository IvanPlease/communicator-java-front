package com.communicatorfront;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CommunicatorFrontApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommunicatorFrontApplication.class, args);
    }

}
