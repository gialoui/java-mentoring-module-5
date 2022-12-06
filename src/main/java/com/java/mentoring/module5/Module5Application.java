package com.java.mentoring.module5;

import com.java.mentoring.module5.service.MessengerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class Module5Application implements CommandLineRunner {

    @Autowired
    private MessengerService messengerService;

    public static void main(String[] args) {
        log.info("Starting the application");
        SpringApplication.run(Module5Application.class, args);
        log.info("Application finished");
    }

    @Override
    public void run(String... args) throws Exception {
        // Identify mode
        
    }
}
