package com.example.my_mvc_project.dtos.reports;

import com.example.my_mvc_project.dtos.product.ProductGetDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class SellingDto {
       private UUID id;
       private ProductGetDto product;
       private Long count;
       private Double soldPrice;
       private String soldPrice2;
       private LocalDateTime dateTime;
       private String firstNameOfEmployee;
       private String lastNameOfEmployee;
}
