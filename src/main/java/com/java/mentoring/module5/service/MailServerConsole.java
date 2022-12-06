package com.java.mentoring.module5.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author khangndd
 */
@Slf4j
@Service
public class MailServerConsole implements MailServer {
    @Override
    public void send(String addresses, String messageContent) {
    }
}
