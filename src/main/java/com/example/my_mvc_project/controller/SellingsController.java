package com.example.my_mvc_project.controller;

import com.example.my_mvc_project.dtos.product.ProductGetDto;
import com.example.my_mvc_project.dtos.reports.ReportInputDto;
import com.example.my_mvc_project.dtos.reports.SellingDto;
import com.example.my_mvc_project.services.ProductService;
import com.example.my_mvc_project.services.SellingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@RequestMapping("/selling")
public class SellingsController {
    private final SellingService sellingService;
    private final ProductService productService;
    @GetMapping("/save1")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public String save1(Model model,@RequestParam(required = false,defaultValue = "0") int page,
                        @RequestParam(required = false)String name){
        Page<ProductGetDto> products;
        if (name!=null && name.isBlank()) {
             products = productService.productsByName(PageRequest.of(page, 3),name);
        }else {
            products=productService.products(PageRequest.of(page,3));
        }
        model.addAttribute("prods",products);
        int pages = products.getTotalPages();
        List<Integer> pagesList=new LinkedList<>();
        for (int i = 0; i < pages; i++) {
            pagesList.add(i);
        }
        model.addAttribute("pages", pagesList);
        return "selling/select";
    }
    @GetMapping("/save2")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public String save2(@RequestParam long productId,@RequestParam long count,Model model){
        ProductGetDto dto = productService.get(productId);
        if (dto.getCount()<count){
            throw new RuntimeException("Afsus! Ushbu mahsulotdan faqatgina %s ta qoldi".formatted(dto.getCount()));
        }
        model.addAttribute("p_id",productId);
        model.addAttribute("count",count);
        model.addAttribute("p_price",dto.getPrice()*count);
        return "selling/input";
    }
    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute ReportInputDto dto,
                       @RequestParam(required = false,defaultValue = "0")int page,
                       Model model){
        Page<SellingDto> sellings = sellingService.save(dto,PageRequest.of(page,5,Sort.by("dateTime").descending()));
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
        model.addAttribute("now", LocalDate.now());
        model.addAttribute("date",date);
        return "selling/get";
    }
    @GetMapping("/get-date/{date}")
    public String getByDate(@PathVariable LocalDate date,Model model){
        Page<SellingDto> sellings = sellingService.sellingsByDate(
                date, PageRequest.of(0, 10, Sort.by("dateTime").descending()));
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
        Page<SellingDto> sellings = sellingService.sellings(PageRequest.of(page, 5,Sort.by("dateTime").descending())
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
                        (page, 3, Sort.by("dateTime").descending()));
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
