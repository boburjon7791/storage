package com.example.my_mvc_project.exceptions;

public class BadParamException extends RuntimeException {
    public BadParamException(String message) {
        super(message);
    }
}
