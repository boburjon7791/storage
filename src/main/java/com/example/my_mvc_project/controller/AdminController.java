package com.example.my_mvc_project.controller;

import com.example.my_mvc_project.dtos.employee.EmployeeGetDto;
import com.example.my_mvc_project.dtos.reports.CustomLocalDate;
import com.example.my_mvc_project.dtos.reports.SoldPersonDaily;
import com.example.my_mvc_project.dtos.reports.SoldPersonMonthly;
import com.example.my_mvc_project.entities.Role;
import com.example.my_mvc_project.enums.MonthCopy;
import com.example.my_mvc_project.services.EmployeeService;
import com.example.my_mvc_project.services.SellingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/manager")
@PreAuthorize("hasAnyRole('SUPER_MANAGER','MANAGER')")
public class AdminController {
    private final EmployeeService employeeService;
    private final SellingService sellingService;
    @Value(value = "${pages.size}")
    private Integer pagesSize;

    public void add(Model model){
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
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/get-total/sells-daily")
    public String getTotalDaily(Model model, @RequestParam(required = false) LocalDate date){
        List<SoldPersonDaily> dailyReport = sellingService.dailyReport(Objects.requireNonNullElse(date,LocalDate.now()));
        Set<Integer> pages=new TreeSet<>();
        model.addAttribute("pages",pages);
        model.addAttribute("dailyReport",dailyReport);
        BigDecimal totalSumma = dailyReport.stream()
                .map(soldPersonDaily -> new BigDecimal(soldPersonDaily.getSumma()))
                .peek(System.out::println)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.valueOf(0));
        model.addAttribute("totalSumma",totalSumma);
        date=Objects.requireNonNullElse(date,LocalDate.now());
        CustomLocalDate customLocalDate = new CustomLocalDate(
                date.getYear(),
                MonthCopy.intValue(date.getMonthValue()),
                date.getDayOfMonth()
        );
        model.addAttribute("date", customLocalDate);
        model.addAttribute("dateC", date);
        return "admin/daily";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/get-total/sells-monthly")
    public String getTotalMonthly(Model model, @RequestParam(required = false,defaultValue = "0") int year){
        if (year==0) {
            year=LocalDate.now().getYear();
        }
        List<SoldPersonMonthly> monthlyReport = sellingService.monthlyReport(year);
        BigDecimal totalSumma = monthlyReport.stream()
                .map(soldPersonMonthly -> new BigDecimal(soldPersonMonthly.totalSumma))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.valueOf(0));
        model.addAttribute("totalSumma",totalSumma);
        model.addAttribute("monthlyReport",monthlyReport);
        add(model);
        model.addAttribute("year",year);
        return "admin/monthly";
    }

    @GetMapping("/employees")
    public String list(Model model, @RequestParam(required = false,defaultValue = "0")int page){
        Page<EmployeeGetDto> employees = employeeService.employees
                (PageRequest.of(page, pagesSize,
                        Sort.by(Sort.Direction.DESC, "role")
                                .and(Sort.by(Sort.Direction.ASC, "firstName", "lastName", "username"))));
        model.addAttribute("employees",employees);
        Role[] roles = Role.values();
        Set<Integer> pages=new TreeSet<>();
        for (int i = 0; i < employees.getTotalPages(); i++) {
            pages.add(i);
        }
        model.addAttribute("pages",pages);
        model.addAttribute("roles",roles);
        return "admin/employees";
    }
    @PreAuthorize("hasRole('SUPER_MANAGER')")
    @PostMapping("/update/role")
    public String updateRole(@RequestParam(name = "u_id") long userId,@RequestParam String role,Model model){
        employeeService.updateRole(userId,role);
        Page<EmployeeGetDto> employees = employeeService.employees
                (PageRequest.of(0, pagesSize,
                        Sort.by(Sort.Direction.DESC, "role")
                                .and(Sort.by(Sort.Direction.ASC, "firstName", "lastName", "username"))));
        model.addAttribute("employees",employees);
        Role[] roles = Role.values();
        Set<Integer> pages=new TreeSet<>();
        for (int i = 0; i < employees.getTotalPages(); i++) {
            pages.add(i);
        }
        model.addAttribute("pages",pages);
        model.addAttribute("roles",roles);
        return "admin/employees";
    }
    @PreAuthorize("hasAnyRole('SUPER_MANAGER','MANAGER')")
    @PostMapping("/block")
    public String block(@RequestParam(name = "u_id") long userId,Model model){
        employeeService.block(userId);
        Page<EmployeeGetDto> employees = employeeService.employees
                (PageRequest.of(0, pagesSize,
                        Sort.by(Sort.Direction.DESC, "role")
                                .and(Sort.by(Sort.Direction.ASC, "firstName", "lastName", "username"))));
        model.addAttribute("employees",employees);
        Role[] roles = Role.values();
        Set<Integer> pages=new TreeSet<>();
        for (int i = 0; i < employees.getTotalPages(); i++) {
            pages.add(i);
        }
        model.addAttribute("pages",pages);
        model.addAttribute("roles",roles);
        return "admin/employees";
    }
    @PreAuthorize("hasAnyRole('SUPER_MANAGER','MANAGER')")
    @PostMapping("/active")
    public String unblock(@RequestParam(name = "u_id") long userId, Model model){
        employeeService.active(userId);
        Page<EmployeeGetDto> employees = employeeService.employees
                (PageRequest.of(0, pagesSize,
                        Sort.by(Sort.Direction.DESC, "role")
                                .and(Sort.by(Sort.Direction.ASC, "firstName", "lastName", "username"))));
        model.addAttribute("employees",employees);
        Role[] roles = Role.values();
        Set<Integer> pages=new TreeSet<>();
        for (int i = 0; i < employees.getTotalPages(); i++) {
            pages.add(i);
        }
        model.addAttribute("pages",pages);
        model.addAttribute("roles",roles);
        return "admin/employees";
    }
    @GetMapping("/search")
    public String search(Model model,@RequestParam String name){
        Page<EmployeeGetDto> employees = employeeService.employeesByName(PageRequest.of(0, pagesSize), name);
        model.addAttribute("employees",employees);
        Role[] roles = Role.values();
        Set<Integer> pages=new TreeSet<>();
        for (int i = 0; i < employees.getTotalPages(); i++) {
            pages.add(i);
        }
        model.addAttribute("pages",pages);
        model.addAttribute("roles",roles);
        return "admin/employees";
    }
    @GetMapping("/get/{id}")
    public String search(Model model,@PathVariable Long id){
        EmployeeGetDto getDto = employeeService.get(id);
        Page<EmployeeGetDto> employees=new PageImpl<>(List.of(getDto),PageRequest.of(0,1),1);
        model.addAttribute("employees",employees);
        Role[] roles = Role.values();
        Set<Integer> pages=new TreeSet<>();
        for (int i = 0; i < employees.getTotalPages(); i++) {
            pages.add(i);
        }
        model.addAttribute("pages",pages);
        model.addAttribute("roles",roles);
        return "admin/employees";
    }
}
