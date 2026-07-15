package com.bizpilot.common.exception;

public class CategoryDataNotFoundException extends RuntimeException {

    public CategoryDataNotFoundException(Integer sortOrder) {
        super("Category data not found for sort order : " + sortOrder);
    }
}