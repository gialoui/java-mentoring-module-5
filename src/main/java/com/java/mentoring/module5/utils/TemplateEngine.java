package com.java.mentoring.module5.utils;

import com.java.mentoring.module5.exception.ArgumentNullException;
import com.java.mentoring.module5.model.Client;
import com.java.mentoring.module5.model.Template;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
            var input = StringUtils.class.getResourceAsStream(template.getPath());
            var templateContent = IOUtils.toString(input, StandardCharsets.UTF_8);

            String pattern = "(?<=\\#\\{)(.*?)(?=\\})";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(templateContent);

            while (m.find()) {
                String placeholder = m.group(1);

                if (!template.getValues().containsKey(placeholder)) {
                    throw new ArgumentNullException();
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
