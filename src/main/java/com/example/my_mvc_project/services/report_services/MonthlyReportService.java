package com.example.my_mvc_project.services.report_services;

import com.example.my_mvc_project.dtos.reports.MonthlyReportDto;

import java.util.List;

public interface MonthlyReportService {
    List<MonthlyReportDto> reportsByYear(int year);

}
