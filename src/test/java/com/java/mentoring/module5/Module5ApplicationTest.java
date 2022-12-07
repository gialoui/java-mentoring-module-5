package com.java.mentoring.module5;

import com.java.mentoring.module5.enums.MessengerMode;
import com.java.mentoring.module5.model.Client;
import com.java.mentoring.module5.model.Template;
import com.java.mentoring.module5.service.MessengerService;
import com.java.mentoring.module5.utils.CliHelper;
import com.java.mentoring.module5.utils.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author khangndd
 */
@ExtendWith(MockitoExtension.class)
class Module5ApplicationTest {
    private static final String RESOURCE_DIR = "src/test/resources";
    private static final Map<String, String> FILE_ARGS = Map.ofEntries(
            new AbstractMap.SimpleEntry<>(Constants.INPUT_FILE_PATH_ARG, RESOURCE_DIR + "/files-to-process/input-template.html"),
            new AbstractMap.SimpleEntry<>(Constants.PARAMS_FILE_PATH_ARG, RESOURCE_DIR + "/files-to-process/params.txt"),
            new AbstractMap.SimpleEntry<>(Constants.OUTPUT_FILE_PATH_ARG, "temp")
    );
    private static final String[] FILE_ARGS_ARRAY = FILE_ARGS.entrySet().stream().flatMap(item -> Stream.of(item.getKey(), item.getValue())).toArray(String[]::new);

    @Mock
    MessengerService messengerService;

    @Mock
    CliHelper cliHelper;

    @InjectMocks
    Module5Application application;

    @Test
    void runInConsoleMode_shouldSuccess() {
        when(cliHelper.identifyMode()).thenReturn(MessengerMode.CONSOLE);
        when(cliHelper.readParamsFromConsole(any(InputStream.class))).thenReturn(new HashMap<>());
        doNothing().when(messengerService).sendEmailInConsoleMode(any(Client.class), any(Template.class));

        application.run();

        verify(cliHelper).identifyMode();
        verify(cliHelper).readParamsFromConsole(any(InputStream.class));
        verify(messengerService).sendEmailInConsoleMode(any(Client.class), any(Template.class));
    }

    @Test
    void runInFileMode_shouldSuccess() throws IOException {
        when(cliHelper.identifyMode(any(String[].class))).thenReturn(MessengerMode.FILE);
        when(cliHelper.readArgsInFileMode(any(String[].class))).thenReturn(new HashMap<>());
        doNothing().when(messengerService).sendEmailInFileMode(any(Client.class), anyString(), anyString(), anyString());

        application.run(FILE_ARGS_ARRAY);

        verify(cliHelper).identifyMode();
        verify(cliHelper).readArgsInFileMode(any(String[].class));
        verify(messengerService).sendEmailInFileMode(any(Client.class), anyString(), anyString(), anyString());
    }
}
