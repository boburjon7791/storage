package com.example.my_mvc_project.services;

import com.example.my_mvc_project.dtos.product.ProductCreateDto;
import com.example.my_mvc_project.dtos.product.ProductGetDto;
import com.example.my_mvc_project.dtos.product.ProductUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductGetDto save(ProductCreateDto dto);
    ProductGetDto update(ProductUpdateDto dto);
    ProductGetDto get(Long id);
    Page<ProductGetDto> products(Pageable pageable);
    Page<ProductGetDto> productsByName(Pageable pageable,String name);
    Page<ProductGetDto> productsByAbout(Pageable pageable,String about);
    Page<ProductGetDto> productsByPrice(Pageable pageable,Double priceStart,Double priceEnd);
    Page<ProductGetDto> productsByCount(Pageable pageable,Long countStart,Long countEnd);

    int updateImage(String saved, long productId);
}
