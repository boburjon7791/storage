package com.example.my_mvc_project.dtos.product;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public record ProductCreateDto(
        @NotBlank
        String name,

        @NotNull
        @PositiveOrZero
        Double price,

        @NotNull
        @PositiveOrZero
        Long count,

        @Nullable
        String image,

        @Nullable
        String colors
) {}
