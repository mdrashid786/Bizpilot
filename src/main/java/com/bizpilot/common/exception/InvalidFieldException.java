package com.bizpilot.common.exception;


public class InvalidFieldException extends RuntimeException {
    public InvalidFieldException(String key) {
        super("Field '" + key + "' is not valid for this business category");
    }
}