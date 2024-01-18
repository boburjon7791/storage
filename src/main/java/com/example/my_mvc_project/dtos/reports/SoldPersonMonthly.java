package com.example.my_mvc_project.dtos.reports;

import com.example.my_mvc_project.dtos.employee.EmployeeGetDto;
import com.example.my_mvc_project.enums.MonthCopy;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.text.DecimalFormat;

@Data
@AllArgsConstructor
public class SoldPersonMonthly{
      public EmployeeGetDto employee;
      public String totalSumma;
      public MonthCopy month;
}
