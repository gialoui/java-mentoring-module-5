package com.java.mentoring.module5.utils;

import com.java.mentoring.module5.enums.MessengerMode;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;

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
}
