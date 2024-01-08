package com.example.my_mvc_project.exceptions;

public class MonthlyReportNotFoundException extends RuntimeException{
    public MonthlyReportNotFoundException(String message) {
        super(message);
    }
}
