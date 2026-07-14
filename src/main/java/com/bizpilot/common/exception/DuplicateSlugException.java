package com.bizpilot.common.exception;

public class DuplicateSlugException extends RuntimeException {

    public DuplicateSlugException(String slug) {
        super("Business slug already exists : " + slug);
    }
}
