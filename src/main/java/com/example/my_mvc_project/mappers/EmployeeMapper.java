package com.example.my_mvc_project.mappers;

import com.example.my_mvc_project.dtos.employee.EmployeeCreateDto;
import com.example.my_mvc_project.dtos.employee.EmployeeGetDto;
import com.example.my_mvc_project.dtos.employee.EmployeeUpdateDto;
import com.example.my_mvc_project.entities.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface EmployeeMapper {
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "accountNonLocked",ignore = true)
    @Mapping(target = "role",ignore = true)
    Employee toEntity(EmployeeCreateDto dto);

    @Mapping(target = "accountNonLocked",ignore = true)
    @Mapping(target = "role",ignore = true)
    @Mapping(target = "password",ignore = true)
    Employee toEntity(EmployeeUpdateDto dto);

    @Mapping(target = "active",source = "accountNonLocked")
    EmployeeGetDto toDto(Employee employee);
}
