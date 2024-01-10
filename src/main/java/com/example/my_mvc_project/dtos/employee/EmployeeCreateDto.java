package com.example.my_mvc_project.dtos.employee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EmployeeCreateDto (
        @NotBlank(message = "Ism bo'sh bo'lishi mumkin emas")
        String firstName,

        @NotBlank(message = "Familya bo'sh bo'lishi mumkin emas")
        String lastName,

        @NotBlank(message = "Login bo'sh bo'lishi mumkin emas")
        String username,

        @NotBlank(message = "Parol bo'sh bo'lishi mumkin emas")
        @Size(min = 8,message = "Parol 8 ta belgidan kam bo'lishi mumkin emas")
        String password
){}
