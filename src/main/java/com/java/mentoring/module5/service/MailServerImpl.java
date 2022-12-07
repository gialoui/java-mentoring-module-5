package com.java.mentoring.module5.service;

import org.springframework.stereotype.Service;

/**
 * @author khangndd
 */
@Service
public class MailServerImpl implements MailServer {
    @Override
    public String send(String addresses, String messageContent) {
        // Implementation goes here
        return String.format("To: %s\n\n%s", addresses, messageContent);
    }
}
