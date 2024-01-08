package com.example.my_mvc_project.entities;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank
    @Column(nullable = false)
    private String name;

    @PositiveOrZero
    @NotNull
    @Column(nullable = false)
    private Double price;

    @Nullable
    private String colors;

    @Nullable
    private String image;

    @Builder.Default
    @Column(name = "date_time",nullable = false,updatable = false)
    private LocalDateTime dateTime=LocalDateTime.now();

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private Long count;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    private Set<Selling> sellings;
}