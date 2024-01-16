package com.example.my_mvc_project.entities;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "product", indexes = {
        @Index(name = "idx_product_price_color_id", columnList = "price,count")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uc_product_name", columnNames = {"name"})
})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Mahsulot nomi bo'sh bo'lishi mumkin emas")
    @Column(nullable = false)
    private String name;

    @Positive(message = "Mahsulot narxi 0 katta bo'lishi kerak")
    @NotNull(message = "Mahsulot narxi bo'sh bo'lishi mumkin emas")
    @Column(nullable = false)
    private Double price;

    @Nullable
    private String colors;

    @Nullable
    private String image;

    @NotNull(message = "Mahsulot miqdori bo'sh bo'lmasligi kerak")
    @PositiveOrZero(message = "Mahsulot miqdori 0 yoki undan katta bo'lishi kerak")
    @Column(nullable = false)
    private Long count;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    private Set<Selling> sellings;
}