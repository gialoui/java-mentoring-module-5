package com.java.mentoring.module5.unit.service;

import com.java.mentoring.module5.exception.ArgumentNullException;
import com.java.mentoring.module5.model.Client;
import com.java.mentoring.module5.model.Template;
import com.java.mentoring.module5.utils.TemplateEngine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.AbstractMap;
import java.util.Map;

/**
 * @author khangndd
 */
@ExtendWith(MockitoExtension.class)
class TemplateEngineTest {
    @Spy
    private TemplateEngine templateEngine;

    @Test
    void generateWithValidValues() {
        String output = templateEngine.generate(Template.builder()
                .path("/template/sample-template.html")
                .values(Map.ofEntries(
                        new AbstractMap.SimpleEntry<>("test", "Test ä®"),
                        new AbstractMap.SimpleEntry<>("test1", "Test 1")
                )).build(), Client.builder().addresses("abc@gmail.com;def@gmail.com").build());

        Assertions.assertEquals("\"<html>\\r\\n<head>\\r\\n    <style>\\r\\n\\t\\t<!-- put your css here -->\\r\\n\\r\\n\\r\\n    </style>\\r\\n</head>\\r\\n<body>\\r\\n<div class=\\\"container\\\" style=\\\"padding-top: 100px;\\\">\\r\\n    <div class=\\\"row justify-content-center\\\">\\r\\n        <div class=\\\"col-md-6\\\">\\r\\n            <table class=\\\"table table-bordered\\\">\\r\\n                <thead class=\\\"thead-light\\\">\\r\\n                <tr>\\r\\n                    <th>Name ©¥</th>\\r\\n                    <th>Address</th>\\r\\n                </tr>\\r\\n                </thead>\\r\\n                <tbody>\\r\\n                <tr>\\r\\n                    <td>Test ä®</td>\\r\\n                    <td>Test 1</td>\\r\\n                </tr>\\r\\n                </tbody>\\r\\n            </table>\\r\\n        </div>\\r\\n    </div>\\r\\n</div>\\r\\n</body>\\r\\n</html>\"", output);
    }

    @Test
    void generateWithMissingValue() {
        Assertions.assertThrows(ArgumentNullException.class, () -> templateEngine.generate(Template.builder()
                .path("/template/sample-template.html")
                .values(Map.ofEntries(
                        new AbstractMap.SimpleEntry<>("test1", "Test 1")
                )).build(), Client.builder().addresses("abc@gmail.com;def@gmail.com").build()));
    }

    @Test
    void generateWithRedundantValues() {
        String output = templateEngine.generate(Template.builder()
                .path("/template/sample-template.html")
                .values(Map.ofEntries(
                        new AbstractMap.SimpleEntry<>("test", "Test"),
                        new AbstractMap.SimpleEntry<>("test1", "Test 1"),
                        new AbstractMap.SimpleEntry<>("test2", "Test 2")
                )).build(), Client.builder().addresses("abc@gmail.com;def@gmail.com").build());

        Assertions.assertEquals("\"<html>\\r\\n<head>\\r\\n    <style>\\r\\n\\t\\t<!-- put your css here -->\\r\\n\\r\\n\\r\\n    </style>\\r\\n</head>\\r\\n<body>\\r\\n<div class=\\\"container\\\" style=\\\"padding-top: 100px;\\\">\\r\\n    <div class=\\\"row justify-content-center\\\">\\r\\n        <div class=\\\"col-md-6\\\">\\r\\n            <table class=\\\"table table-bordered\\\">\\r\\n                <thead class=\\\"thead-light\\\">\\r\\n                <tr>\\r\\n                    <th>Name ©¥</th>\\r\\n                    <th>Address</th>\\r\\n                </tr>\\r\\n                </thead>\\r\\n                <tbody>\\r\\n                <tr>\\r\\n                    <td>Test</td>\\r\\n                    <td>Test 1</td>\\r\\n                </tr>\\r\\n                </tbody>\\r\\n            </table>\\r\\n        </div>\\r\\n    </div>\\r\\n</div>\\r\\n</body>\\r\\n</html>\"", output);
    }
}
