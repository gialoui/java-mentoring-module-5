package com.java.mentoring.module5;

import com.java.mentoring.module5.enums.MessengerMode;
import com.java.mentoring.module5.model.Client;
import com.java.mentoring.module5.model.Template;
import com.java.mentoring.module5.service.MessengerService;
import com.java.mentoring.module5.utils.CliHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author khangndd
 */
@ExtendWith(MockitoExtension.class)
class Module5ApplicationTest {
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
}
