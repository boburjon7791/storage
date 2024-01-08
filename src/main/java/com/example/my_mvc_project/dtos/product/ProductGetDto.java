package com.example.my_mvc_project.dtos.product;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ProductGetDto{
    private  Long id;

    private  String name;

    private  Double price;

    private  Long count;

    private  String image;

    private  LocalDateTime dateTime;

    private  String colors;
}
