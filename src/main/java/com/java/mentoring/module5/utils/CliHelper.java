package com.java.mentoring.module5.utils;

import com.java.mentoring.module5.enums.MessengerMode;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author khangndd
 */
@Component
public class CliHelper {
    /**
     * From the list of arguments, identify if the program is running in CONSOLE or FILE mode
     *
     * @param args
     * @return
     */
    public MessengerMode identifyMode(final String... args) {
        if (args.length == 0) {
            return MessengerMode.CONSOLE;
        }

        if (args.length == 6 && new HashSet<>(Arrays.stream(args).toList()).containsAll(Constants.ALL_FILE_ARGS)) {
            return MessengerMode.FILE;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public Map<String, String> readParamsFromConsole(InputStream inputStream) {
        return new HashMap<>();
    }
}
