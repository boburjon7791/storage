package com.example.my_mvc_project.dtos.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ProductGetDto{
    private  Long id;

    private  String name;

    private  Double price;

    private  Double count;

    private  String image;

    private  String about;
}
