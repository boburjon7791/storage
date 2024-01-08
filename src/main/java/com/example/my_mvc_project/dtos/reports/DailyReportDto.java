package com.example.my_mvc_project.dtos.reports;

import com.example.my_mvc_project.enums.MonthCopy;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class DailyReportDto {
   public LocalDateTime date;
   public Double total;
   public MonthCopy month;
}
