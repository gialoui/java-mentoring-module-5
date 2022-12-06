package com.java.mentoring.module5.service;

import org.springframework.stereotype.Service;

/**
 * @author khangndd
 */
@Service
public class MailServerConsole implements MailServer {
    @Override
    public void send(String addresses, String messageContent) {
        System.out.printf("Email sent to: %s\n%s%n", addresses, messageContent);
    }
}
