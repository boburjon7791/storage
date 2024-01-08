package com.example.my_mvc_project.dtos.product;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public record ProductUpdateDto(
        @NotNull
        @PositiveOrZero
        Long id,

        @NotBlank
        String name,

        @NotNull
        Long count,

        @NotNull
        @PositiveOrZero
        Double price,

        @Nullable
        String image,

        @Nullable
        String colors
) {}
