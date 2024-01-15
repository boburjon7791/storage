package com.example.my_mvc_project.entities;

import com.example.my_mvc_project.dtos.product.ProductGetDto;
import com.example.my_mvc_project.exceptions.NotFoundException;
import lombok.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
public class Basket {
    /**
     * key is product id, value is count
     * */
    public Map<ProductGetDto,Long> productsAndCounts=new ConcurrentHashMap<>();

    @Builder.Default
    private Double price=0d;

    private final Object object=new Object();

    public void setProductsAndCounts(Map<ProductGetDto, Long> productsAndCounts) {
        synchronized (object){
            this.productsAndCounts = productsAndCounts;
        }
    }

    public void setPrice(Double price) {
        synchronized (object){
            this.price = price;
        }
    }
}

