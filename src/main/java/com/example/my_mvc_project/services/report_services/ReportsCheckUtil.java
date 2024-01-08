package com.example.my_mvc_project.services.report_services;

import com.example.my_mvc_project.dtos.reports.DailyReportDto;
import com.example.my_mvc_project.dtos.reports.MonthlyReportDto;
import com.example.my_mvc_project.exceptions.DailyReportNotFoundException;
import com.example.my_mvc_project.exceptions.MonthlyReportNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class ReportsCheckUtil {
    public List<DailyReportDto> checkDay(List<DailyReportDto> reports){
        if (reports.isEmpty()) {
            throw new DailyReportNotFoundException("Hisobotlar topilmadi");
        }
        return reports;
    }
    public List<MonthlyReportDto> checkMonth(List<MonthlyReportDto> reports){
        if (reports.isEmpty()) {
            throw new MonthlyReportNotFoundException("Hisobotlar topilmadi");
        }
        return reports;
    }
}
