package com.java.mentoring.module5.service;

import com.java.mentoring.module5.model.Client;
import com.java.mentoring.module5.model.Template;
import com.java.mentoring.module5.utils.TemplateEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author khangndd
 */
@Service
public class MessengerService {
    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private MailServer mailServer;

    public void sendEmailInConsoleMode(Client client, Template template) {
        String messageContent = templateEngine.generate(template, client);
        mailServer.send(client.getAddresses(), messageContent);
    }

    public void sendEmailInFileMode(Client client, String inputFileName, String outputFileName) {
        var template = Template.builder().build();
        var messageContent = templateEngine.generate(template, client);
        mailServer.send(client.getAddresses(), messageContent);
    }
}
