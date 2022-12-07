package com.java.mentoring.module5;

import com.java.mentoring.module5.enums.MessengerMode;
import com.java.mentoring.module5.model.Client;
import com.java.mentoring.module5.model.Template;
import com.java.mentoring.module5.service.MessengerService;
import com.java.mentoring.module5.utils.CliHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
@Slf4j
public class Module5Application implements CommandLineRunner {

    @Autowired
    private MessengerService messengerService;

    @Autowired
    private CliHelper cliHelper;

    private static final String RESOURCE_DIR = "src/main/resources";

    public static void main(String[] args) {
        log.info("Starting the application");
        SpringApplication.run(Module5Application.class, args);
        log.info("Application finished");
    }

    @Override
    public void run(String... args) {
        // Identify mode
        var currentMode = cliHelper.identifyMode(args);
        var client = Client.builder().addresses("abc@gmail.com;def@gmail.com").build();

        if (MessengerMode.CONSOLE.equals(currentMode)) {
            var params = cliHelper.readParamsFromConsole(System.in);
            messengerService.sendEmailInConsoleMode(client, Template.builder()
                    .path(new File(RESOURCE_DIR + "/template/sample-template.html").getAbsolutePath())
                    .values(params).build());
        }
    }
}
