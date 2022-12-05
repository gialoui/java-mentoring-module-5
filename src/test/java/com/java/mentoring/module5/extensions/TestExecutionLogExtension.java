package com.java.mentoring.module5.extensions;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * @author khangndd
 */
public class TestExecutionLogExtension implements BeforeEachCallback, AfterEachCallback {
    static Logger logger = Logger.getLogger(TestExecutionLogExtension.class.getName());

    static {
        FileHandler fileHandler;
        try {
            fileHandler = new FileHandler("test.log", true);

            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        logger.addHandler(fileHandler);
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        logger.info("After executing " + extensionContext.getTestClass().get().getName() + "." + extensionContext.getDisplayName());
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        logger.info("Before executing " + extensionContext.getTestClass().get().getName() + "." + extensionContext.getDisplayName());
    }
}
