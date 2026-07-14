package com.bizpilot.common.exception;


public class PhoneAlreadyExistsException extends RuntimeException {

    public PhoneAlreadyExistsException(String phone) {
        super("Phone already registered : " + phone);
    }

}