package com.example.my_mvc_project.controller;

import com.example.my_mvc_project.dtos.product.ProductGetDto;
import com.example.my_mvc_project.services.EmployeeService;
import com.example.my_mvc_project.services.ProductService;
import com.example.my_mvc_project.services.SellingService;
import com.example.my_mvc_project.services.report_services.DailyReportService;
import com.example.my_mvc_project.services.report_services.MonthlyReportService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final EmployeeService employeeService;
    private final ProductService productService;
    private final SellingService sellingService;
    private final DailyReportService dailyReportService;
    private final MonthlyReportService monthlyReportService;
    // TODO: 04/01/2024 admin controller
    @GetMapping("/products/list")
    public String list(Model model, @RequestParam(required = false,defaultValue = "0")int page){
        Page<ProductGetDto> products = productService.products(PageRequest.of(page, 10).withSort(Sort.Direction.DESC, "date"));
        model.addAttribute("prods",products);
        return "admin/products";
    }
}
