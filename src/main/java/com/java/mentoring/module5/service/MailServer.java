package com.java.mentoring.module5.service;

/**
 * Service to send email to clients
 *
 * @author khangndd
 */
public interface MailServer {
    /**
     * Send notification.
     *
     * @param addresses      the addresses
     * @param messageContent the message content
     */
    String send(String addresses, String messageContent);
}
