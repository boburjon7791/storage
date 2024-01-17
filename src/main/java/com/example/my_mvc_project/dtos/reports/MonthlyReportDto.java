package com.example.my_mvc_project.dtos.reports;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Month;
@Data
@AllArgsConstructor
public class MonthlyReportDto {
    public int year;
    public Month month;
    public Double total;
}
