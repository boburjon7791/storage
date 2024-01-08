package com.example.my_mvc_project.services;

import com.example.my_mvc_project.dtos.reports.ReportInputDto;
import com.example.my_mvc_project.dtos.reports.SellingDto;
import com.example.my_mvc_project.entities.Selling;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface SellingService {
   Page<SellingDto> save(ReportInputDto dto,Pageable pageable);
   Page<SellingDto> sellingsByTime(LocalTime startTime, LocalTime endTime, Pageable pageable);
   Page<SellingDto> sellingsByDate(LocalDate date, Pageable pageable);
   List<Selling> sellingsByDateList(LocalDate date);
   Page<SellingDto> sellingsByProduct(Pageable pageable,Long productId);
   Page<SellingDto> sellingsByCount(Pageable pageable,Long countStart,Long countEnd);
   Page<SellingDto> sellingsBySoldPrice(Pageable pageable,Double soldPriceStart,Double soldPriceEnd);

    Page<SellingDto> sellings(Pageable of);
}
