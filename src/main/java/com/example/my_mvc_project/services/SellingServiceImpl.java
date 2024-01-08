package com.example.my_mvc_project.services;

import com.example.my_mvc_project.dtos.reports.ReportInputDto;
import com.example.my_mvc_project.dtos.reports.SellingDto;
import com.example.my_mvc_project.entities.Product;
import com.example.my_mvc_project.entities.Selling;
import com.example.my_mvc_project.exceptions.BadParamException;
import com.example.my_mvc_project.exceptions.NotFoundException;
import com.example.my_mvc_project.mappers.ProductMapper;
import com.example.my_mvc_project.mappers.SellingMapper;
import com.example.my_mvc_project.repositories.ProductRepository;
import com.example.my_mvc_project.repositories.SellingRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class SellingServiceImpl implements SellingService {
    private final ProductRepository productRepository;
    private final SellingRepository sellingRepository;
    private final SellingMapper sellingMapper;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public Page<SellingDto> save(ReportInputDto dto, Pageable pageable) {
        Double price = dto.price();
        if (price==null || price==0) {
            price=productRepository.findById(dto.productId())
                    .orElseThrow(()->new NotFoundException("Mahsulot topilmadi")).getPrice()*dto.count();
        }
        Product product = productRepository.findById(dto.productId())
                .orElseThrow(() -> new NotFoundException("Mahsulot topilmadi"));
        Selling selling = Selling.builder()
                .count(dto.count())
                .soldPrice(price)
                .product(product)
                .build();
        Selling saved = sellingRepository.save(selling);
        if (product.getCount()<saved.getCount() || productRepository
                .subtractCountById(saved.getCount(),saved.getProduct().getId())<1) {
            throw new BadParamException("Xatolik yuz berdi");
        }
        return getSellingDtos(sellingRepository.findAllByDate(pageable , LocalDate.now()));
    }

    @Override
    public Page<SellingDto> sellingsByTime(LocalTime startTime, LocalTime endTime, Pageable pageable) {
        return getSellingDtos(sellingRepository.findAllByBetweenTime(startTime, endTime, pageable));
    }

    @Override
    public Page<SellingDto> sellingsByDate(LocalDate date, Pageable pageable) {
        return getSellingDtos(sellingRepository.findAllByDate(pageable, date));
    }

    @Override
    public List<Selling> sellingsByDateList(LocalDate date) {
        return sellingRepository.findAllByDateTime(date);
    }

    @Override
    public Page<SellingDto> sellingsByProduct(Pageable pageable, Long productId) {
        return getSellingDtos(sellingRepository.findAllByProduct(productId, pageable));
    }

    @Override
    public Page<SellingDto> sellingsByCount(Pageable pageable, Long countStart, Long countEnd) {
        return getSellingDtos(sellingRepository.findAllByBetweenCount(countStart, countEnd, pageable));
    }

    @Override
    public Page<SellingDto> sellingsBySoldPrice(Pageable pageable, Double soldPriceStart, Double soldPriceEnd) {
        return getSellingDtos(sellingRepository.findAllByBetweenSoldPrice(soldPriceStart, soldPriceEnd, pageable));
    }

    @Override
    public Page<SellingDto> sellings(Pageable pageable) {
        return getSellingDtos(sellingRepository.findAll(pageable));
    }
    private Page<SellingDto> getSellingDtos(Page<Selling> sellings) {
        if (sellings.isEmpty()) {
            throw new NotFoundException("Savdolar topilmadi");
        }
        return sellings.map(sell->{
            if (!productRepository.existsById(sell.getProduct().getId())) {
                throw new NotFoundException("Sotilgan mahsulot topilmadi");
            }
            SellingDto dto = sellingMapper.toDto(sell);
            dto.setProduct(productMapper.toDto(sell.getProduct()));
            return dto;
        });
    }
}
