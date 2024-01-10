package com.example.my_mvc_project.dtos.employee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record EmployeeUpdateDto(
        @NotNull(message = "id bo'sh bo'lishi mumkin emas")
        @PositiveOrZero(message = "id 0 yoki undan katta bo'lishi kerak")
        Long id,

        @NotBlank(message = "Ism bo'sh bo'lishi mumkin emas")
        String firstName,

        @NotBlank(message = "Familya bo'sh bo'lishi mumkin emas")
        String lastName,

        @NotBlank(message = "Login bo'sh bo'lishi mumkin emas")
        String username
){}
