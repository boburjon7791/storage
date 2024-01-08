package com.example.my_mvc_project.controller;

import com.example.my_mvc_project.dtos.employee.EmployeeGetDto;
import com.example.my_mvc_project.dtos.product.ProductGetDto;
import com.example.my_mvc_project.entities.Role;
import com.example.my_mvc_project.services.EmployeeService;
import com.example.my_mvc_project.services.ProductService;
import com.example.my_mvc_project.services.SellingService;
import com.example.my_mvc_project.services.report_services.DailyReportService;
import com.example.my_mvc_project.services.report_services.MonthlyReportService;
import lombok.AllArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;
import java.util.TreeSet;

@Controller
@AllArgsConstructor
@RequestMapping("/manager")
@PreAuthorize("hasRole('MANAGER')")
public class AdminController {
    private final EmployeeService employeeService;
    private final ProductService productService;
    private final SellingService sellingService;
    private final DailyReportService dailyReportService;
    private final MonthlyReportService monthlyReportService;
    // TODO: 04/01/2024 admin controller
    @GetMapping("/employees")
    public String list(Model model, @RequestParam(required = false,defaultValue = "0")int page){
        Page<EmployeeGetDto> employees = employeeService.employees
                (PageRequest.of(page, 5,
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
    @PostMapping("/update/role")
    public String updateRole(@RequestParam(name = "u_id") long userId,@RequestParam String role,Model model){
        employeeService.updateRole(userId,role);
        Page<EmployeeGetDto> employees = employeeService.employees
                (PageRequest.of(0, 5,
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
    @PostMapping("/block")
    public String block(@RequestParam(name = "u_id") long userId,Model model){
        employeeService.block(userId);
        Page<EmployeeGetDto> employees = employeeService.employees
                (PageRequest.of(0, 5,
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
    @PostMapping("/active")
    public String unblock(@RequestParam(name = "u_id") long userId, Model model){
        employeeService.active(userId);
        Page<EmployeeGetDto> employees = employeeService.employees
                (PageRequest.of(0, 5,
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
        Page<EmployeeGetDto> employees = employeeService.employeesByName(PageRequest.of(0, 5), name);
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
