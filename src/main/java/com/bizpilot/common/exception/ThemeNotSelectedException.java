package com.bizpilot.common.exception;

public class ThemeNotSelectedException extends RuntimeException {
    public ThemeNotSelectedException() {
        super("Please select a template before publishing your website");
    }
}