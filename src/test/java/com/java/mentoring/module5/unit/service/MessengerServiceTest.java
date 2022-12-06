package com.java.mentoring.module5.unit.service;

import com.java.mentoring.module5.extensions.EnvironmentExtension;
import com.java.mentoring.module5.extensions.TestExecutionLogExtension;
import com.java.mentoring.module5.model.Client;
import com.java.mentoring.module5.model.Template;
import com.java.mentoring.module5.service.MailServer;
import com.java.mentoring.module5.service.MessengerService;
import com.java.mentoring.module5.utils.Constants;
import com.java.mentoring.module5.utils.TemplateEngine;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author khangndd
 */
@ExtendWith(MockitoExtension.class)
@ExtendWith(EnvironmentExtension.class)
@ExtendWith(TestExecutionLogExtension.class)
class MessengerServiceTest {
    private static final String CLIENT_ADDRESSES = "abc@gmail.com;def@gmail.com";
    private static final String RESOURCE_DIR = "src/test/resources";
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private static final String SAMPLE_TEMPLATE_HTML = new File(RESOURCE_DIR + "/files-to-process/input-template.html").getAbsolutePath();
    private static final String SAMPLE_PARAMS_FILE = new File(RESOURCE_DIR + "/files-to-process/params.txt").getAbsolutePath();

    @TempDir
    File testTempDir;

    @Mock
    TemplateEngine templateEngine;

    @Mock
    MailServer mailServer;

    @InjectMocks
    MessengerService messengerService;

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    /**
     * @return
     */
    private static Stream<Arguments> provideInvalidDataForConsoleMode() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(Client.builder().addresses(CLIENT_ADDRESSES).build(), null),
                Arguments.of(null, Template.builder()
                        .path(SAMPLE_TEMPLATE_HTML)
                        .values(Map.ofEntries(
                                new AbstractMap.SimpleEntry<>("test", "Test ä®"),
                                new AbstractMap.SimpleEntry<>("test1", "Test 1"),
                                new AbstractMap.SimpleEntry<>("test2", "Test 2")
                        )).build())
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidDataForConsoleMode")
    void testSendEmailWithInvalidArgsInConsoleMode(Client client, Template template) {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> messengerService.sendEmailInConsoleMode(client, template));

        Assertions.assertEquals(Constants.ERROR_MISSING_ARGS, exception.getMessage());
    }

    @Test
    void testSendEmailInConsoleModeSuccessfully() {
        // Partial mock
        when(templateEngine.generate(any(Template.class), any(Client.class))).thenCallRealMethod();

        var GENERATED_EMAIL = "Generated email";
        when(mailServer.send(anyString(), anyString())).thenReturn(GENERATED_EMAIL);

        messengerService.sendEmailInConsoleMode(Client.builder().addresses(CLIENT_ADDRESSES).build(), Template.builder()
                .path(SAMPLE_TEMPLATE_HTML)
                .values(Map.ofEntries(
                        new AbstractMap.SimpleEntry<>("test", "Test ä®"),
                        new AbstractMap.SimpleEntry<>("test1", "Test 1"),
                        new AbstractMap.SimpleEntry<>("test2", "Test 2")
                )).build());

        verify(templateEngine).generate(any(Template.class), any(Client.class));
        verify(mailServer).send(anyString(), anyString());
        Assertions.assertEquals(GENERATED_EMAIL, outputStreamCaptor.toString().trim());
    }

    private static Stream<Arguments> provideInvalidDataForFileMode() {
        return Stream.of(
                Arguments.of(null, null, null, null),
                Arguments.of(null, SAMPLE_TEMPLATE_HTML, SAMPLE_PARAMS_FILE, "output-file.html"),
                Arguments.of(Client.builder().addresses(CLIENT_ADDRESSES).build(), null, "files-to-process/params.txt", "output-file.html"),
                Arguments.of(Client.builder().addresses(CLIENT_ADDRESSES).build(), "files-to-process/input-template.html", null, "output-file.html"),
                Arguments.of(Client.builder().addresses(CLIENT_ADDRESSES).build(), "files-to-process/input-template.html", "files-to-process/params.txt", null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidDataForFileMode")
    void testSendEmailWithInvalidArgsInFileMode(Client client, String inputFileName, String paramsFileName, String outputFileName) {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> messengerService.sendEmailInFileMode(client, inputFileName, paramsFileName, outputFileName));

        Assertions.assertEquals(Constants.ERROR_MISSING_ARGS, exception.getMessage());
    }

    @Test
    void testSendEmailInFileModeSuccessfully() throws IOException {
        // Partial mock
        when(templateEngine.generate(any(Template.class), any(Client.class))).thenCallRealMethod();

        var GENERATED_EMAIL = "Generated email";
        when(mailServer.send(anyString(), anyString())).thenReturn(GENERATED_EMAIL);

        var OUTPUT_FILE_PATH = testTempDir.getAbsolutePath() + "/output-file.html";
        messengerService.sendEmailInFileMode(Client.builder().addresses(CLIENT_ADDRESSES).build(), SAMPLE_TEMPLATE_HTML, SAMPLE_PARAMS_FILE, OUTPUT_FILE_PATH);

        File outputFile = new File(OUTPUT_FILE_PATH);
        Assertions.assertTrue(outputFile.exists() && !outputFile.isDirectory());
        Assertions.assertLinesMatch(List.of(GENERATED_EMAIL), Files.readAllLines(outputFile.toPath()));

        verify(templateEngine).generate(any(Template.class), any(Client.class));
        verify(mailServer).send(anyString(), anyString());
    }
}
