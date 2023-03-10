package com.java.mentoring.module5.utils;

import com.java.mentoring.module5.enums.MessengerMode;
import com.java.mentoring.module5.exception.WrongParamsFormatException;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    /**
     * Reading arguments for FILE mode
     *
     * @param args
     * @return
     */
    public Map<String, String> readArgsInFileMode(final String... args) {
        var argsMap = Constants.ALL_FILE_ARGS.stream()
                .collect(Collectors.toMap(Function.identity(), s -> "temp"));

        for (int i = 0; i < args.length; i++) {
            final int finalI = i;

            if (finalI % 2 == 0) {
                Constants.ALL_FILE_ARGS.stream()
                        .filter(arg -> arg.equals(args[finalI]))
                        .forEach(arg -> argsMap.put(arg, args[finalI + 1]));
            }
        }

        return argsMap;
    }

    /**
     * Reading parameters from console
     *
     * @param inputStream
     * @return
     */
    public Map<String, String> readParamsFromConsole(InputStream inputStream) {
        try (Scanner scanner = new Scanner(inputStream, String.valueOf(StandardCharsets.UTF_8))) {
            System.out.println("Enter parameters for template in format parameterName=parameterValue and enter new parameter in a new line");
            String parameterLine;
            var params = new HashMap<String, String>();
            while (scanner.hasNextLine()) {
                parameterLine = scanner.nextLine();

                // Stop receiving if the next line is empty
                if (parameterLine.isEmpty()) {
                    break;
                }

                final String[] parameterParts = parameterLine.split("=", 2);

                if (parameterParts.length != 2) {
                    throw new WrongParamsFormatException("Parameters format is incorrect");
                }

                params.put(parameterParts[0], parameterParts[1]);
            }

            return params;
        }
    }
}
