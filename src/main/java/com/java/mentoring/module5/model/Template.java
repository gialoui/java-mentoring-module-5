package com.java.mentoring.module5.model;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * @author khangndd
 */
@Data
@Builder
public class Template {
    private String path;
    private Map<String, String> values;
}
