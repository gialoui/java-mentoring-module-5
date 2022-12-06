package com.java.mentoring.module5.service;

import com.java.mentoring.module5.model.Client;
import com.java.mentoring.module5.model.Template;
import com.java.mentoring.module5.utils.TemplateEngine;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

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
            throw new IllegalArgumentException("Required args should not be null");
        }

        String messageContent = templateEngine.generate(template, client);
        System.out.println(mailServer.send(client.getAddresses(), messageContent));
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
                                    String outputFileName) throws IOException {
        if (client == null || StringUtils.isBlank(inputFileName) || StringUtils.isBlank(paramsFileName) || StringUtils.isBlank(outputFileName)) {
            throw new IllegalArgumentException("Required args should not be null");
        }

        File paramsFile = new File(paramsFileName);

        var template = Template.builder()
                .path(inputFileName)
                .values(Files.readAllLines(paramsFile.toPath()).stream()
                        .map(item -> item.split("="))
                        .collect(Collectors.toMap(value -> value[0], value -> value[1]))).build();
        var messageContent = templateEngine.generate(template, client);
        String emailContent = mailServer.send(client.getAddresses(), messageContent);

        // Write to output file
        Files.write(Paths.get(outputFileName), emailContent.getBytes());
    }
}
