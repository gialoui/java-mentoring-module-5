package com.java.mentoring.module5.unit.service;

import com.java.mentoring.module5.exception.ArgumentNullException;
import com.java.mentoring.module5.model.Client;
import com.java.mentoring.module5.model.Template;
import com.java.mentoring.module5.utils.TemplateEngine;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.Map;

/**
 * @author khangndd
 */
@ExtendWith(MockitoExtension.class)
class TemplateEngineTest {
    @Spy
    private TemplateEngine templateEngine;

    @Test
    void generateWithValidValues() throws IOException {
        String output = templateEngine.generate(Template.builder()
                .path("/template/sample-template.html")
                .values(Map.ofEntries(
                        new AbstractMap.SimpleEntry<>("test", "Test ä®"),
                        new AbstractMap.SimpleEntry<>("test1", "Test 1")
                )).build(), Client.builder().addresses("abc@gmail.com;def@gmail.com").build());

        Assertions.assertEquals(getExpectedResult(), output);
    }

    @Test
    void generateWithMissingValue() {
        Assertions.assertThrows(ArgumentNullException.class, () -> templateEngine.generate(Template.builder()
                .path("/template/sample-template.html")
                .values(Map.ofEntries(
                        new AbstractMap.SimpleEntry<>("test1", "Test 1")
                )).build(), Client.builder().addresses("abc@gmail.com;def@gmail.com").build()));
    }

    @Test
    void generateWithRedundantValues() throws IOException {
        String output = templateEngine.generate(Template.builder()
                .path("/template/sample-template.html")
                .values(Map.ofEntries(
                        new AbstractMap.SimpleEntry<>("test", "Test ä®"),
                        new AbstractMap.SimpleEntry<>("test1", "Test 1"),
                        new AbstractMap.SimpleEntry<>("test2", "Test 2")
                )).build(), Client.builder().addresses("abc@gmail.com;def@gmail.com").build());

        Assertions.assertEquals(getExpectedResult(), output);
    }

    private String getExpectedResult() throws IOException {
        var input = StringUtils.class.getResourceAsStream("/test-output/sample-generated-template.html");
        var expected = IOUtils.toString(input, StandardCharsets.UTF_8);

        return expected;
    }
}
