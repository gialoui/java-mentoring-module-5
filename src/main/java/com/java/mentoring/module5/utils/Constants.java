package com.java.mentoring.module5.utils;

import lombok.experimental.UtilityClass;

import java.util.List;

/**
 * @author khangndd
 */
@UtilityClass
public class Constants {
    public static final String ERROR_MISSING_ARGS = "Required args should not be null";
    public static final String INPUT_FILE_PATH_ARG = "--input-file";
    public static final String PARAMS_FILE_PATH_ARG = "--params-file";
    public static final String OUTPUT_FILE_PATH_ARG = "--output-file";
    public static final List<String> ALL_FILE_ARGS = List.of(INPUT_FILE_PATH_ARG, PARAMS_FILE_PATH_ARG, OUTPUT_FILE_PATH_ARG);
}
