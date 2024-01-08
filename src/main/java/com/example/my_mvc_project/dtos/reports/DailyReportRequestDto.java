package com.example.my_mvc_project.dtos.reports;

import jakarta.validation.constraints.Positive;

public record DailyReportRequestDto(
        @Positive
        int year,

        @Positive
        int month
){}
