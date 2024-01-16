package com.example.my_mvc_project.services;

import com.example.my_mvc_project.dtos.employee.EmployeeCreateDto;
import com.example.my_mvc_project.dtos.employee.EmployeeGetDto;
import com.example.my_mvc_project.dtos.employee.EmployeeUpdateDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public interface EmployeeService {
    EmployeeGetDto save(EmployeeCreateDto dto);
    EmployeeGetDto update(EmployeeUpdateDto dto);
    EmployeeGetDto get(HttpServletRequest id);
    Page<EmployeeGetDto> delete(Long id);
    Page<EmployeeGetDto> employees(Pageable pageable);
    Page<EmployeeGetDto> employeesByName(Pageable pageable,String name);
    EmployeeGetDto changePassword(String oldPassword, String newPassword, String confirmPassword,long id);

    void updateRole(long userId, String role);

    void block(long userId);

    void active(long userId);

    EmployeeGetDto get();

    EmployeeGetDto get(long id);


//    EmployeeGetDto login(String username, String password,HttpServletResponse response);

//    void logout(HttpServletRequest request, HttpServletResponse response);
}
