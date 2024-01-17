package com.example.my_mvc_project.dtos.product;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductUpdateDto(
        @NotNull(message = "id bo'sh bo'lmasligi kerak")
        @PositiveOrZero(message = "id 0 yoki undan katta bo'lishi kerak")
        Long id,

        @NotBlank(message = "Mahsulot nomi bo'sh bo'lmasligi kerak")
        String name,

        @NotNull(message = "Mahsulot miqdori bo'sh bo'lmasligi kerak")
        @PositiveOrZero(message = "Mahsulot miqdori 0 yoki undan katta bo'lishi kerak")
        Long count,

        @NotNull(message = "Mahsulot narxi bo'sh bo'lmasligi kerak")
        @Positive(message = "Mahsulot narxi 0 dan katta bo'lishi kerak")
        Double price,

        @Nullable
        String image,

        @Nullable
        String about
) {}
