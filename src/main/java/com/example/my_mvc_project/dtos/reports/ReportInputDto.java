package com.example.my_mvc_project.dtos.reports;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ReportInputDto(
        @NotNull
        @PositiveOrZero
        Long productId,

        @NotNull
        @PositiveOrZero
        Long count,

        @Nullable
        @PositiveOrZero
        Double price
){}
