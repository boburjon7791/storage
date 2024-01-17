package com.example.my_mvc_project.controller;

import com.example.my_mvc_project.dtos.product.ProductCreateDto;
import com.example.my_mvc_project.dtos.product.ProductGetDto;
import com.example.my_mvc_project.dtos.product.ProductUpdateDto;
import com.example.my_mvc_project.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.TreeSet;

@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    @Value(value = "${pages.size}")
    private Integer pageSize;

    @GetMapping("/save")
    @PreAuthorize("hasAnyRole('SUPER_MANAGER','MANAGER')")
    public String save(){
        return "product/save1";
    }
    @PostMapping("/save2")
    @PreAuthorize("hasAnyRole('SUPER_MANAGER','MANAGER')")
    public String save(@ModelAttribute @Valid ProductCreateDto dto, Model model){
        ProductGetDto saved = productService.save(dto);
        System.out.println("dto.image() = " + dto.image());
        model.addAttribute("prod",saved);
        System.out.println("saved.getImage() = " + saved.getImage());
        return "product/about";
    }
    @GetMapping("/about")
    public String about(@RequestParam long id,Model model){
        ProductGetDto dto = productService.get(id);
        model.addAttribute("prod",dto);
        return "product/about";
    }
    @PreAuthorize("hasAnyRole('SUPER_MANAGER','MANAGER')")
    @PostMapping("/update")
    public String update(@Valid @ModelAttribute ProductUpdateDto dto,Model model){
        ProductGetDto updated = productService.update(dto);
        model.addAttribute("prod",updated);
        return "product/about";
    }
    @GetMapping("/get/{id}")
    public String get(@PathVariable long id,Model model){
        ProductGetDto dto = productService.get(id);
        model.addAttribute("prod",dto);
        return "product/about";
    }
    @GetMapping("/list")
    public String list(@RequestParam(required = false,defaultValue = "0")int page,Model model){
        Page<ProductGetDto> products = productService.products
                (PageRequest.of(page, pageSize));
        model.addAttribute("prods",products);
        Set<Integer> pages=new TreeSet<>();
        for (int i = 0; i < products.getTotalPages(); i++) {
            pages.add(i);
        }
        System.out.println("products.getNumber() = " + products.getNumber());
        model.addAttribute("pages",pages);
        return "product/products";
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list-by-name")
    public String listByName(
            @RequestParam(defaultValue = "0")int page,
            @RequestParam String name,Model model
            ){
        Page<ProductGetDto> products = productService.productsByName(PageRequest.of(page, pageSize), name);
        model.addAttribute("prods",products);
        Set<Integer> pages=new TreeSet<>();
        for (int i = 0; i < products.getTotalPages(); i++) {
            pages.add(i);
        }
        System.out.println("products.getNumber() = " + products.getNumber());
        model.addAttribute("pages",pages);
        return "product/products";
    }
}
