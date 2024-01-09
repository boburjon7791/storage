package com.example.my_mvc_project.controller;

import com.example.my_mvc_project.dtos.reports.DailyReportDto;
import com.example.my_mvc_project.dtos.reports.DailyReportRequestDto;
import com.example.my_mvc_project.enums.MonthCopy;
import com.example.my_mvc_project.exceptions.NotFoundException;
import com.example.my_mvc_project.services.report_services.DailyReportService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@RequestMapping("/daily/report")
@PreAuthorize("hasAnyRole('SUPER_MANAGER','MANAGER')")
public class DailyReportController {
    private final DailyReportService dailyReportService;

    @GetMapping
    public String get1(Model model){
        List<DailyReportDto> reports = dailyReportService.reportsByMonth(
                new DailyReportRequestDto(LocalDate.now().getYear(), LocalDate.now().getMonthValue()));
        add(model, reports);
        return "reports/daily";
    }
    private void add(Model model,List<DailyReportDto> dto){
        model.addAttribute("months", MonthCopy.values());
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
    public String byTwoDates(@Valid @ModelAttribute DailyReportRequestDto dto, Model model){
        List<DailyReportDto> reports = dailyReportService.reportsByMonth(dto);
        add(model,reports);
        return "reports/daily";
    }
    @GetMapping("/get-month/{month}/{year}")
    public String byMonthName(Model model,@PathVariable Integer year, @PathVariable Integer month){
        List<DailyReportDto> reports = dailyReportService.reportsByMonth(new DailyReportRequestDto(year, month));
        add(model,reports);
        return "reports/daily";
    }
}
