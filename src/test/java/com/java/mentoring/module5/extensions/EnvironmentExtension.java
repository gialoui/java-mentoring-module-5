package com.java.mentoring.module5.extensions;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * If the active profile is QA, we don't need to execute the tests
 *
 * @author khangndd
 */
@Slf4j
public class EnvironmentExtension implements ExecutionCondition {
    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext extensionContext) {
        Properties props = new Properties();
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream stream = loader.getResourceAsStream("application.properties");
            props.load(stream);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        String env = props.getProperty("env");

        log.info("Current Environment: {}", env);
        if ("qa".equalsIgnoreCase(env)) {
            return ConditionEvaluationResult
                    .disabled("Test disabled on QA environment");
        }

        return ConditionEvaluationResult.enabled(
                "Test enabled on QA environment");
    }
}
