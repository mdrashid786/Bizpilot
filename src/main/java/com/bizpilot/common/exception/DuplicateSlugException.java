package com.bizpilot.common.exception;

public class DuplicateSlugException extends RuntimeException {

    public DuplicateSlugException(String slug) {
        super("Same Business name is already exists : " + slug);
    }
}
