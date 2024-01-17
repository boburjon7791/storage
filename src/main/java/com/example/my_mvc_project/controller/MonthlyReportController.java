package com.example.my_mvc_project.controller;

import com.example.my_mvc_project.dtos.reports.MonthlyReportDto;
import com.example.my_mvc_project.exceptions.NotFoundException;
import com.example.my_mvc_project.services.report_services.MonthlyReportService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@PreAuthorize("hasAnyRole('SUPER_MANAGER','MANAGER')")
@RequestMapping("/monthly/report")
public class MonthlyReportController {
    private final MonthlyReportService monthlyReportService;
    @GetMapping
    public String get1(Model model){
        List<MonthlyReportDto> reports = monthlyReportService.reportsByYear(LocalDate.now().getYear());
        add(model, reports);
        return "reports/monthly";
    }
    private void add(Model model,List<MonthlyReportDto> dto){
        List<LocalDate> localDates=new LinkedList<>();
        LocalDate start = LocalDate.of(Year.now().minusYears(10).getValue(), 1, 1);
        while (!start.isAfter(LocalDate.now())) {
            localDates.add(start);
            start = start.plusDays(1);
        }
        Set<Integer> years = localDates.stream()
                .map(LocalDate::getYear)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toCollection(LinkedHashSet::new));
        model.addAttribute("years",years);
        if (dto.isEmpty()) {
            throw new NotFoundException("Hisobotlar topilmadi");
        }
        Double totalSumma = dto.stream()
                .map(reportDto -> reportDto.total)
                .reduce(Double::sum).orElse(0d);
        model.addAttribute("total",totalSumma);
        model.addAttribute("reports",dto);
    }
    @GetMapping("/get")
    public String byTwoDates(@RequestParam int year, Model model){
        List<MonthlyReportDto> reports = monthlyReportService.reportsByYear(year);
        add(model,reports);
        return "reports/monthly";
    }
    @GetMapping("/get-year/{year}")
    public String byMonthName(Model model, @PathVariable Integer year){
        List<MonthlyReportDto> reports = monthlyReportService.reportsByYear(year);
        add(model,reports);
        return "reports/monthly";
    }
}
