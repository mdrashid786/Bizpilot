package com.bizpilot.common.exception;


public class BusinessNotFoundException extends RuntimeException {

    public BusinessNotFoundException(String slug) {
        super("Business not found with slug : " + slug);
    }

}