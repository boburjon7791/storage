package com.example.my_mvc_project.dtos.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class ProductGetDto{
    private  Long id;

    private  String name;

    private  Double price;

    private  Long count;

    private  String image;

    private  String about;
}
