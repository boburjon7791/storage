package com.example.my_mvc_project.services;

import com.example.my_mvc_project.dtos.reports.ReportInputDto;
import com.example.my_mvc_project.dtos.reports.SellingDto;
import com.example.my_mvc_project.dtos.reports.SoldPersonDaily;
import com.example.my_mvc_project.dtos.reports.SoldPersonMonthly;
import com.example.my_mvc_project.entities.Basket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


public interface SellingService {
   List<SoldPersonDaily> dailyReport(LocalDate date);
   List<SoldPersonMonthly> monthlyReport(int month);
   Basket getBasket();
   Basket putToBasket(Long productId,Long productCount);
   Basket removeProductFromBasket(Long productId);
   void clearBasket();
   Page<SellingDto> startSelling();
   void save(ReportInputDto dto);
   Page<SellingDto> sellingsByTime(LocalTime startTime, LocalTime endTime, Pageable pageable);
   Page<SellingDto> sellingsByDate(LocalDate date, Pageable pageable);
   Page<SellingDto> sellingsByProduct(Pageable pageable,Long productId);
   Page<SellingDto> sellingsByCount(Pageable pageable,Long countStart,Long countEnd);

   Page<SellingDto> sellingsBySoldPrice(Pageable pageable,Double soldPriceStart,Double soldPriceEnd);

    Page<SellingDto> sellings(Pageable of);
}
