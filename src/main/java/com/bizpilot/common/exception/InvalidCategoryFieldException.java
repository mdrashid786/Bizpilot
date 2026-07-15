package com.bizpilot.common.exception;

public class InvalidCategoryFieldException extends RuntimeException {

    public InvalidCategoryFieldException(String fieldKey) {
        super("Invalid field : " + fieldKey);
    }
}