package com.example.my_mvc_project.mappers;

import com.example.my_mvc_project.dtos.reports.SellingDto;
import com.example.my_mvc_project.entities.Selling;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface SellingMapper {
    @Mapping(target = "product",ignore = true)
    @Mapping(target = "firstNameOfEmployee",ignore = true)
    @Mapping(target = "lastNameOfEmployee",ignore = true)
    SellingDto toDto(Selling selling);
}
