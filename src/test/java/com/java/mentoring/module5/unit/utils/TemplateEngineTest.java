package com.java.mentoring.module5.unit.utils;

import com.java.mentoring.module5.exception.NullArgumentException;
import com.java.mentoring.module5.extensions.EnvironmentExtension;
import com.java.mentoring.module5.extensions.TestExecutionLogExtension;
import com.java.mentoring.module5.model.Client;
import com.java.mentoring.module5.model.Template;
import com.java.mentoring.module5.tags.UnhappyCase;
import com.java.mentoring.module5.utils.TemplateEngine;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.DynamicTest.dynamicTest;

/**
 * @author khangndd
 */
@ExtendWith(MockitoExtension.class)
@ExtendWith(EnvironmentExtension.class)
@ExtendWith(TestExecutionLogExtension.class)
class TemplateEngineTest {
    private static final String CLIENT_ADDRESSES = "abc@gmail.com;def@gmail.com";
    private static final String SAMPLE_TEMPLATE_HTML = new File("src/main/resources/template/sample-template.html").getAbsolutePath();

    @Spy
    private TemplateEngine templateEngine;

    /**
     * @return
     */
    private static Stream<Arguments> provideDataToGenerate() {
        return Stream.of(
                Arguments.of(SAMPLE_TEMPLATE_HTML, Map.ofEntries(
                        new AbstractMap.SimpleEntry<>("test", "Test ä®"),
                        new AbstractMap.SimpleEntry<>("test1", "Test 1"),
                        new AbstractMap.SimpleEntry<>("test2", "Test 2"),
                        new AbstractMap.SimpleEntry<>("test3", "Test 3")
                )),
                Arguments.of(SAMPLE_TEMPLATE_HTML, Map.ofEntries(
                        new AbstractMap.SimpleEntry<>("test", "Test ä®"),
                        new AbstractMap.SimpleEntry<>("test1", "Test 1"),
                        new AbstractMap.SimpleEntry<>("test2", "Test 2")
                ))
        );
    }

    @ParameterizedTest
    @MethodSource("provideDataToGenerate")
    void generateWithValidValues(String templatePath,
                                 Map<String, String> values) throws IOException {
        String output = templateEngine.generate(Template.builder()
                .path(templatePath)
                .values(values).build(), Client.builder().addresses(CLIENT_ADDRESSES).build());

        Assertions.assertEquals(getExpectedResult(), output);
    }

    @UnhappyCase
    void generateWithMissingValue() {
        Assertions.assertThrows(NullArgumentException.class, () -> templateEngine.generate(Template.builder()
                .path(SAMPLE_TEMPLATE_HTML)
                .values(Map.ofEntries(
                        new AbstractMap.SimpleEntry<>("test1", "Test 1")
                )).build(), Client.builder().addresses(CLIENT_ADDRESSES).build()));
    }

    @TestFactory
    Collection<DynamicTest> dynamicTestsFromCollection() {
        return List.of(
                dynamicTest("1st dynamic test", () -> Assertions.assertThrows(NullArgumentException.class, () -> templateEngine.generate(Template.builder()
                        .path(SAMPLE_TEMPLATE_HTML)
                        .values(Map.ofEntries(
                                new AbstractMap.SimpleEntry<>("test1", "Test 1")
                        )).build(), Client.builder().addresses(CLIENT_ADDRESSES).build()))),
                dynamicTest("2nd dynamic test", () -> {
                    String output = templateEngine.generate(Template.builder()
                            .path(SAMPLE_TEMPLATE_HTML)
                            .values(Map.ofEntries(
                                    new AbstractMap.SimpleEntry<>("test", "Test ä®"),
                                    new AbstractMap.SimpleEntry<>("test1", "Test 1"),
                                    new AbstractMap.SimpleEntry<>("test2", "Test 2")
                            )).build(), Client.builder().addresses(CLIENT_ADDRESSES).build());

                    Assertions.assertEquals(getExpectedResult(), output);
                })
        );
    }

    private String getExpectedResult() throws IOException {
        var input = StringUtils.class.getResourceAsStream("/test-output/sample-generated-template.html");
        var expected = IOUtils.toString(input, StandardCharsets.UTF_8);

        return expected;
    }
}
