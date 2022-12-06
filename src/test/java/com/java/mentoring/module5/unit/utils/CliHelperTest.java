package com.java.mentoring.module5.unit.utils;

import com.java.mentoring.module5.enums.MessengerMode;
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

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
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

    @Spy
    private CliHelper cliHelper;

    private static Stream<Arguments> provideInvalidDataForFileMode() {
        return Stream.of(
                Arguments.of(Constants.INPUT_FILE_PATH_ARG, FILE_ARGS.get(Constants.INPUT_FILE_PATH_ARG), Constants.PARAMS_FILE_PATH_ARG, FILE_ARGS.get(Constants.PARAMS_FILE_PATH_ARG), null, null),
                Arguments.of(Constants.INPUT_FILE_PATH_ARG, FILE_ARGS.get(Constants.INPUT_FILE_PATH_ARG), null, null, null, null),
                Arguments.of(null, null, null, null, null, null)
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
}
