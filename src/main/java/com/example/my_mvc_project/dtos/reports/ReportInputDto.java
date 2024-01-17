package com.example.my_mvc_project.dtos.reports;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record ReportInputDto(
        @NotNull(message = "Mahsulot id si bo'sh bo'lmasligi kerak")
        @PositiveOrZero(message = "Mahsulot id si 0 yoki undan katta bo'lishi kerak")
        Long productId,

        @NotNull(message = "Savdo miqdori bo'sh bo'lmasligi kerak")
        @Positive(message = "Savdo miqdori 0 dan katta bo'lishi kerak")
        Long count,

        @NotNull(message = "Savdo narxi bo'sh bo'lmasligi kerak")
        @PositiveOrZero(message = "Savdo narxi 0 yoki undan katta bo'lishi kerak")
        Double price
){}
