package com.example.my_mvc_project.controller;

import com.example.my_mvc_project.dtos.product.ProductGetDto;
import com.example.my_mvc_project.dtos.reports.SellingDto;
import com.example.my_mvc_project.entities.Basket;
import com.example.my_mvc_project.services.ProductService;
import com.example.my_mvc_project.services.SellingService;
import com.example.my_mvc_project.utils.BaseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


@Controller
@RequiredArgsConstructor
@RequestMapping("/selling")
public class SellingsController {
    private final SellingService sellingService;
    private final ProductService productService;
    @Value(value = "${pages.size}")
    private Integer pageSize;
    @GetMapping("/get-basket")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public String getBasket(Model model){
        Basket basket = sellingService.getBasket();
        model.addAttribute("basket",basket);
        return "selling/basket";
    }

    @PostMapping("/update-basket")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public String updateBasket(Model model,@RequestParam long productId,@RequestParam long count){
        Basket basket = sellingService.putToBasket(productId, count);
        model.addAttribute("basket",basket);
        return "selling/basket";
    }

    @PostMapping("/remove-product/basket")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public String updateBasket(Model model,@RequestParam long productId){
        Basket basket = sellingService.removeProductFromBasket(productId);
        model.addAttribute("basket",basket);
        return "selling/basket";
    }

    @PostMapping("/clear-basket")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public String updateBasket(Model model){
        sellingService.clearBasket();
        Page<ProductGetDto> products=productService.products(PageRequest.of(0,pageSize));
        model.addAttribute("prods",products);
        int pages = products.getTotalPages();
        List<Integer> pagesList=new LinkedList<>();
        for (int i = 0; i < pages; i++) {
            pagesList.add(i);
        }
        model.addAttribute("pages", pagesList);
        return "selling/select";
    }

    @PostMapping("/start-selling")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public String startSelling(Model model){
        Page<SellingDto> sellings = sellingService.startSelling();
        sellingService.clearBasket();
        model.addAttribute("sells",sellings);
        AtomicReference<String> date= new AtomicReference<>("Kuni yozilmagan");
        sellings.stream()
                .peek(System.out::println)
                .findAny()
                .ifPresent(sellingDto -> {
                    String string = sellingDto.getDateTime().toLocalDate().toString();
                    System.out.println("string = " + string);
                    date.set(string);
                });
        List<Integer> pages=new LinkedList<>();
        for (int i = 0; i < sellings.getTotalPages(); i++) {
            pages.add(i);
        }
        model.addAttribute("now", LocalDate.now());
        model.addAttribute("pages",pages);
        model.addAttribute("date",date);
        return "selling/get";
    }
    @GetMapping("/save1")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public String save1(Model model,@RequestParam(required = false,defaultValue = "0") int page){
        Page<ProductGetDto> products=productService.products(PageRequest.of(page,pageSize));
        model.addAttribute("prods",products);
        int pages = products.getTotalPages();
        List<Integer> pagesList=new LinkedList<>();
        for (int i = 0; i < pages; i++) {
            pagesList.add(i);
        }
        model.addAttribute("pages", pagesList);
        return "selling/select";
    }
    @GetMapping("/save1/by-name")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public String save1(Model model,@RequestParam(required = false)String name){
        Page<ProductGetDto> products = productService.productsByName(PageRequest.of(0, pageSize),name);
        model.addAttribute("prods",products);
        int pages = products.getTotalPages();
        List<Integer> pagesList=new LinkedList<>();
        for (int i = 0; i < pages; i++) {
            pagesList.add(i);
        }
        model.addAttribute("pages", pagesList);
        return "selling/select";
    }

    @PostMapping("/select")
    public String save(@RequestParam long productId,@RequestParam long count,Model model){
        Basket basket = sellingService.putToBasket(productId, count);
        model.addAttribute("basket",basket);
        Page<ProductGetDto> products=productService.products(PageRequest.of(0,pageSize));
        model.addAttribute("prods",products);
        int pages = products.getTotalPages();
        List<Integer> pagesList=new LinkedList<>();
        for (int i = 0; i < pages; i++) {
            pagesList.add(i);
        }
        model.addAttribute("pages", pagesList);
        return "selling/select";
    }
    @GetMapping("/get-date/{date}")
    public String getByDate(@PathVariable LocalDate date,Model model){
        Page<SellingDto> sellings = sellingService.sellingsByDate(
                date, PageRequest.of(0, pageSize, Sort.by("dateTime").descending()));
        model.addAttribute("sells",sellings);
        AtomicReference<String> atomicDate= new AtomicReference<>("Kuni yozilmagan");
        sellings.stream()
                .peek(System.out::println)
                .findAny()
                .ifPresent(sellingDto -> {
                    String string = sellingDto.getDateTime().toLocalDate().toString();
                    System.out.println("string = " + string);
                    atomicDate.set(string);
                });
        List<Integer> pages=new LinkedList<>();
        for (int i = 0; i < sellings.getTotalPages(); i++) {
            pages.add(i);
        }
        model.addAttribute("now", date);
        model.addAttribute("pages",pages);
        model.addAttribute("date",atomicDate);
        return "selling/get";
    }
    @GetMapping("/get-list")
    public String list(@RequestParam(required = false,defaultValue = "0") int page,Model model){
        Page<SellingDto> sellings = sellingService.sellingsByDate(LocalDate.now(),PageRequest.of(page, pageSize,Sort.by("dateTime").descending())
                .withSort(Sort.Direction.DESC,"dateTime"));
        model.addAttribute("sells",sellings);
        AtomicReference<String> date= new AtomicReference<>("Kuni yozilmagan");
        sellings.stream()
                .peek(System.out::println)
                .findAny()
                .ifPresent(sellingDto -> {
                    String string = sellingDto.getDateTime().toLocalDate().toString();
                    System.out.println("string = " + string);
                    date.set(string);
                });
        List<Integer> pages=new LinkedList<>();
        for (int i = 0; i < sellings.getTotalPages(); i++) {
            pages.add(i);
        }
        model.addAttribute("now", LocalDate.now());
        model.addAttribute("pages",pages);
        model.addAttribute("date",date);
        return "selling/get";
    }
    @GetMapping("/list-by-date")
    public String list(
            @RequestParam(defaultValue = "0",required = false)int page,
            @RequestParam(name = "date") LocalDate dateTime,Model model
    ){
        Page<SellingDto> sellings = sellingService.
                sellingsByDate(dateTime, PageRequest.of
                        (page, pageSize, Sort.by("dateTime").descending()));
        model.addAttribute("sells",sellings);
        AtomicReference<String> date= new AtomicReference<>("Kuni yozilmagan");
        sellings.stream()
                .peek(System.out::println)
                .findAny()
                .ifPresent(sellingDto -> {
                    String string = sellingDto.getDateTime().toLocalDate().toString();
                    System.out.println("string = " + string);
                    date.set(string);
                });
        List<Integer> pages=new LinkedList<>();
        for (int i = 0; i < sellings.getTotalPages(); i++) {
            pages.add(i);
        }
        model.addAttribute("now", dateTime);
        model.addAttribute("pages",pages);
        model.addAttribute("date",date);
        return "selling/get";
    }
}
