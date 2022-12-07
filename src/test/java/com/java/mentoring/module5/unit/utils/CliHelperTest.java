package com.java.mentoring.module5.unit.utils;

import com.java.mentoring.module5.enums.MessengerMode;
import com.java.mentoring.module5.exception.WrongParamsFormatException;
import com.java.mentoring.module5.extensions.EnvironmentExtension;
import com.java.mentoring.module5.extensions.TestExecutionLogExtension;
import com.java.mentoring.module5.utils.CliHelper;
import com.java.mentoring.module5.utils.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author khangndd
 */
@ExtendWith(MockitoExtension.class)
@ExtendWith(EnvironmentExtension.class)
@ExtendWith(TestExecutionLogExtension.class)
class CliHelperTest {
    private static final String RESOURCE_DIR = "src/test/resources";
    private static final Map<String, String> FILE_ARGS = Map.ofEntries(
            new AbstractMap.SimpleEntry<>(Constants.INPUT_FILE_PATH_ARG, RESOURCE_DIR + "/files-to-process/input-template.html"),
            new AbstractMap.SimpleEntry<>(Constants.PARAMS_FILE_PATH_ARG, RESOURCE_DIR + "/files-to-process/params.txt"),
            new AbstractMap.SimpleEntry<>(Constants.OUTPUT_FILE_PATH_ARG, "temp")
    );
    private static final String[] FILE_ARGS_ARRAY = FILE_ARGS.entrySet().stream().flatMap(item -> Stream.of(item.getKey(), item.getValue())).toArray(String[]::new);
    private static final Map<String, String> PARAMS_MAP = Map.ofEntries(
            new AbstractMap.SimpleEntry<>("test", "Test ä®"),
            new AbstractMap.SimpleEntry<>("test1", "Test 1"),
            new AbstractMap.SimpleEntry<>("test2", "Test 2"),
            new AbstractMap.SimpleEntry<>("test3", "Test 3")
    );

    @Spy
    private CliHelper cliHelper;

    private static Stream<Arguments> provideInvalidDataForFileMode() {
        return Stream.of(
                Arguments.of(Constants.INPUT_FILE_PATH_ARG, FILE_ARGS.get(Constants.INPUT_FILE_PATH_ARG), Constants.PARAMS_FILE_PATH_ARG, FILE_ARGS.get(Constants.PARAMS_FILE_PATH_ARG), null, null),
                Arguments.of(Constants.INPUT_FILE_PATH_ARG, FILE_ARGS.get(Constants.INPUT_FILE_PATH_ARG), null, null, null, null),
                Arguments.of(null, null, Constants.PARAMS_FILE_PATH_ARG, FILE_ARGS.get(Constants.PARAMS_FILE_PATH_ARG), null, null),
                Arguments.of(null, null, Constants.PARAMS_FILE_PATH_ARG, FILE_ARGS.get(Constants.PARAMS_FILE_PATH_ARG), Constants.OUTPUT_FILE_PATH_ARG, FILE_ARGS.get(Constants.OUTPUT_FILE_PATH_ARG))
        );
    }

    @Test
    void detectConsoleMode_shouldSuccess() {
        MessengerMode mode = cliHelper.identifyMode();
        Assertions.assertEquals(MessengerMode.CONSOLE, mode);
    }

    @Test
    void detectFileMode_shouldSuccess() {
        MessengerMode mode = cliHelper.identifyMode(FILE_ARGS_ARRAY);
        Assertions.assertEquals(MessengerMode.FILE, mode);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidDataForFileMode")
    void detectFileModeWithInvalidArgs_shouldThrowException(String key1, String value1, String key2, String value2, String key3, String value3) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> cliHelper.identifyMode(Stream.of(key1, value1, key2, value2, key3, value3).filter(Objects::nonNull).toArray(String[]::new)));
    }

    @Test
    void readParamsFromConsole_shouldSuccess() {
        ByteArrayInputStream inputStreamCaptor = buildInputStreamFromMapParams(PARAMS_MAP);
        System.setIn(inputStreamCaptor);

        var result = cliHelper.readParamsFromConsole(System.in);
        Assertions.assertEquals(PARAMS_MAP, result);
    }

    @Test
    void readParamsFromConsole_shouldFailedWithWrongFormat() {
        String inputString = PARAMS_MAP.keySet().stream()
                .map(key -> key + ":" + PARAMS_MAP.get(key))
                .collect(Collectors.joining(System.lineSeparator(), "", System.lineSeparator()));

        ByteArrayInputStream inputStreamCaptor = new ByteArrayInputStream(inputString.getBytes(StandardCharsets.UTF_8));
        System.setIn(inputStreamCaptor);

        Assertions.assertThrows(WrongParamsFormatException.class, () -> cliHelper.readParamsFromConsole(System.in));
    }

    /**
     * @param params
     * @return
     */
    private ByteArrayInputStream buildInputStreamFromMapParams(Map<String, String> params) {
        String inputString = params.keySet().stream()
                .map(key -> key + "=" + params.get(key))
                .collect(Collectors.joining(System.lineSeparator(), "", System.lineSeparator()));

        return new ByteArrayInputStream(inputString.getBytes(StandardCharsets.UTF_8));
    }
}
