package com.java.mentoring.module5.utils;

import com.java.mentoring.module5.exception.NullArgumentException;
import com.java.mentoring.module5.model.Client;
import com.java.mentoring.module5.model.Template;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author khangndd
 */
@Component
public class TemplateEngine {
    public String generate(Template template, Client client) {
        try {
            var templateContent = FileUtils.readFileToString(new File(template.getPath()), StandardCharsets.UTF_8);

            String pattern = "(?<=\\#\\{)(.*?)(?=\\})";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(templateContent);

            while (m.find()) {
                String placeholder = m.group(1);

                if (!template.getValues().containsKey(placeholder)) {
                    throw new NullArgumentException();
                } else {
                    templateContent = templateContent.replace(String.format("#{%s}", placeholder), template.getValues().get(new String(placeholder.getBytes(), StandardCharsets.UTF_8)));
                }
            }

            return templateContent;
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
}
