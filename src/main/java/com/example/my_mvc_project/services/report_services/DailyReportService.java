package com.example.my_mvc_project.services.report_services;

import com.example.my_mvc_project.dtos.reports.DailyReportDto;
import com.example.my_mvc_project.dtos.reports.DailyReportRequestDto;
import jakarta.validation.Valid;

import java.util.List;

public interface DailyReportService {
    List<DailyReportDto> reportsByMonth(@Valid DailyReportRequestDto dto);

}
