package com.example.my_mvc_project.dtos.reports;

import com.example.my_mvc_project.dtos.employee.EmployeeGetDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.text.DecimalFormat;

@Data
@AllArgsConstructor
public class SoldPersonDaily{
       public EmployeeGetDto employee;
       public String summa;
}
