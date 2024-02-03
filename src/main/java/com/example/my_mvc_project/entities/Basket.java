package com.example.my_mvc_project.entities;

import com.example.my_mvc_project.dtos.product.ProductGetDto;
import lombok.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@Setter
public class Basket {
    /**
     * key is product id, value is count
     * */
    @Builder.Default
    public Map<ProductGetDto,Double> productsAndCounts=new ConcurrentHashMap<>();

    @Builder.Default
    private Double price=0d;

    private String priceTotal;

    private final Object object=new Object();

    public void setProductsAndCounts(Map<ProductGetDto, Double> productsAndCounts) {
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

