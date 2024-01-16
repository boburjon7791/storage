package com.example.my_mvc_project.dtos.product;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public record ProductCreateDto(
        @NotBlank(message = "Mahsulot nomi bo'sh bo'lishi mumkin emas")
        String name,

        @NotNull(message = "Mahsulot narxi bo'sh bo'lishi mumkin emas")
        @Positive(message = "Mahsulot narxi 0 katta bo'lishi kerak")
        Double price,

        @NotNull(message = "Mahsulot miqdori bo'sh bo'lishi mumkin emas")
        @PositiveOrZero(message = "Mahsulot miqdori 0 yoki undan katta bo'lishi kerak")
        Long count,

        @Nullable
        String image,

        @Nullable
        String about
) {}
