package com.java.mentoring.module5.utils;

import com.java.mentoring.module5.enums.MessengerMode;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;

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
        try (Scanner scanner = new Scanner(inputStream, String.valueOf(StandardCharsets.UTF_8))) {
            System.out.format("Enter parameters for template in format parameterName=parameterValue%n"
                    + "end entering parameters with new line %n");
            String parameterLine;
            var params = new HashMap<String, String>();
            while (scanner.hasNextLine()) {
                parameterLine = scanner.nextLine();

                // Stop receiving if the next line is empty
                if (parameterLine.isEmpty()) {
                    break;
                }

                final String[] parameterParts = parameterLine.split("=", 2);
                params.put(parameterParts[0], parameterParts[1]);
            }

            return params;
        }
    }
}
