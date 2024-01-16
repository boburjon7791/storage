package com.example.my_mvc_project.mappers;

import com.example.my_mvc_project.dtos.product.ProductCreateDto;
import com.example.my_mvc_project.dtos.product.ProductGetDto;
import com.example.my_mvc_project.dtos.product.ProductUpdateDto;
import com.example.my_mvc_project.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductGetDto toDto(Product product);
    @Mapping(target = "sellings",ignore = true)
    Product toEntity(ProductUpdateDto dto);
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "sellings",ignore = true)
    Product toEntity(ProductCreateDto dto);
}
