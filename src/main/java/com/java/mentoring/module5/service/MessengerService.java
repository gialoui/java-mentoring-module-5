package com.java.mentoring.module5.service;

import com.java.mentoring.module5.model.Client;
import com.java.mentoring.module5.model.Template;
import com.java.mentoring.module5.utils.TemplateEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * @author khangndd
 */
@Service
@Validated
public class MessengerService {
    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private MailServer mailServer;

    /**
     * @param client
     * @param template
     */
    public void sendEmailInConsoleMode(Client client, Template template) {
        if (client == null || template == null) {
            throw new IllegalArgumentException();
        }

        String messageContent = templateEngine.generate(template, client);
        mailServer.send(client.getAddresses(), messageContent);
    }

    /**
     * @param client
     * @param inputFileName
     * @param paramsFileName
     * @param outputFileName
     */
    public void sendEmailInFileMode(Client client,
                                    String inputFileName,
                                    String paramsFileName,
                                    String outputFileName) {
        var template = Template.builder().build();
        var messageContent = templateEngine.generate(template, client);
        mailServer.send(client.getAddresses(), messageContent);
    }
}
