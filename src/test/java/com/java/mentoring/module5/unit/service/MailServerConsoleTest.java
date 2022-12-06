package com.java.mentoring.module5.unit.service;

import com.java.mentoring.module5.service.MailServerConsole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * @author khangndd
 */
@ExtendWith(MockitoExtension.class)
class MailServerConsoleTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @Spy
    MailServerConsole mailServerConsole;

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    void sendEmail_shouldSuccess() {
        String EMAIL_ADDRESS = "abc@mail.com";
        String MESSAGE = "Sample message";
        mailServerConsole.send(EMAIL_ADDRESS, MESSAGE);

        Assertions.assertEquals(String.format("Email sent to: %s\n%s", EMAIL_ADDRESS, MESSAGE), outputStreamCaptor.toString().trim());
    }
}
