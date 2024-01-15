package com.example.my_mvc_project.repositories;

import com.example.my_mvc_project.dtos.product.ProductGetDto;
import com.example.my_mvc_project.entities.Basket;
import com.example.my_mvc_project.exceptions.NotFoundException;
import com.example.my_mvc_project.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@AllArgsConstructor
public class BasketUtils {
    private final ProductRepository productRepository;
    private final ProductService productService;
    /**
     * key is employee id, value is employee's basket
    * */
    public static final Map<Long, Basket> baskets=new ConcurrentHashMap<>();

    private final Object object=new Object();
    public Basket get(Long employeeId){
        if (!baskets.containsKey(employeeId)) {
            throw new NotFoundException("Sizning savatingiz bo'sh");
        }
        return baskets.get(employeeId);
    }
    public Basket putProductToBasket(Long employeeId,Long productId,Long count){
        synchronized (object){
            Basket basket;
            ProductGetDto product = productService.get(productId);
            if (baskets.containsKey(employeeId)) {
                basket = get(employeeId);
            }else {
                basket=new Basket();
            }
            Long productCount = productRepository.findById(productId)
                    .orElseThrow(() -> new NotFoundException("Mahsulot topilmadi"))
                    .getCount();
            if (productCount <count) {
                throw new NotFoundException("Ushbu mahsulotdan faqatgina %s ta qoldi".formatted(productCount));
            }
            basket.productsAndCounts.put(product,count);
            basket.setProductsAndCounts(basket.productsAndCounts);
            baskets.put(employeeId,basket);
            return baskets.get(employeeId);
        }
    }
    public Basket removeProductByEmployeeId(Long employeeId,Long productId){
        synchronized (object){
            Basket basket = get(employeeId);
            basket.productsAndCounts.remove(productService.get(productId));
            return baskets.get(employeeId);
        }
    }
    public void clear(Long employeeId){
        baskets.put(employeeId,new Basket());
    }
}