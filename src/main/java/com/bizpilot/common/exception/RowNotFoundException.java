package com.bizpilot.common.exception;


public class RowNotFoundException extends RuntimeException {
    public RowNotFoundException(String rowId) {
        super("Row not found with id: " + rowId);
    }
}