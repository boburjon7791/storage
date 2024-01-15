package com.example.my_mvc_project.services;

import com.example.my_mvc_project.dtos.employee.EmployeeGetDto;
import com.example.my_mvc_project.dtos.product.ProductGetDto;
import com.example.my_mvc_project.dtos.reports.ReportInputDto;
import com.example.my_mvc_project.dtos.reports.SellingDto;
import com.example.my_mvc_project.entities.Basket;
import com.example.my_mvc_project.entities.Employee;
import com.example.my_mvc_project.entities.Product;
import com.example.my_mvc_project.entities.Selling;
import com.example.my_mvc_project.exceptions.BadParamException;
import com.example.my_mvc_project.exceptions.NotFoundException;
import com.example.my_mvc_project.mappers.EmployeeMapper;
import com.example.my_mvc_project.mappers.ProductMapper;
import com.example.my_mvc_project.mappers.SellingMapper;
import com.example.my_mvc_project.repositories.BasketUtils;
import com.example.my_mvc_project.repositories.EmployeeRepository;
import com.example.my_mvc_project.repositories.ProductRepository;
import com.example.my_mvc_project.repositories.SellingRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
@AllArgsConstructor
public class SellingServiceImpl implements SellingService {
    private final EmployeeRepository employeeRepository;
    private final ProductRepository productRepository;
    private final SellingRepository sellingRepository;
    private final SellingMapper sellingMapper;
    private final ProductMapper productMapper;
    private final EmployeeMapper employeeMapper;
    private final BasketUtils basketUtils;
    private final ProductService productService;

    @Override
    public Basket getBasket() {
        CustomUserDetails customUserDetails=(CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return basketMatcher(basketUtils.get(customUserDetails.employee.getId()));
    }

    @Override
    public Basket putToBasket(Long productId, Long productCount) {
        CustomUserDetails customUserDetails=(CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return basketMatcher(basketUtils.putProductToBasket
                (customUserDetails.employee.getId(), productId, productCount));
    }
    public Basket basketMatcher(Basket basket){
        AtomicReference<Double> totalSumma=new AtomicReference<>(0d);
        basket.productsAndCounts.forEach((product, count) -> totalSumma.getAndUpdate(aDouble -> aDouble+product.getPrice()*count));
        basket.setPrice(totalSumma.get());
        return basket;
    }

    @Override
    public Basket removeProductFromBasket(Long productId) {
        CustomUserDetails customUserDetails=(CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return basketMatcher(basketUtils.removeProductByEmployeeId
                (customUserDetails.employee.getId(), productId));
    }

    @Override
    public void clearBasket() {
        CustomUserDetails customUserDetails=(CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        basketUtils.clear(customUserDetails.employee.getId());
    }

    @Override
    @Transactional
    public Page<SellingDto> startSelling() {
        CustomUserDetails customUserDetails=(CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Basket basket = basketUtils.get(customUserDetails.employee.getId());
        if (basket.productsAndCounts.isEmpty()) {
            throw new NotFoundException("Savat bo'sh");
        }
        List<Long> productsIds = basket.productsAndCounts.keySet().stream().map(ProductGetDto::getId).toList();
        List<Long> counts = basket.productsAndCounts.values().stream().toList();
        for (int i = 0; i < productsIds.size(); i++) {
            ProductGetDto product = productService.get(productsIds.get(i));
            save(new ReportInputDto(product.getId(),counts.get(i),product.getPrice()*counts.get(i)));
        }
        return getSellingDtos(sellingRepository.findAllByDate
                (PageRequest.of(0,3,
                        Sort.by(Sort.Direction.DESC,"dateTime")), LocalDate.now()));
    }

    @Override
    public void save(ReportInputDto dto) {
        Double price = dto.price();
        if (price==null || price==0) {
            price=productRepository.findById(dto.productId())
                    .orElseThrow(()->new NotFoundException("Mahsulot topilmadi")).getPrice()*dto.count();
        }
        Product product = productRepository.findById(dto.productId())
                .orElseThrow(() -> new NotFoundException("Mahsulot topilmadi"));
        CustomUserDetails userDetails= (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("userDetails.getEmployee() = " + userDetails.getEmployee());
        if (userDetails.getEmployee()==null) {
            throw new RuntimeException();
        }
        Employee employee = employeeRepository.findById(userDetails.getEmployee().getId())
                .orElseThrow(() -> new NotFoundException("Ishchi topilmadi"));
        Selling selling = Selling.builder()
                .count(dto.count())
                .soldPrice(price)
                .employee(employee)
                .product(product)
                .build();
        Selling saved= sellingRepository.save(selling);
        if (product.getCount()<saved.getCount() || productRepository
                .subtractCountById(saved.getCount(),saved.getProduct().getId())<1) {
            throw new BadParamException("Xatolik yuz berdi");
        }
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
            if (!employeeRepository.existsById(sell.getEmployee().getId())) {
                throw new NotFoundException("Ishchi topilmadi");
            }
            SellingDto dto = sellingMapper.toDto(sell);
            dto.setProduct(productMapper.toDto(sell.getProduct()));
            EmployeeGetDto getDto = employeeMapper.toDto(sell.getEmployee());
            dto.setFirstNameOfEmployee(getDto.getFirstName());
            dto.setLastNameOfEmployee(getDto.getLastName());
            System.out.println("dto = return " + dto);
            return dto;
        });
    }
}
