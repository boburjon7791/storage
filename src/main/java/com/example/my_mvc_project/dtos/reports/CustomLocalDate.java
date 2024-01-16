package com.example.my_mvc_project.dtos.reports;

import com.example.my_mvc_project.enums.MonthCopy;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomLocalDate {
    public int year;
    public MonthCopy month;
    public int dayOfMonth;
}
