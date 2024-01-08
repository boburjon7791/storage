package com.example.my_mvc_project.exceptions;

public class DailyReportNotFoundException extends RuntimeException{
    public DailyReportNotFoundException(String message) {
        super(message);
    }
}
