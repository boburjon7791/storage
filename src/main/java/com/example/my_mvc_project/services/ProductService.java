package com.example.my_mvc_project.services;

import com.example.my_mvc_project.dtos.product.ProductCreateDto;
import com.example.my_mvc_project.dtos.product.ProductGetDto;
import com.example.my_mvc_project.dtos.product.ProductUpdateDto;
import com.example.my_mvc_project.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    ProductGetDto save(ProductCreateDto dto);
    ProductGetDto update(ProductUpdateDto dto);
    ProductGetDto get(Long id);
    ProductGetDto updateCountById(Long count, Long id);
    Page<ProductGetDto> products(Pageable pageable);
    Page<ProductGetDto> productsByName(Pageable pageable,String name);
    Page<ProductGetDto> productsByColor(Pageable pageable,String color);
    Page<ProductGetDto> productsByPrice(Pageable pageable,Double priceStart,Double priceEnd);
    Page<ProductGetDto> productsByCount(Pageable pageable,Long countStart,Long countEnd);

    void exists(long productId);

    int updateImage(String saved, long productId);

    List<Product> getAllByIds(List<Long> list);
}
